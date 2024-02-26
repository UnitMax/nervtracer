package unitmax;

import java.util.Optional;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import unitmax.graphics.HitRecord;
import unitmax.graphics.Hittable;
import unitmax.graphics.Hittables;
import unitmax.graphics.Ray;
import unitmax.graphics.Sphere;
import unitmax.math.Interval;
import unitmax.math.Vec3;

/**
 * JavaFX App
 */
public class App extends Application {

    final int imageWidthFX = 450;
    final int imageHeightFX = 400;

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(imageWidthFX - 10);
        progressBar.setProgress(0);

        // World
        Hittables world = new Hittables();
        world.add(new Sphere(Vec3.create(0, 0, -1), 0.5));
        world.add(new Sphere(Vec3.create(1, 0, -1), 0.5));
        world.add(new Sphere(Vec3.create(-1, 0, -1), 0.5));
        world.add(new Sphere(Vec3.create(0, -100.5, -1), 100));

        WritableImage img = new WritableImage(400, 400 / (int) (16.0 / 9.0));
        Task<Void> task = new Task<>() {

            private Vec3 rayColor(Ray r, Hittable world) {
                Optional<HitRecord> rec = world.hit(r, new Interval(0, Double.POSITIVE_INFINITY));
                if (rec.isPresent()) {
                    return Vec3.create(1, 1, 1).add(rec.get().getNormal()).multScalar(0.5);
                }
                Vec3 unitDirection = r.getDirection().unitVector();
                var a = (unitDirection.y() + 1.0) * 0.5;
                return Vec3.create(1.0, 1.0, 1.0).multScalar(1.0 - a).add(Vec3.create(0.5, 0.7, 1.0).multScalar(a));
            }

            private Color vec3toRGB(Vec3 v) {
                int ir = (int) (255.999 * v.x());
                int ig = (int) (255.999 * v.y());
                int ib = (int) (255.999 * v.z());
                return Color.rgb(ir, ig, ib);
            }

            private void drawImage(WritableImage img) throws Exception {
                PixelWriter pixelWriter = img.getPixelWriter();

                // img width + height
                double aspectRatio = 16.0 / 9.0;
                int imageWidth = 400;
                int imageHeight = imageWidth / (int) aspectRatio;
                imageHeight = imageHeight < 1 ? 1 : imageHeight; // clamp to 1

                // camera
                double focalLength = 1.0;
                double viewportHeight = 2.0;
                double viewportWidth = viewportHeight * ((double) imageWidth / (double) imageHeight);
                Vec3 cameraCenter = Vec3.create(0, 0, 0);

                // viewport vectors
                Vec3 viewportU = Vec3.create(viewportWidth, 0, 0);
                Vec3 viewportV = Vec3.create(0, -viewportHeight, 0);

                // horizontal and vertical delta vectors pixel to pixel
                Vec3 pixelDeltaU = viewportU.divScalar(imageWidth);
                Vec3 pixelDeltaV = viewportV.divScalar(imageHeight);

                // upper left pixel (0,0) in image space
                Vec3 viewportUpperLeft = cameraCenter.sub(Vec3.create(0, 0, focalLength)).sub(viewportU.divScalar(2))
                        .sub(viewportV.divScalar(2));
                Vec3 pixel00Location = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multScalar(0.5));

                for (int j = 0; j < imageHeight; j++) {
                    final double progress = (double) j / imageHeight;
                    updateProgress(progress, 1.0);

                    for (int i = 0; i < imageWidth; i++) {
                        var pixelCenter = pixel00Location.add(pixelDeltaU.multScalar(i)).add(pixelDeltaV.multScalar(j));
                        var rayDirection = pixelCenter.sub(cameraCenter);
                        Ray r = new Ray(cameraCenter, rayDirection);

                        pixelWriter.setColor(i, j, vec3toRGB(rayColor(r, world)));
                    }
                }
            }

            @Override
            protected Void call() throws Exception {
                drawImage(img);
                return null;
            }
        };
        progressBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        VBox root = new VBox(5);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(progressBar, new javafx.scene.image.ImageView(img), label);

        Scene scene = new Scene(root, imageWidthFX, imageHeightFX + 50);
        stage.setTitle("nervtracer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}