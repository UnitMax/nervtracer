package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    final int imageWidth = 256;
    final int imageHeight = 256;

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        WritableImage img = new WritableImage(256, 256);
        PixelWriter pixelWriter = img.getPixelWriter();

        for (int j = 0; j < imageHeight; j++) {
            for (int i = 0; i < imageWidth; i++) {
                var r = (double) i / (double) (imageWidth - 1);
                var g = (double) j / (double) (imageHeight - 1);
                var b = 0;

                int ir = (int) (255.999f * r);
                int ig = (int) (255.999f * g);
                int ib = (int) (255.999f * b);
                var color = Color.rgb(ir, ig, ib);
                pixelWriter.setColor(j, i, color);
            }
        }

        StackPane root = new StackPane();
        root.getChildren().add(new javafx.scene.image.ImageView(img));
        root.getChildren().add(label);

        Scene scene = new Scene(root, 256, 256);

        stage.setTitle("nervtracer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}