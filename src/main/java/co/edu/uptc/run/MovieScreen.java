package co.edu.uptc.run;

import java.io.File;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.utilities.FileManagement;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MovieScreen {

    TextField nameField;
    TextField directorField;
    TextField descriptionField;
    TextField durationField;

    Button cancelButton;
    Button acceptButton;
    Button fileButton;
    Button coverButton;

    ChoiceBox<String> categoryBox = new ChoiceBox<>();
    Label labelName = new Label("Movie name:");
    Label labelDirector = new Label("Director name:");
    Label labelDescription = new Label("Description:");
    Label labelDuration = new Label("Duration:");
    Label labelCategory = new Label("Category:");
    Label labelWarning = new Label();
    Label labelFileName = new Label("File video:");
    Label labelImageCover = new Label("Image cover:");

    File selectedFile;
    File selectedCover;
    private Stage primaryStage;
    private Scene newMovieScene;
    private Scene editMovieScene;

    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    FileManagement fm;
    AdminController adminC;
    CategoryController categoryC;

    public MovieScreen(Stage primaryStage, AdminController adminC) {
        this.primaryStage = primaryStage;
        //
        this.adminC = adminC;
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> categoryBox.getItems().add(category.getName()));
    }

    public Scene newMovieScene() {
        BorderPane root = new BorderPane();
        GridPane gridPane = new GridPane();

        nameField = new TextField();
        directorField = new TextField();
        descriptionField = new TextField();
        durationField = new TextField();

        nameField.setPrefWidth(300);
        directorField.setPrefWidth(300);
        descriptionField.setPrefWidth(300);
        durationField.setPrefWidth(300);

        categoryBox.setMaxSize(300, 20);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);

        GridPane.setConstraints(nameField, 1, 0);
        GridPane.setConstraints(directorField, 1, 1);
        GridPane.setConstraints(descriptionField, 1, 2);
        GridPane.setConstraints(durationField, 1, 3);
        GridPane.setConstraints(categoryBox, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, nameField, labelDirector, directorField, labelDescription,
                descriptionField, labelDuration, durationField, labelCategory, categoryBox);

        // Save buttton
        acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 7);
        acceptButton.setTranslateY(60);
        acceptButton.setText("Save");
        acceptButton.setPrefWidth(150);
        acceptButton.setOnAction(event -> addNewMovie());
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);

        // Cancel buttton
        cancelButton = new Button();

        GridPane.setConstraints(cancelButton, 1, 7);
        cancelButton.setTranslateY(60);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);
        cancelButton.setOnAction(event -> returnScene());

        // File Video buttton
        fileButton = new Button();
        fileButton.setPrefWidth(50);
        fileButton.setOnAction(event -> chooseFileScreen());

        // Cover image button
        coverButton = new Button();
        coverButton.setPrefWidth(50);
        coverButton.setOnAction(event -> chooseImageScreen());

        ImageView fileIcon = new ImageView(new Image("file:" + "src/prograIconos/video.png"));
        fileIcon.setFitWidth(22);
        fileIcon.setFitHeight(22);
        fileButton.setGraphic(fileIcon);

        ImageView coverIcon = new ImageView(new Image("file:" + "src/prograIconos/cover.png"));
        coverIcon.setFitWidth(22);
        coverIcon.setFitHeight(22);
        coverButton.setGraphic(coverIcon);

        //
        HBox fileHBox = new HBox(labelFileName, fileButton);
        HBox coverHBox = new HBox(labelImageCover, coverButton);
        fileHBox.setSpacing(25);
        coverHBox.setSpacing(25);
        GridPane.setConstraints(fileHBox, 0, 5);
        GridPane.setConstraints(coverHBox, 1, 5);

        coverHBox.setTranslateX(130);

        //
        gridPane.getChildren().addAll(acceptButton, cancelButton, fileHBox, coverHBox);

        VBox container = new VBox(gridPane, labelWarning);
        container.setAlignment(Pos.CENTER_LEFT);
        root.setCenter(container);
        labelWarning.setTranslateY(-50);
        labelWarning.setTranslateX(75);

        container.setMaxWidth(600);
        container.setMaxHeight(600);
        //
        newMovieScene = new Scene(root, screenWidth, screenHeight);
        // CSS
        newMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("acceptbutton");
        fileButton.setId("videoButton");
        coverButton.setId("videoButton");
        labelWarning.setId("warning");
        root.setId("root3");
        gridPane.setId("gridPane");
        container.setId("gridPane");

        return newMovieScene;
    }

    private void returnScene() {
        EntryWindow main = new EntryWindow();
        main.getScene1();
    }

    public void addNewMovie() {
        if (validation()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm changes");
            alert.setHeaderText(null);
            alert.setContentText("You want to save to changes?");

            // cambio de texto predet. en Alert
            Button buttonOK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Button buttonCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            buttonOK.setText("Accept");
            buttonCancel.setText("Return");
            //
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    adminC.addMovie(nameField.getText(), directorField.getText(), descriptionField.getText(),
                            Integer.parseInt(durationField.getText()),
                            categoryBox.getValue(), selectedFile.getName(), selectedCover.getName());
                    returnScene();
                } else {
                }
            });
        }
    }

    public Boolean validation() {
        if (nameField.getText().isBlank() && directorField.getText().isBlank() && descriptionField.getText().isBlank()
                && durationField.getText().isBlank()
                && (categoryBox.getValue() == null) && (selectedFile == null) && (selectedCover == null)) {

            adminC.showErrorTimeline(nameField, labelWarning, "* All fields must be filled!");
            adminC.showErrorTimeline(directorField, labelWarning, "* All fields must be filled!");
            adminC.showErrorTimeline(descriptionField, labelWarning, "* All fields must be filled!");
            adminC.showErrorTimeline(durationField, labelWarning, "* All fields must be filled!");
            adminC.showErrorTimelineChoiceBox(categoryBox, labelWarning, "* All fields must be filled!");
            adminC.showErrorTimelineFile(fileButton, labelWarning, "* All fields must be filled!");
            adminC.showErrorTimelineFile(coverButton, labelWarning, "* All fields must be filled!");
            return false;

        } else if (nameField.getText().isBlank() || !adminC.validateName(nameField.getText())
                || !adminC.validateWithNumber(nameField.getText())) {
            adminC.showErrorTimeline(nameField, labelWarning, "* Invalid name. Min. 3 digits, no special characters.");
            return false;
        } else if (directorField.getText().isBlank() || !adminC.validateName(directorField.getText())
                || !adminC.validarSinCharacterSpecial(directorField.getText())) {
            adminC.showErrorTimeline(directorField, labelWarning,
                    "* Invalid director. Min. 3 digits, no numbers or special characters.");
            return false;
        } else if (descriptionField.getText().isBlank() || !adminC.validateName(descriptionField.getText())
                || !adminC.validateWithoutSpecialCharacter(descriptionField.getText())) {
            adminC.showErrorTimeline(descriptionField, labelWarning,
                    "* Invalid description. Min. 3 digits, no numbers or special characters.");
            return false;
        } else if (durationField.getText().isBlank() || !adminC.validateNumbers(durationField.getText())) {
            adminC.showErrorTimeline(durationField, labelWarning, "* Invalid duration. No characters.");
            return false;
        } else if (categoryBox.getValue() == null) {
            adminC.showErrorTimelineChoiceBox(categoryBox, labelWarning, "* Select the category.");
            return false;
        } else if (selectedFile == null) {
            adminC.showErrorTimelineFile(fileButton, labelWarning, "* Select the file video.");
            return false;
        } else if (selectedCover == null) {
            adminC.showErrorTimelineFile(coverButton, labelWarning, "* Select the cover image.");
            return false;
        }
        return true;
    }
    // EDIT

    public Scene editMovieScene(Movie movie) {

        BorderPane root = new BorderPane();
        GridPane gridPane = new GridPane();
        //
        nameField = new TextField();
        nameField.setPromptText("Input the name of the movie");
        nameField.setText(movie.getName());

        directorField = new TextField();
        directorField.setPromptText("Input the director");
        directorField.setText(movie.getAuthor());

        descriptionField = new TextField();
        descriptionField.setPromptText("Input the description");
        descriptionField.setText(movie.getDescription());

        durationField = new TextField();
        durationField.setPromptText("Input the duration");
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
        GridPane.setConstraints(categoryBox, 1, 4);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);

        categoryBox.setValue(movie.getCategory());
        categoryBox.setMaxSize(300, 20);

        gridPane.getChildren().setAll(nameField, directorField, descriptionField, durationField, labelName,
                labelDirector, labelDescription, labelDuration, labelCategory, categoryBox);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        // Save button
        acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 7);
        acceptButton.setTranslateY(60);
        acceptButton.setText("Save");
        acceptButton.setPrefWidth(150);

        acceptButton.setOnAction(event -> editMovie(movie));
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);

        // Cancel buttton
        cancelButton = new Button();

        GridPane.setConstraints(cancelButton, 1, 7);
        cancelButton.setTranslateY(60);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);
        cancelButton.setOnAction(event -> returnScene());

        // File Video buttton
        fileButton = new Button();
        fileButton.setPrefWidth(50);
        fileButton.setOnAction(event -> chooseFileScreen());

        // Cover image button
        coverButton = new Button();
        coverButton.setPrefWidth(50);
        coverButton.setOnAction(event -> chooseImageScreen());

        ImageView fileIcon = new ImageView(new Image("file:" + "src/prograIconos/video.png"));
        fileIcon.setFitWidth(22);
        fileIcon.setFitHeight(22);
        fileButton.setGraphic(fileIcon);

        ImageView coverIcon = new ImageView(new Image("file:" + "src/prograIconos/cover.png"));
        coverIcon.setFitWidth(22);
        coverIcon.setFitHeight(22);
        coverButton.setGraphic(coverIcon);

        HBox fileHBox = new HBox(labelFileName, fileButton);
        HBox coverHBox = new HBox(labelImageCover, coverButton);
        fileHBox.setSpacing(25);
        coverHBox.setSpacing(25);
        GridPane.setConstraints(fileHBox, 0, 5);
        GridPane.setConstraints(coverHBox, 1, 5);
        coverHBox.setTranslateX(130);
        //
        selectedFile = new File("src/multimediaVideos/Movies/" + movie.getFileVideo());
        selectedCover = new File("src/multimediaCovers/Movies/" + movie.getCoverImage());
        //
        gridPane.getChildren().addAll(acceptButton, cancelButton, fileHBox, coverHBox);

        VBox container = new VBox(gridPane, labelWarning);
        container.setAlignment(Pos.CENTER_LEFT);
        root.setCenter(container);
        labelWarning.setTranslateY(-50);
        labelWarning.setTranslateX(75);

        container.setMaxWidth(600);
        container.setMaxHeight(600);

        //
        editMovieScene = new Scene(root, screenWidth, screenHeight);
        // CSS
        editMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("acceptbutton");
        fileButton.setId("videoButton");
        coverButton.setId("videoButton");
        labelWarning.setId("warning");
        root.setId("root3");
        gridPane.setId("gridPane");
        container.setId("gridPane");

        return editMovieScene;
    }

    public void editMovie(Movie movie) {
        if (validation()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm changes");
            alert.setHeaderText(null);
            alert.setContentText("You want to save to changes?");

            // cambio de texto predet. en Alert
            Button buttonOK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Button buttonCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            buttonOK.setText("Accept");
            buttonCancel.setText("Return");
            //
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    movie.setName(nameField.getText());
                    movie.setDescription(descriptionField.getText());
                    movie.setAuthor(directorField.getText());
                    movie.setDuration(Integer.parseInt(durationField.getText()));
                    movie.setCategory(categoryBox.getValue());

                    if (selectedFile != null) {
                        movie.setFileVideo(selectedFile.getName());
                    }
                    if (selectedCover != null) {
                        movie.setCoverImage(selectedCover.getName());
                    }
                    adminC.updateMovieInformation(movie);
                    returnScene();
                }
            });
        }

    }

    public void chooseFileScreen() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"));

        fileChooser.setTitle("Select the file Video");
        File initialDirectory = new File("src/multimediaVideos/Movies");
        fileChooser.setInitialDirectory(initialDirectory);
        selectedFile = fileChooser.showOpenDialog(primaryStage);

    }

    public void chooseImageScreen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));

        fileChooser.setTitle("Select the cover image");
        File initialDirectory = new File("src/multimediaCovers/Movies");
        fileChooser.setInitialDirectory(initialDirectory);
        selectedCover = fileChooser.showOpenDialog(primaryStage);
    }
}
