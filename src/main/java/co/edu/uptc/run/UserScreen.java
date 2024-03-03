package co.edu.uptc.run;

import java.io.File;
import java.util.Optional;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.Serie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserScreen {
    private TableView<Movie> tablaMovie;
    private TableView<Serie> tablaSerie = new TableView<>();
    private Stage primaryStage;
    private LogInWindow logInWindow;
    private AdminController adminC;
    private CategoryController categoryC;
    private Scene scene1, scene2;
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    Label labelName = new Label("Movie name:");
    Label labelDirector = new Label("Director name:");
    Label labelDescription = new Label("Description:");
    Label labelDuration = new Label("Duration:");
    Label labelCategory = new Label("Category:");
    Label labelWarning;

    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    MovieScreen movieScreen;

    Button movieButton = new Button("Movie");
    Button serieButton = new Button("Serie");
    Button playListButton = new Button("PlayList");
    Button subscriptionButton = new Button("Subscription");
    Button returnButton = new Button("Log Out");

    public UserScreen() {
        logInWindow = new LogInWindow();
        primaryStage = LogInWindow.getPrimaryStage();
        adminC = new AdminController();
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> choiceBox.getItems().add(category.getName()));

    }

    public void showMovieScene() {
        BorderPane root = new BorderPane();
        ToolBar menuBar = createMenuBar();
        root.setTop(menuBar);
        movieButton.setStyle("-fx-text-fill: black;");

        tablaMovie = new TableView<>();

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        ObservableList<Movie> grupos = FXCollections.observableArrayList(adminC.getMovies());

        TableColumn<Movie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        nameColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(4));
        genreColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(4));
        descriptionColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(4));

        // Configurar estilo de las columnas
        nameColumn.setStyle("-fx-alignment: CENTER;");
        genreColumn.setStyle("-fx-alignment: CENTER;");
        descriptionColumn.setStyle("-fx-alignment: CENTER;");

        tablaMovie.getColumns().addAll(nameColumn, genreColumn, descriptionColumn);

        // Establecer ancho máximo para la tabla
        tablaMovie.setMaxWidth(600);

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tablaMovie.getColumns().add(accionesColumna);

        tablaMovie.setItems(grupos);
        StackPane stackPane = new StackPane(tablaMovie);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
        BorderPane.setMargin(stackPane, new Insets(35, 0, 60, 0));

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root.setCenter(stackPane);

        scene1 = new Scene(root, screenWidth, screenHeight);

        // Configurar la escena y mostrarla
        scene1.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(scene1);
        primaryStage.setTitle("JavaFX MenuBar with CSS");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    void switchEditMovieScene(Movie movie) {
        movieScreen = new MovieScreen(primaryStage, adminC);
        primaryStage.setScene(movieScreen.editMovieScene(movie));
    }

    public class BotonCelda extends TableCell<Movie, Void> {
        Button btnWatch = new Button();
        Button btnDetails = new Button();
        Button btnPlayList = new Button();

        public BotonCelda() {
            btnWatch.setCursor(Cursor.HAND);
            btnDetails.setCursor(Cursor.HAND);
            btnPlayList.setCursor(Cursor.HAND);

            // Configura los íconos para los botones
            ImageView iconoWatch = new ImageView(new Image("file:" + "src\\prograIconos\\play.png"));
            ImageView iconoDetails = new ImageView(new Image("file:" + "src\\prograIconos\\detalle.png"));
            ImageView iconoPlayList = new ImageView(new Image("file:" + "src\\prograIconos\\corazon.png"));

            iconoWatch.setFitWidth(16);
            iconoWatch.setFitHeight(16);

            iconoDetails.setFitWidth(16);
            iconoDetails.setFitHeight(16);

            iconoPlayList.setFitWidth(16);
            iconoPlayList.setFitHeight(16);

            btnWatch.setGraphic(iconoWatch);
            btnDetails.setGraphic(iconoDetails);
            btnPlayList.setGraphic(iconoPlayList);

            btnWatch.getStyleClass().add("seeButton");
            btnDetails.getStyleClass().add("boton-modificar");
            btnPlayList.getStyleClass().add("seeButton");

            btnWatch.setOnAction(event -> {
            });

            btnDetails.setOnAction(event -> {
                seeMovieScreen(getTableView().getItems().get(getIndex()));
            });

            btnPlayList.setOnAction(event -> {

            });

            // Configura el contenido de las celdas para mostrar los botones
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox botonesContainer = new HBox(btnWatch, btnDetails, btnPlayList);
                botonesContainer.setSpacing(5);
                setGraphic(botonesContainer);
            }
        }
    }

    private ToolBar createMenuBar() {
        Region spacer = new Region();
        movieButton = new Button("Movie");
        serieButton = new Button("Serie");
        playListButton = new Button("PlayList");
        subscriptionButton = new Button("Subscription");
        returnButton = new Button("Log Out");

        movieButton.setCursor(Cursor.HAND);
        serieButton.setCursor(Cursor.HAND);
        playListButton.setCursor(Cursor.HAND);
        subscriptionButton.setCursor(Cursor.HAND);
        returnButton.setCursor(Cursor.HAND);

        subscriptionButton.setMinWidth(110);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Asignar acciones a los botones
        movieButton.setOnAction(event -> {
            cambiarAEscena1();
        });

        serieButton.setOnAction(event -> {
            entryWindowSerie();
        });

        returnButton.setOnAction(event -> {
            LogInWindow.getSceneLogIn();
        });

        // Crear la barra de herramientas y agregar los botones
        ToolBar toolBar = new ToolBar(movieButton, serieButton, playListButton, subscriptionButton, spacer,
                returnButton);
        toolBar.getStyleClass().add("menubar");
        movieButton.getStyleClass().add("menu");
        serieButton.getStyleClass().add("menu");
        playListButton.getStyleClass().add("menu");
        subscriptionButton.getStyleClass().add("menu");
        returnButton.getStyleClass().add("menu");

        return toolBar;
    }

    private void entryWindowSerie() {
        if (scene2 == null) {
            BorderPane root2 = new BorderPane();
            ToolBar menuBar = createMenuBar();
            serieButton.setStyle("-fx-text-fill: black;");
            root2.setTop(menuBar);

            // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
            ObservableList<Serie> series = FXCollections.observableArrayList(adminC.getListSeries());

            TableColumn<Serie, String> nameColumn = new TableColumn<>("Name");
            TableColumn<Serie, String> categoryColumn = new TableColumn<>("Category");
            TableColumn<Serie, String> descriptionColumn = new TableColumn<>("Description");

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            nameColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(4));
            categoryColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(4));
            descriptionColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(4));

            // Configurar estilo de las columnas
            nameColumn.setStyle("-fx-alignment: CENTER;");
            categoryColumn.setStyle("-fx-alignment: CENTER;");
            descriptionColumn.setStyle("-fx-alignment: CENTER;");

            tablaSerie.getColumns().addAll(nameColumn, categoryColumn, descriptionColumn);

            // Establecer ancho máximo para la tabla
            tablaSerie.setMaxWidth(600);

            // Agregar columna de botones
            TableColumn<Serie, Void> accionesColumna = new TableColumn<>("Actions");
            accionesColumna.setCellFactory(param -> new BotonCeldaSerie());
            tablaSerie.getColumns().add(accionesColumna);

            tablaSerie.setItems(series);

            StackPane stackPane = new StackPane(tablaSerie);
            stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
            BorderPane.setMargin(stackPane, new Insets(35, 0, 60, 0));

            // Agregar el StackPane que contiene la tabla al centro del BorderPane
            root2.setCenter(stackPane);
            scene2 = new Scene(root2, screenWidth, screenHeight);

            // Configurar la escena y mostrarla
            scene2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        }
        primaryStage.setScene(scene2);
        primaryStage.setTitle("JavaFX series with CSS");
        primaryStage.setMaximized(true);
    }

    public class BotonCeldaSerie extends TableCell<Serie, Void> {
        private final Button btnWatch = new Button();
        private final Button btnDetails = new Button();
        private final Button btnPlayList = new Button();

        public BotonCeldaSerie() {
            // Configura los íconos para los botones
            ImageView iconoWatch = new ImageView(new Image("file:" + "src\\prograIconos\\play.png"));
            ImageView iconoDetails = new ImageView(new Image("file:" + "src\\prograIconos\\detalle.png"));
            ImageView iconoPlayList = new ImageView(new Image("file:" + "src\\prograIconos\\corazon.png"));

            iconoWatch.setFitWidth(16);
            iconoWatch.setFitHeight(16);

            iconoDetails.setFitWidth(16);
            iconoDetails.setFitHeight(16);

            iconoPlayList.setFitWidth(16);
            iconoPlayList.setFitHeight(16);

            btnWatch.setGraphic(iconoWatch);
            btnDetails.setGraphic(iconoDetails);
            btnPlayList.setGraphic(iconoPlayList);

            btnWatch.getStyleClass().add("seeButton");
            btnDetails.getStyleClass().add("boton-modificar");
            btnPlayList.getStyleClass().add("seeButton");

            btnWatch.setOnAction(event -> {
            });

            btnDetails.setOnAction(event -> {
            });

            btnPlayList.setOnAction(event -> {
            });

            // Configura el contenido de las celdas para mostrar los botones
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox botonesContainer = new HBox(btnWatch, btnDetails, btnPlayList);
                botonesContainer.setSpacing(5);
                setGraphic(botonesContainer);
            }
        }
    }

    void seeMovieScreen(Movie movie) {

        Stage secundaryStage = new Stage();
        secundaryStage.initModality(Modality.APPLICATION_MODAL);
        secundaryStage.setTitle("Movie Information");

        GridPane gridPane = new GridPane();
        gridPane.setId("root2");
        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);
        gridPane.setHgap(50);

        Label name = new Label(movie.getName());
        Label director = new Label(movie.getAuthor());
        Label description = new Label(movie.getDescription());
        Label duration = new Label(String.valueOf(movie.getDuration()));
        Label category = new Label(movie.getCategory());

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);

        GridPane.setConstraints(name, 1, 0);
        GridPane.setConstraints(director, 1, 1);
        GridPane.setConstraints(description, 1, 2);
        GridPane.setConstraints(duration, 1, 3);
        GridPane.setConstraints(category, 1, 4);

        Button closeButton = new Button();
        closeButton.setText("Close");
        closeButton.setPrefWidth(100);
        GridPane.setConstraints(closeButton, 1, 5);
        closeButton.setOnAction(event -> secundaryStage.close());
        closeButton.setId("button");
        gridPane.getChildren().setAll(labelName, labelDirector, labelDescription, labelDuration, labelCategory, name,
                director, description, duration, category, closeButton);

        // Configurar tamano description
        description.setMaxWidth(200);
        description.setWrapText(true);

        Scene seeMovieScene = new Scene(gridPane, 500, 550);
        seeMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        secundaryStage.setScene(seeMovieScene);
        secundaryStage.showAndWait();
    }

    public void cambiarAEscena1() {
        primaryStage.setScene(scene1);
    }

    public Scene getScene1() {
        showMovieScene();
        return scene1;
    }
}
