package org.openjfx;

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

/**
 * JavaFX App
 */
public class App extends Application {

    final int imageWidth = 512;
    final int imageHeight = 512;

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(imageWidth - 10);
        progressBar.setProgress(0);

        WritableImage img = new WritableImage(imageWidth, imageHeight);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                PixelWriter pixelWriter = img.getPixelWriter();

                for (int j = 0; j < imageHeight; j++) {
                    final double progress = (double) j / imageHeight;
                    updateProgress(progress, 1.0);

                    for (int i = 0; i < imageWidth; i++) {
                        var r = (double) i / (imageWidth - 1);
                        var g = (double) j / (imageHeight - 1);
                        var b = 0;

                        int ir = (int) (255.999 * r);
                        int ig = (int) (255.999 * g);
                        int ib = (int) (255.999 * b);
                        Color color = Color.rgb(ir, ig, ib);
                        pixelWriter.setColor(i, j, color);
                    }
                }
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

        Scene scene = new Scene(root, imageWidth, imageHeight + 50);
        stage.setTitle("nervtracer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}