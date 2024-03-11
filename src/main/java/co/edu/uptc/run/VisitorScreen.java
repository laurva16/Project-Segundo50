package co.edu.uptc.run;

import java.io.File;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.Serie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class VisitorScreen {

    private ScrollPane scrollPane;
    private BorderPane root;
    private double screenWidth, screenHeight;
    private AdminController adminController;
    private LogInWindow logInWindow;
    private Stage primaryStage;

    // First screen
    private ImageView image;
    private Label labelName;
    private Label labelDesciption;
    private Label labelDuration;
    private Label labelAuthor;
    private Label labelGenre;
    private FlowPane flowPane;
    private Scene scene;
    private String pathMovies;
    private String pathSeries;
    Button movieButton = new Button("Movie");
    Button serieButton = new Button("Serie");
    Button returnButton = new Button("Return");

    public VisitorScreen() {
        screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        scrollPane = new ScrollPane();
        adminController = new AdminController();
        labelName = new Label();
        flowPane = new FlowPane();
        logInWindow = new LogInWindow();
        primaryStage = LogInWindow.getPrimaryStage();
        pathMovies = "src\\multimediaCovers\\Movies\\";
        pathSeries = "src\\multimediaCovers\\Series\\";
    }

    public void principalScene() {

        flowPane.setStyle("-fx-background-color: #191919;");
        flowPane.setHgap(10);
        flowPane.setAlignment(Pos.CENTER_LEFT);
        flowPane.setMaxHeight(screenHeight);
        flowPane.setPadding(new Insets(0, 90, 0, 90));
        System.out.println((screenWidth));

        scrollPane = new ScrollPane(flowPane);
        scrollPane.setStyle("-fx-background-color: #191919;");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(scrollPane);
        scene = new Scene(root, screenWidth, screenHeight);

        scene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
    }

    public void moviesList() {
        for (Movie movies : adminController.getMovies()) {
            image = new ImageView(new Image("file:" + pathMovies + movies.getCoverImage()));
            image.setOnMouseClicked(event -> moviesScreen(movies));
            labelName = new Label(movies.getName());
            labelName.setId("labelName");
            VBox vBox = new VBox(labelName, image);

            image.setFitWidth(240);
            image.setFitHeight(120);
            labelName.setMaxWidth(180);
            FlowPane.setMargin(vBox, new Insets(20, 0, 20, 20));
            flowPane.getChildren().add(vBox);
        }
    }

    public void seriesList() {
        for (Serie serie : adminController.getListSeries()) {
            image = new ImageView(new Image("file:" + pathSeries + serie.getCoverImage()));
            image.setOnMouseClicked(event -> seriesScreen(serie));
            labelName = new Label(serie.getName());
            labelName.setId("labelName");
            VBox vBox = new VBox(labelName, image);

            image.setFitWidth(240);
            image.setFitHeight(120);
            labelName.setMaxWidth(180);
            FlowPane.setMargin(vBox, new Insets(20, 0, 20, 20));
            flowPane.getChildren().add(vBox);
        }
    }

    private ToolBar createMenuBar() {
        Region spacer = new Region();
        movieButton = new Button("Movie");
        serieButton = new Button("Serie");
        returnButton = new Button("Return");
        movieButton.setCursor(Cursor.HAND);
        serieButton.setCursor(Cursor.HAND);
        returnButton.setCursor(Cursor.HAND);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        ToolBar toolBar = new ToolBar(movieButton, serieButton, spacer, returnButton);
        toolBar.getStyleClass().add("menubar");
        movieButton.getStyleClass().add("menu");
        serieButton.getStyleClass().add("menu");
        returnButton.getStyleClass().add("menu");

        // Asignar acciones a los botones
        movieButton.setOnAction(event -> {
            flowPane.getChildren().clear();
            moviesList();
            principalScene();
        });
        serieButton.setOnAction(event -> {
            flowPane.getChildren().clear();
            seriesList();
            principalScene();
        });
        returnButton.setOnAction(event -> LogInWindow.getSceneLogIn());
        return toolBar;
    }

    // See movie
    public void moviesScreen(Movie movie) {
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();

        image = new ImageView(new Image("file:" + pathMovies + movie.getCoverImage()));
        labelName = new Label(movie.getName());
        labelDesciption = new Label(movie.getDescription());
        labelDuration = new Label(String.valueOf(movie.getDuration()));
        labelAuthor = new Label(movie.getAuthor());
        labelGenre = new Label(movie.getCategory());

        VBox vBox1 = new VBox(labelName, labelDesciption);
        VBox vBox2 = new VBox(labelDuration, labelAuthor, labelGenre);
        HBox hBox = new HBox(vBox1, vBox2);

        vBox = new VBox(image, hBox);

        image.setFitWidth(400);
        image.setFitHeight(200);
        root.getChildren().add(vBox);

        hBox.setStyle("-fx-background-color: #191919;");
        hBox.setMinHeight(screenHeight);
        hBox.setMinWidth(screenWidth);

        root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(vBox);

        scene = new Scene(root, screenWidth, screenHeight);

        scene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());

        primaryStage.setScene(scene);
        primaryStage.setTitle("See movie");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // See serie
    public void seriesScreen(Serie serie) {

    }

    public Scene getSceneMovie() {
        flowPane.getChildren().clear();
        moviesList();
        principalScene();
        return scene;
    }
}