package unitmax.graphics;

import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import unitmax.math.Interval;
import unitmax.math.Util;
import unitmax.math.Vec3;

public class Camera {
    
    private double aspectRatio = 1.0;

    private int imageWidth = 100;
    private int imageHeight;

    private int samplesPerPixel = 10;
    private int maxDepth = 10; // Max number of ray bounces

    private Vec3 center = Vec3.create(0, 0, 0);
    private Vec3 pixel00Location;
    private Vec3 pixelDeltaU;
    private Vec3 pixelDeltaV;

    private static final boolean USE_LAMBERTIAN = true;

    private PixelWriter pixelWriter;

    public Camera(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public void render(Hittable world) {
        render(world, null);
    }

    private double linearToGamma(double linearComponent) {
        return Math.sqrt(linearComponent);
    }

    private void setColor(int x, int y, Vec3 pixelColor, int samplesPerPixel) {
        var scale = 1.0 / (double) samplesPerPixel;
        var r = linearToGamma(pixelColor.x() * scale);
        var g = linearToGamma(pixelColor.y() * scale);
        var b = linearToGamma(pixelColor.z() * scale);
        Vec3 newPixelColor = Vec3.create(r, g, b);

        pixelWriter.setColor(x, y, vec3toRGB(newPixelColor));
    }

    public void render(Hittable world, Consumer<Double> updateProgressCallback) {
        initialize();

        for (int j = 0; j < imageHeight; j++) {
            for (int i = 0; i < imageWidth; i++) {
                final double progress = (double) j / imageHeight;
                if (updateProgressCallback != null) {
                    updateProgressCallback.accept(progress);
                }
                
                Vec3 pixelColor = Vec3.create(0, 0, 0);
                for (int sample = 0; sample < samplesPerPixel; sample++) {
                    Ray ray = getRay(i, j);
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
                }
                this.setColor(i, j, pixelColor, samplesPerPixel);
            }
        }
    }

    private void initialize() {
        imageHeight = (int) ((double) imageWidth / aspectRatio);
        imageHeight = imageHeight < 1 ? 1 : imageHeight; // clamp

        // Viewport dimensions
        double focalLength = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double) imageWidth / (double) imageHeight);

        var viewportU = Vec3.create(viewportWidth, 0, 0);
        var viewportV = Vec3.create(0, -viewportHeight, 0);

        pixelDeltaU = viewportU.divScalar(imageWidth);
        pixelDeltaV = viewportV.divScalar(imageHeight);

        var viewportUpperLeft = center.sub(Vec3.create(0, 0, focalLength)).sub(viewportU.divScalar(2.0))
                .sub(viewportV.divScalar(2.0));
        pixel00Location = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multScalar(0.5));
    }

    private Vec3 rayColor(Ray ray, Hittable world, int depth) {
        if (depth == 0) {
            return Vec3.create(0, 0, 0);
        }

        Optional<HitRecord> record = world.hit(ray, new Interval(0.001, Double.POSITIVE_INFINITY));

        if (record.isPresent()) {
            Vec3 direction;
            if (USE_LAMBERTIAN) {
                direction = record.get().getNormal().add(Vec3.randomUnitVector());
            } else {
                direction = Vec3.randomOnHemisphere(record.get().getNormal());
            }
            return rayColor(new Ray(record.get().getPoint(), direction), world, depth - 1).multScalar(0.5);
        }

        Vec3 unitDirection = ray.getDirection().unitVector();
        var a = 0.5 * (unitDirection.y() + 1.0);
        return Vec3.create(1, 1, 1).multScalar(1.0 - a).add(Vec3.create(0.5, 0.7, 1.0).multScalar(a));
    }

    private static final Color vec3toRGB(Vec3 v) {
        final Interval intensity = new Interval(0.0, 0.999);
        int ir = (int) ((double) 256 * intensity.clamp(v.x()));
        int ig = (int) ((double) 256 * intensity.clamp(v.y()));
        int ib = (int) ((double) 256 * intensity.clamp(v.z()));
        return Color.rgb(ir, ig, ib);
    }

    private Ray getRay(int i, int j) {
        var pixelCenter = pixel00Location.add(pixelDeltaU.multScalar(i)).add(pixelDeltaV.multScalar(j));
        var pixelSample = pixelCenter.add(pixelSampleSquare());

        var rayOrigin = center;
        var rayDirection = pixelSample.sub(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    private Vec3 pixelSampleSquare() {
        // /-----------\
        // |a      *  b|
        // |           |
        // |   *       |
        // |     x  *  | <--- Origin (0,0)
        // |           |
        // | *         |
        // |d         c|
        // \-----------/
        // a = (-0.5,-0.5)
        // b = (0.5,-0.5)
        // c = (0.5,0.5)
        // d = (-0.5,0.5)
        // Returns randomly sampled point like *
        var px = -0.5 + Util.randomDouble();
        var py = -0.5 + Util.randomDouble();
        return pixelDeltaU.multScalar(px).add(pixelDeltaV.multScalar(py));
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getSamplesPerPixel() {
        return samplesPerPixel;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setSamplesPerPixel(int samplesPerPixel) {
        this.samplesPerPixel = samplesPerPixel;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

}
