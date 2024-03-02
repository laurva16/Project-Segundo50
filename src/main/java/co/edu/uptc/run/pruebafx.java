package co.edu.uptc.run;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

    public class pruebafx extends Application {
        private Stage primaryStage;
    
        @Override
        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage;
    
            // Crear la escena principal y establecerla en el escenario principal
            Scene scene1 = new Scene(createScene1Content(), 400, 300);
            primaryStage.setScene(scene1);
            primaryStage.setTitle("Scene 1");
            primaryStage.show();
        }
    
        private VBox createScene1Content() {
            VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER);
    
            Button botonCambiar = new Button("Cambiar a Escena 2");
            botonCambiar.setOnAction(e -> cambiarAEscena2());
    
            layout.getChildren().addAll(botonCambiar);
            return layout;
        }
    
        private void cambiarAEscena2() {
            Clase2 clase2 = new Clase2(primaryStage);
            primaryStage.setScene(clase2.crearEscena());
        }
        
    
        public static void main(String[] args) {
            launch(args);
        }
    }
    
    class Clase2 {
        private Stage primaryStage;
    
        public Clase2(Stage primaryStage) {
            this.primaryStage = primaryStage;
        }
    
        public Scene crearEscena() {
            VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER);
    
            Button botonCambiar = new Button("Volver a Escena 1");
            botonCambiar.setOnAction(e -> cambiarAEscena1());
    
            layout.getChildren().addAll(botonCambiar);
            return new Scene(layout, 400, 300);
        }
    
        private void cambiarAEscena1() {
            pruebafx main = new pruebafx();
            main.start(primaryStage);
        }
    }
    