package co.edu.uptc.run;

import java.io.File;
import java.util.Optional;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.PlayList;
import co.edu.uptc.model.Serie;
import co.edu.uptc.model.UserRegistered;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PlayListScreen {
    private static UserScreen userScreen;
    private static Stage primaryStage = LogInWindow.getPrimaryStage();
    private static Scene scene, scene2;
    private static UserRegisteredController userRegisteredController;
    private static UserRegistered userRegistered;
    static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    private static BorderPane root, root2;
    private static ScrollPane scrollPanePrincipal;
    private static VBox vBoxPrincipal;
    private static BorderPane borderPanePrincipalPlayList;
    private static Label namePlayList;
    private static HBox hBoxActions;
    private static Button buttonAddMovie, buttonAddSerie, buttonDeletePlayList;
    private static ImageView imageAddMovie, imageAddSerie;
    private static ImageView imageDeletePlayList;
    private static Button buttonAddPlayList;
    private static ImageView imageAddPlayList;
    private static StackPane stackPaneAddPlayList;
    private static Rectangle rectangle2;
    private static Tooltip tooltipAddPlayList, tooltipMovie, tooltipSerie, tooltipDelete, tooltipName;

    // _______________________________________________________________________________________________________________________
    private static Rectangle rectangle2Content;
    private static StackPane stackPaneContent;
    private static Button buttonReturn;
    private static VBox vBoxContent, vBoxMovies, vBoxSeries, vBoxCurrentMovie, vBoxCurrentSerie;
    private static HBox hBoxMultimedia;
    private static ScrollPane scrollPaneContent;
    private static ImageView imageMovie, imageSerie;
    private static String pathMovie, pathSerie;
    private static HBox hBoxMovieCurrent, hBoxSerieCurrent;
    private static ImageView delete;
    private static ImageView play;
    static DisplayMultimediaScreen displayScreen;
    private static PlayList playList;

    public PlayListScreen() {
        userRegistered = new UserRegistered();
        userRegisteredController = getUserRegisteredController();
        root = new BorderPane();
        borderPanePrincipalPlayList = new BorderPane();
        imageAddPlayList = new ImageView();
        imageAddMovie = new ImageView();
        imageDeletePlayList = new ImageView();
        stackPaneAddPlayList = new StackPane();
        playList = new PlayList();
    }

    public static void setUserRegistered(UserRegistered userRegistered) {
        PlayListScreen.userRegistered = userRegistered;
        userRegisteredController = new UserRegisteredController();
        userRegisteredController.setCurrentUser(userRegistered);
    }

    public static UserRegistered getUserRegistered() {
        return userRegistered;
    }

    public static UserRegisteredController getUserRegisteredController() {
        return userRegisteredController;
    }

    public static void showPlayListScene() {
        userScreen = new UserScreen();
        userScreen.getMenuBar().getStylesheets()
                .add(new File("src\\main\\java\\co\\styles\\playList.css").toURI().toString());
        root = new BorderPane();

        Rectangle rectangle = new Rectangle(200, screenHeight, Color.valueOf("#191919"));
        getPlayList();
        addNewPlayList();
        principal();
        messages();
        switchToUserScreen();

        root.setTop(userScreen.getMenuBar());
        root.setLeft(stackPaneAddPlayList);
        root.setRight(rectangle);
        root.setCenter(scrollPanePrincipal);

        scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\playList.css").toURI().toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("PlayList");
        primaryStage.setMaximized(true);
        userScreen.setUserRegistered(userRegistered);
    }

    public static void principal() {
        namePlayList = new Label();
        scrollPanePrincipal = new ScrollPane();
        vBoxPrincipal = new VBox();
        buttonAddMovie = new Button();
        buttonDeletePlayList = new Button();
        hBoxActions = new HBox();

        scrollPanePrincipal.setContent(vBoxPrincipal);
        scrollPanePrincipal.setStyle("-fx-background-color: #5c5c5c;");
        BorderPane.setMargin(scrollPanePrincipal, new Insets(0, 0, 80, 0));
        scrollPanePrincipal.setMaxSize(screenWidth, screenHeight);

        vBoxPrincipal.setPrefSize(screenWidth - 190, screenHeight);
        vBoxPrincipal.setStyle("-fx-background-color: #5c5c5c;");

        for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
            namePlayList = new Label(playList.getName());
            buttonsActions();
            BorderPane borderPanePrincipalPlayList = new BorderPane();
            hBoxActions.setSpacing(50);
            borderPanePrincipalPlayList.setRight(hBoxActions);
            borderPanePrincipalPlayList.setLeft(namePlayList);

            borderPanePrincipalPlayList.getStyleClass().add("hBoxPrincipal");
            borderPanePrincipalPlayList.setMinHeight(100);
            borderPanePrincipalPlayList.setMaxWidth(700);
            borderPanePrincipalPlayList.setCursor(Cursor.HAND);

            vBoxPrincipal.getChildren().add(borderPanePrincipalPlayList);
            vBoxPrincipal.setAlignment(Pos.CENTER);
            vBoxPrincipal.setMaxSize(screenWidth - 400, screenHeight);

            hBoxActions.setAlignment(Pos.CENTER_RIGHT);
            BorderPane.setAlignment(namePlayList, Pos.CENTER_LEFT);
            namePlayList.setMaxWidth(290);
            BorderPane.setMargin(hBoxActions, new Insets(0, 50, 0, 10));
            BorderPane.setMargin(namePlayList, new Insets(0, 10, 0, 50));
            VBox.setMargin(borderPanePrincipalPlayList, new Insets(5, 0, 5, 0));

            tooltipName = new Tooltip(namePlayList.getText());
            Tooltip.install(namePlayList, tooltipName);

            deletePlayList(namePlayList.getText());
            setPlayList(playList);
            changeToScene2(borderPanePrincipalPlayList, playList);
        }
    }

    public static void addNewPlayList() {
        rectangle2 = new Rectangle(200, screenHeight, Color.valueOf("#191919"));
        buttonAddPlayList = new Button();
        StackPane.setAlignment(buttonAddPlayList, Pos.TOP_CENTER);
        StackPane.setMargin(buttonAddPlayList, new Insets(20, 0, 0, 0));

        imageAddPlayList = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        imageAddPlayList.setFitHeight(50);
        imageAddPlayList.setFitWidth(50);

        buttonAddPlayList.setGraphic(imageAddPlayList);
        buttonAddPlayList.setStyle("-fx-background-color: transparent;");
        BorderPane.setMargin(buttonAddPlayList,
                new Insets(20, imageAddPlayList.getFitWidth(), 0, imageAddPlayList.getFitWidth()));
        stackPaneAddPlayList = new StackPane(rectangle2, buttonAddPlayList);

        buttonAddPlayList.setOnAction(event -> addPlayList());
        buttonAddPlayList.setCursor(Cursor.HAND);

        tooltipAddPlayList = new Tooltip("Add new playList");
        Tooltip.install(buttonAddPlayList, tooltipAddPlayList);
    }

    public static void addPlayList() {
        TextInputDialog addPlayList = new TextInputDialog();
        addPlayList.setTitle("Add PlayList");
        addPlayList.setHeaderText("Enter the name of the new PlayList");
        addPlayList.setContentText("Name: ");

        Optional<String> name = addPlayList.showAndWait();
        name.ifPresent(namePlayList -> {
            boolean add = true;

            if (namePlayList.length() > 24) {
                Alert error = new Alert(AlertType.INFORMATION);
                error.setTitle("Error");
                error.setHeaderText("Name too long");
                error.setContentText(("Enter a name with a maximum of 25 characters"));
                error.show();
            } else {
                for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
                    add = !playList.getName().equals(namePlayList);
                }
                if (add) {
                    userRegisteredController.addPlayList(namePlayList);
                    Alert confirmation = new Alert(AlertType.INFORMATION);
                    confirmation.setTitle("Confirmation");
                    confirmation.setHeaderText("PLayList '" + namePlayList + "' added correctly");
                    confirmation.show();
                    showPlayListScene();
                } else {
                    Alert error = new Alert(AlertType.INFORMATION);
                    error.setTitle("Error");
                    error.setHeaderText("Existing PlayList");
                    error.setContentText(("Please enter another name"));
                    error.show();
                }
            }
        });
    }

    public static void buttonsActions() {
        Region spacer = new Region();
        spacer.setPrefWidth(100);

        buttonAddMovie = new Button();
        imageAddMovie = new ImageView(new Image("file:" + "src\\prograIconos\\agregarVerde.png"));
        imageAddMovie.setFitHeight(30);
        imageAddMovie.setFitWidth(30);
        buttonAddMovie.setGraphic(imageAddMovie);
        buttonAddMovie.setStyle("-fx-background-color: transparent;");

        buttonAddSerie = new Button();
        imageAddSerie = new ImageView(new Image("file:" + "src\\prograIconos\\anadirMorado.png"));
        imageAddSerie.setFitHeight(30);
        imageAddSerie.setFitWidth(30);
        buttonAddSerie.setGraphic(imageAddSerie);
        buttonAddSerie.setStyle("-fx-background-color: transparent;");

        buttonDeletePlayList = new Button();
        imageDeletePlayList = new ImageView(new Image("file:" + "src\\prograIconos\\borrarRojo.png"));
        imageDeletePlayList.setFitHeight(30);
        imageDeletePlayList.setFitWidth(30);
        buttonDeletePlayList.setGraphic(imageDeletePlayList);
        buttonDeletePlayList.setStyle("-fx-background-color: transparent;");

        hBoxActions = new HBox(spacer, buttonAddMovie, buttonAddSerie, buttonDeletePlayList);
        buttonAddMovie.setCursor(Cursor.HAND);
        buttonAddSerie.setCursor(Cursor.HAND);
        buttonDeletePlayList.setCursor(Cursor.HAND);

        tooltipMovie = new Tooltip("Add new movie");
        Tooltip.install(buttonAddMovie, tooltipMovie);
        tooltipSerie = new Tooltip("Add new serie");
        Tooltip.install(buttonAddSerie, tooltipSerie);
        tooltipDelete = new Tooltip("Delete playList");
        Tooltip.install(buttonDeletePlayList, tooltipDelete);

        switchToUserScreen();
    }

    public static void deletePlayList(String playListName) {
        buttonDeletePlayList.setOnAction(event -> {
            Alert delete = new Alert(AlertType.CONFIRMATION);
            delete.setTitle("Delete");
            delete.setHeaderText("Delete playList '" + playListName + "'?");
            delete.setContentText(("Are you sure you want to delete the playList"));

            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            delete.getButtonTypes().setAll(buttonTypeOK, buttonCancel);

            delete.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    if (userRegisteredController.removeplayList(playListName)) {
                        Alert correct = new Alert(AlertType.INFORMATION);
                        correct.setTitle("Delete playList");
                        correct.setHeaderText("PLayList removed '" + playListName + "'");
                        correct.setContentText(("The playList has been successfully deleted"));
                        correct.show();
                        showPlayListScene();
                    } else {
                        Alert error = new Alert(AlertType.INFORMATION);
                        error.setTitle("Error");
                        error.setHeaderText("Some error ocurred");
                        error.setContentText(("Please try again"));
                        error.show();
                    }
                }
            });
        });
    }

    public static void messages() {

        // Add new PlayList
        buttonAddPlayList.setOnMouseEntered(
                event -> tooltipAddPlayList.show(buttonAddPlayList, event.getScreenX(), event.getScreenY() + 10));
        buttonAddPlayList.setOnMouseExited(event -> tooltipAddPlayList.hide());

        // Add movie
        buttonAddMovie.setOnMouseEntered(
                event -> tooltipMovie.show(buttonAddMovie, event.getScreenX(), event.getScreenY() + 10));
        buttonAddMovie.setOnMouseExited(event -> tooltipMovie.hide());

        // Add serie
        buttonAddSerie.setOnMouseEntered(
                event -> tooltipSerie.show(buttonAddSerie, event.getScreenX(), event.getScreenY() + 10));
        buttonAddSerie.setOnMouseExited(event -> tooltipSerie.hide());

        // Delete playList
        buttonDeletePlayList.setOnMouseEntered(
                event -> tooltipDelete.show(buttonDeletePlayList, event.getScreenX(), event.getScreenY() + 10));
        buttonDeletePlayList.setOnMouseExited(event -> tooltipDelete.hide());

        // PlayList Name
        namePlayList.setOnMouseEntered(
                event -> tooltipName.show(namePlayList, event.getScreenX(), event.getScreenY() + 10));
        namePlayList.setOnMouseExited(event -> tooltipName.hide());
    }

    public static void changeToScene2(BorderPane borderPane, PlayList playList) {
        setPlayList(playList);
        borderPane.setOnMouseClicked(event -> {
            setPlayList(playList);
            showPlayListScene2();
        });
    }

    public static void switchToUserScreen() {
        buttonAddMovie.setOnMouseClicked(event -> userScreen.getScene1());
        buttonAddSerie.setOnMouseClicked(event -> userScreen.getScene2());
    }

    // Second scene
    public static void showPlayListScene2() {
        getPlayList();
        changeToScene1(rectangle2);
        principalContent();

        userScreen = new UserScreen();
        root2 = new BorderPane();
        Rectangle rectangle = new Rectangle(150, screenHeight, Color.valueOf("black"));

        root2.setLeft(stackPaneContent);
        root2.setRight(rectangle);
        root2.setCenter(vBoxContent);

        scene2 = new Scene(root2, screenWidth, screenHeight);
        scene2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\playList.css").toURI().toString());
        primaryStage.setScene(scene2);
        primaryStage.setTitle("PlayListContent");
        primaryStage.setMaximized(true);
    }

    public static void runScene2() {

    }

    public static void changeToScene1(Rectangle rectangle2) {
        rectangle2Content = new Rectangle(150, screenHeight, Color.valueOf("black"));
        stackPaneContent = new StackPane();

        buttonReturn = new Button("Return");
        buttonReturn.setId("return");
        buttonReturn.setCursor(Cursor.HAND);
        StackPane.setAlignment(buttonReturn, Pos.TOP_LEFT);
        StackPane.setMargin(buttonReturn, new Insets(20, 0, 0, 20));
        buttonReturn.setOnMouseClicked(event -> showPlayListScene());

        stackPaneContent.getChildren().addAll(rectangle2Content, buttonReturn);
    }

    public static void principalContent() {
        Label playListNameContent = new Label(playList.getName());
        vBoxContent = new VBox();
        vBoxMovies = new VBox();
        vBoxSeries = new VBox();
        hBoxMultimedia = new HBox();
        scrollPaneContent = new ScrollPane();
        scrollPaneContent.setStyle("-fx-background-color: #191919;");

        playListNameContent.getStyleClass().add("playListNameContent");
        vBoxContent.setAlignment(Pos.TOP_CENTER);
        scrollPaneContent.setMinHeight(screenHeight - 150);
        VBox.setMargin(playListNameContent, new Insets(20, 0, 20, 0));

        vBoxContent.setId("vBoxContent");
        vBoxContent.getChildren().addAll(playListNameContent, scrollPaneContent);

        playListContent();
    }

    public static void playListContent() {
        Label labelMovie = new Label("Movies"), labelSerie = new Label("Series"), labelName;
        labelMovie.getStyleClass().add("labelMultimedia");
        labelSerie.getStyleClass().add("labelMultimedia");
        pathMovie = "src\\multimediaCovers\\Movies\\";
        pathSerie = "src\\multimediaCovers\\Series\\";
        vBoxMovies.getChildren().add(labelMovie);
        vBoxSeries.getChildren().add(labelSerie);

        labelMovie.getStyleClass().add("nameMultimedia");
        labelSerie.getStyleClass().add("nameMultimedia");

        scrollPaneContent.setMaxSize(screenWidth - 300, screenHeight);
        vBoxSeries.setAlignment(Pos.TOP_CENTER);
        vBoxMovies.setAlignment(Pos.TOP_CENTER);
        vBoxMovies.setMaxWidth(scrollPaneContent.getMaxWidth() / 2);
        vBoxSeries.setMaxWidth(scrollPaneContent.getMaxWidth() / 2);
        vBoxMovies.setMinWidth(scrollPaneContent.getMaxWidth() / 2);
        vBoxSeries.setMinWidth(scrollPaneContent.getMaxWidth() / 2);
        vBoxMovies.setMinHeight(screenHeight - 150);
        vBoxSeries.setMinHeight(screenHeight - 150);
        vBoxMovies.setStyle("-fx-background-color: #191919;");
        vBoxSeries.setStyle("-fx-background-color: #191919;");
        vBoxMovies.setSpacing(20);
        vBoxSeries.setSpacing(20);

        for (Movie movie : playList.getMovies()) {
            vBoxCurrentMovie = new VBox();
            vBoxCurrentMovie.setAlignment(Pos.CENTER);

            labelName = new Label(movie.getName());
            labelName.getStyleClass().add("labelMultimedia2");

            imageMovie = new ImageView(new Image("file:" + pathMovie + movie.getCoverImage()));
            imageMovie.setFitWidth((vBoxMovies.getMaxWidth() / 2) - 5);
            imageMovie.setFitHeight(imageMovie.getFitWidth() * 0.50);

            hBoxCurrentMovie();
            playMultimediaMovie(movie.getFileVideo(), playList);
            deleteMovie(playList.getName(), movie);

            vBoxCurrentMovie.getChildren().addAll(labelName, imageMovie, hBoxMovieCurrent);
            vBoxMovies.getChildren().add(vBoxCurrentMovie);
        }

        for (Serie serie : playList.getSeries()) {
            vBoxCurrentSerie = new VBox();
            vBoxCurrentSerie.setAlignment(Pos.CENTER);

            labelName = new Label(serie.getName());
            labelName.getStyleClass().add("labelMultimedia2");

            imageSerie = new ImageView(new Image("file:" + pathSerie + serie.getCoverImage()));
            imageSerie.setFitWidth((vBoxSeries.getMaxWidth() / 2) - 5);
            imageSerie.setFitHeight(imageSerie.getFitWidth() * 0.50);

            hBoxCurrentSerie();
            playMultimediaMovie(serie.getFileVideo(), playList);
            deleteSerie(playList.getName(), serie);

            vBoxCurrentSerie.getChildren().addAll(labelName, imageSerie, hBoxSerieCurrent);
            vBoxSeries.getChildren().add(vBoxCurrentSerie);
        }
        hBoxMultimedia.getChildren().addAll(vBoxMovies, vBoxSeries);
        scrollPaneContent.setContent(hBoxMultimedia);
    }

    public static void icons() {
        delete = new ImageView(new Image("file:" + "src\\prograIconos\\borrarRojo.png"));
        play = new ImageView(new Image("file:" + "src\\prograIconos\\play.png"));
        hBoxMovieCurrent = new HBox();
        hBoxSerieCurrent = new HBox();

        delete.setCursor(Cursor.HAND);
        play.setCursor(Cursor.HAND);

        delete.setFitHeight(30);
        delete.setFitWidth(30);
        play.setFitHeight(30);
        play.setFitWidth(30);
        HBox.setMargin(play, new Insets(5, 0, 5, 0));
    }

    public static void hBoxCurrentMovie() {
        icons();
        hBoxMovieCurrent.getStyleClass().add("hBoxCurrentMultimedia");
        hBoxMovieCurrent.setSpacing(100);
        hBoxMovieCurrent.setAlignment(Pos.CENTER);
        hBoxMovieCurrent.setPrefWidth(imageMovie.getFitWidth());
        hBoxMovieCurrent.getChildren().addAll(play, delete);
    }

    public static void hBoxCurrentSerie() {
        icons();
        hBoxSerieCurrent.getStyleClass().add("hBoxCurrentMultimedia");
        hBoxSerieCurrent.setSpacing(100);
        hBoxSerieCurrent.setAlignment(Pos.CENTER);
        hBoxSerieCurrent.setPrefWidth(imageMovie.getFitWidth());
        hBoxSerieCurrent.getChildren().addAll(play, delete);
    }

    public static void playMultimediaMovie(String nameFile, PlayList playList) {
        play.setOnMouseClicked(event -> {
            switchReproductionScene(nameFile, playList);
        });
    }

    public static void switchReproductionScene(String nameFile, PlayList playList) {
        displayScreen = new DisplayMultimediaScreen();
        primaryStage.setScene(displayScreen.multimediaScene(nameFile, true, "PlayListScreen"));
    }

    public static Scene getScene() {
        showPlayListScene2();
        return scene2;
    }

    public static void deleteMovie(String playListName, Movie movie) {
        delete.setOnMouseClicked(event -> {

            Alert delete = new Alert(AlertType.CONFIRMATION);
            delete.setTitle("Remove");
            delete.setHeaderText("Remove movie '" + movie.getName() + "'?");
            delete.setContentText(("Are you want to remove the movie from the playList"));

            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            delete.getButtonTypes().setAll(buttonTypeOK, buttonCancel);

            delete.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    if (userRegisteredController.removeMovie(playListName, movie.getName())) {
                        Alert correct = new Alert(AlertType.INFORMATION);
                        correct.setTitle("Remove movie");
                        correct.setHeaderText("Movie removed '" + movie.getName() + "'");
                        correct.setContentText(("The movie has been successfully deleted"));
                        correct.show();
                        for (PlayList playListUser : userRegisteredController.getCurrentUser().getplayList()) {
                            if (playListUser.getName().equals(playListName)) {
                                setPlayList(playListUser);
                            }
                        }
                        showPlayListScene2();
                    } else {
                        Alert error = new Alert(AlertType.INFORMATION);
                        error.setTitle("Error");
                        error.setHeaderText("Some error ocurred");
                        error.setContentText(("Please try again"));
                        error.show();
                    }
                }
            });
        });
    }

    public static void deleteSerie(String playListName, Serie serie) {
        delete.setOnMouseClicked(event -> {
            Alert delete = new Alert(AlertType.CONFIRMATION);
            delete.setTitle("Remove");
            delete.setHeaderText("Remove serie '" + serie.getName() + "'?");
            delete.setContentText(("Are you want to remove the serie from the playList"));

            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            delete.getButtonTypes().setAll(buttonTypeOK, buttonCancel);

            delete.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    if (userRegisteredController.removeSerie(playListName, serie.getName())) {
                        Alert correct = new Alert(AlertType.INFORMATION);
                        correct.setTitle("Remove serie");
                        correct.setHeaderText("Serie removed '" + serie.getName() + "'");
                        correct.setContentText(("The serie has been successfully deleted"));
                        correct.show();
                        for (PlayList playListUser : userRegisteredController.getCurrentUser().getplayList()) {
                            if (playListUser.getName().equals(playListName)) {
                                setPlayList(playListUser);
                            }
                        }
                        showPlayListScene2();
                    } else {
                        Alert error = new Alert(AlertType.INFORMATION);
                        error.setTitle("Error");
                        error.setHeaderText("Some error ocurred");
                        error.setContentText(("Please try again"));
                        error.show();
                    }
                }
            });
        });
    }

    public static void setPlayList(PlayList playList2) {
        playList = playList2;
    }

    public static PlayList getPlayList() {
        return playList;
    }

    public static BorderPane getBorderPanePrincipalPlayList() {
        return borderPanePrincipalPlayList;
    }

    public static void setBorderPanePrincipalPlayList(BorderPane borderPanePrincipalPlayList) {
        PlayListScreen.borderPanePrincipalPlayList = borderPanePrincipalPlayList;
    }
}
