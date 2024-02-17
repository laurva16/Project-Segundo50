package co.edu.uptc.run;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class pruebafx extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creamos un nuevo objeto Label con el mensaje "Hola, funcionó"
        Label label = new Label("Hola, funcionó");

        // Creamos un layout StackPane y agregamos el Label
        StackPane root = new StackPane();
        root.getChildren().add(label);

        // Creamos la escena y le asignamos el layout
        Scene scene = new Scene(root, 300, 200);

        // Configuramos el Stage (ventana principal)
        primaryStage.setTitle("Hola Mundo JavaFX");
        primaryStage.setScene(scene);

        // Mostramos la ventana
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Lanzamos la aplicación
        launch(args);
    }
}
