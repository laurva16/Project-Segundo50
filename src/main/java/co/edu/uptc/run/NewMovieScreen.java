package co.edu.uptc.run;

import java.io.File;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class NewMovieScreen {
    TextField text1 = new TextField();
    TextField text2 = new TextField();
    TextField text3 = new TextField();
    TextField text4 = new TextField();
    Label labelName = new Label("Movie name:");
    Label labelDirector = new Label("Director name:");
    Label labelDescription = new Label("Description:");
    Label labelDuration = new Label("Duration:");
    Label labelCategory = new Label("Category:");    
    Label labelWarning;

    private Scene newMovieScene;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    Button cancelButton = new Button();
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    
    AdminController adminC;
    CategoryController categoryC;

    private EntryWindow entryWindow;

    public NewMovieScreen(EntryWindow entryWindow, AdminController adminC){
        this.adminC = adminC;
        this.entryWindow = entryWindow;
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> choiceBox.getItems().add(category.getName()));
    }
    

    public void switchScene() {
        BorderPane root3 = new BorderPane();
        GridPane gridPane = new GridPane();

        text1.setPrefWidth(300);
        text2.setPrefWidth(300);
        text3.setPrefWidth(300);
        text4.setPrefWidth(300);

        labelWarning = new Label("* All fields must be filled!");
        labelWarning.setVisible(false);

        choiceBox.setMaxSize(300, 20);

        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);

        GridPane.setConstraints(labelWarning, 1, 5);
        GridPane.setHalignment(labelWarning, javafx.geometry.HPos.RIGHT);

        GridPane.setConstraints(text1, 1, 0);
        GridPane.setConstraints(text2, 1, 1);
        GridPane.setConstraints(text3, 1, 2);
        GridPane.setConstraints(text4, 1, 3);
        GridPane.setConstraints(choiceBox, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, text1, labelDirector, text2, labelDescription, text3, labelDuration,
                text4, labelCategory, choiceBox, labelWarning);
        root3.setCenter(gridPane);

        // Save buttton
        Button acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 5);
        acceptButton.setTranslateY(100);
        acceptButton.setText("Save");
        acceptButton.setPrefWidth(150);
        acceptButton.setOnAction(event -> addNewMovie());
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);

        // Cancel buttton
        
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setTranslateY(100);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);

        cancelButton.setOnAction(event -> returnScene());
        gridPane.getChildren().addAll(acceptButton, cancelButton);

        // Crear la escena
        newMovieScene = new Scene(root3, screenWidth, screenHeight);
        // aplicar CSS
        newMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");
        labelWarning.setId("warning");
        root3.setId("root3");
        gridPane.setId("gridPane");
    }

    public Scene getNewMovieScene() {
        return newMovieScene;
    }


    public void returnScene() {
      entryWindow.showMovieScene();
    }

    public void addNewMovie() {
        Boolean numberValid;
        try {
            Integer.parseInt(text4.getText());
            numberValid = true;
        } catch (Exception e) {
            numberValid = false;
        }
       
        if(!numberValid && (!text4.getText().isEmpty())){
            labelWarning.setText("* Duration format is invalid !");
            labelWarning.setVisible(true);
        } else{
            labelWarning.setText("* All fields must be filled!");
            if (text1.getText().isEmpty() || text2.getText().isEmpty() || text3.getText().isEmpty() || text4.getText().isEmpty() || (choiceBox.getValue() == null)) {
                labelWarning.setVisible(true);
              
            }else{
                // ventana de confirmacion
                labelWarning.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar");
                alert.setHeaderText(null);
                alert.setContentText("You want to save to changes?");
    
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
    
                        adminC.addMovie(text1.getText(), text2.getText(), text3.getText(), Integer.parseInt(text4.getText()),
                                choiceBox.getValue());
                                returnScene();
                    } else {
                    }
                });
            }
        }
    }

}

