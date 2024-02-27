package unitmax.graphics;

import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import unitmax.math.Interval;
import unitmax.math.Vec3;

public class Camera {
    
    private double aspectRatio = 1.0;

    private int imageWidth = 100;
    private int imageHeight;

    private Vec3 center = Vec3.create(0, 0, 0);
    private Vec3 pixel00Location;
    private Vec3 pixelDeltaU;
    private Vec3 pixelDeltaV;

    private PixelWriter pixelWriter;

    public Camera(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public void render(Hittable world) {
        render(world, null);
    }

    public void render(Hittable world, Consumer<Double> updateProgressCallback) {
        initialize();

        for (int j = 0; j < imageHeight; j++) {
            for (int i = 0; i < imageWidth; i++) {
                final double progress = (double) j / imageHeight;
                if (updateProgressCallback != null) {
                    updateProgressCallback.accept(progress);
                }

                var pixelCenter = pixel00Location.add(pixelDeltaU.multScalar(i)).add(pixelDeltaV.multScalar(j));
                var rayDirection = pixelCenter.sub(center);
                Ray ray = new Ray(center, rayDirection);

                Vec3 pixelColor = rayColor(ray, world);
                pixelWriter.setColor(i, j, vec3toRGB(pixelColor));
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

    private Vec3 rayColor(Ray ray, Hittable world) {
        Optional<HitRecord> record = world.hit(ray, new Interval(0, Double.POSITIVE_INFINITY));

        if (record.isPresent()) {
            return Vec3.create(1, 1, 1).add(record.get().getNormal()).multScalar(0.5);
        }

        Vec3 unitDirection = ray.getDirection().unitVector();
        var a = 0.5 * (unitDirection.y() + 1.0);
        return Vec3.create(1, 1, 1).multScalar(1.0 - a).add(Vec3.create(0.5, 0.7, 1.0).multScalar(a));
    }

    private static final Color vec3toRGB(Vec3 v) {
        int ir = (int) (255.999 * v.x());
        int ig = (int) (255.999 * v.y());
        int ib = (int) (255.999 * v.z());
        return Color.rgb(ir, ig, ib);
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

}
