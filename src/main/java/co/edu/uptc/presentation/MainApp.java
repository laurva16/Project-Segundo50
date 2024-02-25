package co.edu.uptc.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/co/edu/uptc/persistence/fxmlFiles/main.fxml"));

        // Crear la escena
        Scene scene = new Scene(root, 300, 200);

        // Configurar la escena y mostrarla
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ejemplo FXML");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
