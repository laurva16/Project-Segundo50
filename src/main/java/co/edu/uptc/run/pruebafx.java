package co.edu.uptc.run;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class pruebafx extends Application {

     @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Main Window");

        // Crear un botón para mostrar la ventana emergente
        Button openButton = new Button("Open Popup");
        openButton.setOnAction(event -> showPopup());

        // Colocar el botón en un contenedor
        VBox root = new VBox(10, openButton);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showPopup() {
        // Crear un nuevo Stage para la ventana emergente
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Popup Window");

        // Crear el contenido de la ventana emergente
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> popupStage.close());
        VBox popupRoot = new VBox(10, closeButton);
        popupRoot.setAlignment(Pos.CENTER);
        Scene popupScene = new Scene(popupRoot, 200, 100);

        // Establecer la escena y mostrar la ventana emergente
        popupStage.setScene(popupScene);
        popupStage.showAndWait(); // Espera hasta que se cierre la ventana emergente
    }

    public static void main(String[] args) {
        launch(args);
    }
}
