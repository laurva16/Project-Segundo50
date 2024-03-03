package co.edu.uptc.run;

import java.io.File;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.utilities.FileManagement;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;

public class MovieScreen {

    TextField text1 = new TextField();
    TextField text2 = new TextField();
    TextField text3 = new TextField();
    TextField text4 = new TextField();
    TextField nameField;
    TextField directorField;
    TextField descriptionField;
    TextField durationField;

    Button cancelButton = new Button();

    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    ChoiceBox <String> fileBox = new ChoiceBox<>();
    Label labelName = new Label("Movie name:");
    Label labelDirector = new Label("Director name:");
    Label labelDescription = new Label("Description:");
    Label labelDuration = new Label("Duration:");
    Label labelCategory = new Label("Category:");
    Label labelWarning;       
    Label labelFileName = new Label("File Video:");

    //
    private Scene newMovieScene;
    private Scene editMovieScene;
    //
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    FileManagement fm;
    AdminController adminC;
    CategoryController categoryC;

    private Stage primaryStage;

    public MovieScreen(Stage primaryStage, AdminController adminC) {
        this.primaryStage = primaryStage;
        //
        this.adminC = adminC;
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> choiceBox.getItems().add(category.getName()));
    }

    public Scene newMovieScene() {
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

        GridPane.setConstraints(labelWarning, 1, 6);
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

        GridPane.setConstraints(acceptButton, 0, 7);
        acceptButton.setTranslateY(60);
        acceptButton.setText("Save");
        acceptButton.setPrefWidth(150);
        acceptButton.setOnAction(event -> addNewMovie());
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);

        // Cancel buttton

        GridPane.setConstraints(cancelButton, 1, 7);
        cancelButton.setTranslateY(60);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);
        
        cancelButton.setOnAction(event -> returnScene());
        gridPane.getChildren().addAll(acceptButton, cancelButton);

        //FILE Box
        fm = new FileManagement();
        fileBox = fm.getFileMoviesNames();
        fileBox.setMaxSize(300, 20);

        GridPane.setConstraints(labelFileName, 0, 5);
        GridPane.setConstraints(fileBox, 1, 5);
       
        gridPane.getChildren().addAll(fileBox, labelFileName);
        //

        // Crear la escena
        newMovieScene = new Scene(root3, screenWidth, screenHeight);
        // aplicar CSS
        newMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");
        labelWarning.setId("warning");
        root3.setId("root3");
        gridPane.setId("gridPane");
        return newMovieScene;
    }

    private void returnScene() {
        EntryWindow main = new EntryWindow();
        main.getScene1();
    }

    public void addNewMovie() {
        Boolean numberValid;
        try {
            Integer.parseInt(text4.getText());
            numberValid = true;
        } catch (Exception e) {
            numberValid = false;
        }

        if (!numberValid && (!text4.getText().isEmpty())) {
            labelWarning.setText("* Duration format is invalid !");
            labelWarning.setVisible(true);
        } else {
            labelWarning.setText("* All fields must be filled!");
            if (text1.getText().isEmpty() || text2.getText().isEmpty() || text3.getText().isEmpty()
                    || text4.getText().isEmpty() || (choiceBox.getValue() == null) || (fileBox.getValue() == null)) {
                labelWarning.setVisible(true);

            } else {
                // ventana de confirmacion
                labelWarning.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar");
                alert.setHeaderText(null);
                alert.setContentText("You want to save to changes?");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println(fileBox.getValue());
                        adminC.addMovie(text1.getText(), text2.getText(), text3.getText(),
                                Integer.parseInt(text4.getText()),
                                choiceBox.getValue(), fileBox.getValue());
                        returnScene();
                    } else {
                    }
                });
            }
        }
    }

    // EDIT

    public Scene editMovieScene(Movie movie) {

        BorderPane root3 = new BorderPane();

        GridPane gridPane = new GridPane();
        //
        nameField = new TextField();
        nameField.setPromptText("Ingrese la faculty");
        nameField.setText(movie.getName());

        directorField = new TextField();
        directorField.setPromptText("Ingrese el nombre del grupo");
        directorField.setText(movie.getAuthor());

        descriptionField = new TextField();
        descriptionField.setPromptText("Ingrese la initial");
        descriptionField.setText(movie.getDescription());

        durationField = new TextField();
        durationField.setPromptText("Ingrese el correo de contacto");
        durationField.setText(String.valueOf(movie.getDuration()));

        nameField.setPrefWidth(300);
        directorField.setPrefWidth(300);
        descriptionField.setPrefWidth(300);
        durationField.setPrefWidth(300);

        //
        GridPane.setConstraints(nameField, 1, 0);
        GridPane.setConstraints(directorField, 1, 1);
        GridPane.setConstraints(descriptionField, 1, 2);
        GridPane.setConstraints(durationField, 1, 3);
        GridPane.setConstraints(choiceBox, 1, 4);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);

        labelWarning = new Label("* All fields must be filled!");
        labelWarning.setVisible(false);

        choiceBox.setValue(movie.getCategory());
        choiceBox.setMaxSize(300, 20);

        gridPane.getChildren().setAll(nameField, directorField, descriptionField, durationField, labelName,
                labelDirector, labelDescription, labelDuration, labelCategory, labelWarning, choiceBox);

        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelWarning, 1, 6);
        GridPane.setHalignment(labelWarning, javafx.geometry.HPos.RIGHT);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        root3.setCenter(gridPane);

        // Save buttton
        Button acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 7);
        acceptButton.setTranslateY(60);
        acceptButton.setText("Save");
        acceptButton.setPrefWidth(150);

        acceptButton.setOnAction(event -> editMovie(movie));
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);

        // Cancel buttton
        Button cancelButton = new Button();

        GridPane.setConstraints(cancelButton, 1, 7);
        cancelButton.setTranslateY(60);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);

        cancelButton.setOnAction(event -> returnScene());
        gridPane.getChildren().addAll(acceptButton, cancelButton);

        //FILE Box
        fm = new FileManagement();
        fileBox = fm.getFileMoviesNames();
        fileBox.setMaxSize(300, 20);

        GridPane.setConstraints(labelFileName, 0, 5);
        GridPane.setConstraints(fileBox, 1, 5);
        
        fileBox.setValue(movie.getFileVideo());
        
        gridPane.getChildren().addAll(fileBox, labelFileName);
        //

        // Crear la escena
        editMovieScene = new Scene(root3, screenWidth, screenHeight);
        // aplicar CSS
        editMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");
        labelWarning.setId("warning");
        root3.setId("root3");
        gridPane.setId("gridPane");
        return editMovieScene;
    }

    public void editMovie(Movie movie) {
        Boolean numberValid;
        try {
            Integer.parseInt(durationField.getText());
            numberValid = true;
        } catch (Exception e) {
            numberValid = false;
        }

        labelWarning.setVisible(false);
        if (!numberValid && (!durationField.getText().isEmpty())) {
            labelWarning.setText("* Duration format is invalid !");
            labelWarning.setVisible(true);
        } else {
            if (nameField.getText().isEmpty() || directorField.getText().isEmpty()
                    || descriptionField.getText().isEmpty() || durationField.getText().isEmpty()
                    || (choiceBox.getValue() == null) || (fileBox.getValue() == null) ){
                labelWarning.setText("* All fields must be filled!");
                labelWarning.setVisible(true);
            } else {
                labelWarning.setVisible(false);
                movie.setName(nameField.getText());
                movie.setDescription(descriptionField.getText());
                movie.setAuthor(directorField.getText());
                movie.setDuration(Integer.parseInt(durationField.getText()));
                movie.setCategory(choiceBox.getValue());
                movie.setFileVideo(fileBox.getValue());
                adminC.updateMovieInformation(movie);
                returnScene();
            }
        }
    }
}
