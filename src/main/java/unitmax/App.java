package unitmax;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unitmax.graphics.Camera;
import unitmax.graphics.Hittables;
import unitmax.graphics.Sphere;
import unitmax.math.Vec3;

/**
 * JavaFX App
 */
public class App extends Application {

    final int imageWidthFX = 450;
    final int imageHeightFX = 450;

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

        WritableImage img = new WritableImage(400, (int)(400.0 / (16.0 / 9.0)));
        label.setText(label.getText() + "\n" + "W: " + img.getWidth() + " / H: " + img.getHeight());
        Task<Void> task = new Task<>() {

            private void drawImage(WritableImage img) throws Exception {
                PixelWriter pixelWriter = img.getPixelWriter();

                Camera camera = new Camera(pixelWriter);
                camera.setAspectRatio(16.0 / 9.0);
                camera.setImageWidth(400);
                camera.render(world, (d) -> {updateProgress(d, 1.0);});
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