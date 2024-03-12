package co.edu.uptc.run;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class EntryWindow {
    private TableView<Movie> tablaMovie;
    private TableView<Serie> tablaSerie = new TableView<>();
    private static Stage primaryStage;
    private Stage secundaryStage;
    private LogInWindow logInWindow;
    private AdminController adminC;
    private CategoryController categoryC;
    private Scene scene1, scene2;
    private Serie serieReturn;
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private boolean tableInitialized = false;

    Label labelName = new Label("Movie name:");
    Label labelDirector = new Label("Director name:");
    Label labelDescription = new Label("Description:");
    Label labelDuration = new Label("Duration:");
    Label labelCategory = new Label("Category:");
    Label labelFileVideo = new Label("File Video:");
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    MovieScreen movieScreen;
    CreateSerie createSerie;
    static DisplayMultimediaScreen displayScreen;
    ModifySerie modifySerie;

    Button movieButton = new Button("Movie");
    Button serieButton = new Button("Serie");
    Button returnButton = new Button("Log Out");

    public EntryWindow() {
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
        serieButton.setStyle("-fx-text-fill: white;");

        tablaMovie = new TableView<>();

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        ObservableList<Movie> grupos = FXCollections.observableArrayList(adminC.getMovies());

        TableColumn<Movie, String> IdColumn = new TableColumn<>("Id");
        TableColumn<Movie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> directorColumn = new TableColumn<>("Director");

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        IdColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(3.6));
        nameColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(3.6));
        directorColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(3.6));

        // Configurar estilo de las columnas
        IdColumn.setStyle("-fx-alignment: CENTER;");
        nameColumn.setStyle("-fx-alignment: CENTER;");
        directorColumn.setStyle("-fx-alignment: CENTER;");

        tablaMovie.getColumns().addAll(IdColumn, nameColumn, directorColumn);

        // Establecer ancho máximo para la tabla
        tablaMovie.setMaxWidth(900);

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tablaMovie.getColumns().add(accionesColumna);

        tablaMovie.setItems(grupos);
        StackPane stackPane = new StackPane(tablaMovie);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
        StackPane.setMargin(tablaMovie, new Insets(30)); // Agregar margen a la tabla

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root.setCenter(stackPane);

        // Crear un botón flotante
        ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        iconoAgregar.setFitWidth(32);
        iconoAgregar.setFitHeight(32);
        Button addNewButton = new Button();
        addNewButton.setTranslateY(-30);
        addNewButton.getStyleClass().add("boton-flotante");
        addNewButton.setGraphic(iconoAgregar);

        // Agregar el botón flotante en la esquina inferior derecha
        BorderPane.setAlignment(addNewButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(addNewButton, new Insets(15));
        root.setBottom(addNewButton);

        scene1 = new Scene(root, screenWidth, screenHeight);

        // Configurar la escena y mostrarla
        scene1.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(scene1);
        primaryStage.setTitle("Movies Admin");
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Add new Movie scene
        addNewButton.setOnAction(event -> switchNewMovieScene());

    }

    void switchNewMovieScene() {
        movieScreen = new MovieScreen(primaryStage, adminC);
        primaryStage.setScene(movieScreen.newMovieScene());
    }

    void switchNewSerieScene() {
        createSerie = new CreateSerie(primaryStage, adminC);
        primaryStage.setScene(createSerie.newSerieScene());
    }

    void switchEditSerieScene(Serie serie) {
        modifySerie = new ModifySerie(primaryStage, adminC);
        primaryStage.setScene(modifySerie.newSerieScene(serie));
    }

    void switchEditMovieScene(Movie movie) {
        movieScreen = new MovieScreen(primaryStage, adminC);
        primaryStage.setScene(movieScreen.editMovieScene(movie));
    }

    public class BotonCelda extends TableCell<Movie, Void> {
        private final Button btnEliminar = new Button();
        private final Button btnModificar = new Button();
        private final Button seeButton = new Button();

        public BotonCelda() {
            // Configura los íconos para los botones

            ImageView iconoEliminar = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
            ImageView iconoModificar = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));
            ImageView iconover = new ImageView(new Image("file:" + "src\\prograIconos\\ver.png"));

            iconoEliminar.setFitWidth(16);
            iconoEliminar.setFitHeight(16);

            iconoModificar.setFitWidth(16);
            iconoModificar.setFitHeight(16);

            iconover.setFitWidth(16);
            iconover.setFitHeight(16);

            btnEliminar.setGraphic(iconoEliminar);
            btnModificar.setGraphic(iconoModificar);
            seeButton.setGraphic(iconover);

            btnEliminar.getStyleClass().add("boton-eliminar");
            btnModificar.getStyleClass().add("boton-modificar");
            seeButton.getStyleClass().add("seeButton");

            btnEliminar.setOnAction(event -> {
                Movie grupo = getTableView().getItems().get(getIndex());
                // Mostrar una ventana emergente de confirmación
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this movie?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    adminC.deleteMovie(grupo.getId());

                    tablaMovie.getItems().remove(grupo);
                }
            });

            seeButton.setOnAction(event -> {
                seeMovieScreen(getTableView().getItems().get(getIndex()));
            });

            btnModificar.setOnAction(event -> {
                switchEditMovieScene(getTableView().getItems().get(getIndex()));
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
                HBox botonesContainer = new HBox(seeButton, btnEliminar, btnModificar);
                botonesContainer.setSpacing(5);
                setGraphic(botonesContainer);
            }
        }
    }

    private ToolBar createMenuBar() {
        Region spacer = new Region();
        movieButton.setCursor(Cursor.HAND);
        serieButton.setCursor(Cursor.HAND);
        returnButton.setCursor(Cursor.HAND);

        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Asignar acciones a los botones
        movieButton.setOnAction(event -> {
            // Aquí va la lógica para mostrar la ventana de películas
            showMovieScene();
        });

        serieButton.setOnAction(event -> {
            entryWindowSerie();
        });

        returnButton.setOnAction(event -> {
            LogInWindow.getSceneLogIn();
        });

        // Crear la barra de herramientas y agregar los botones
        ToolBar toolBar = new ToolBar(movieButton, serieButton, spacer, returnButton);
        toolBar.getStyleClass().add("menubar");
        movieButton.getStyleClass().add("menu");
        serieButton.getStyleClass().add("menu");
        returnButton.getStyleClass().add("menu");

        return toolBar;
    }

    public void entryWindowSerie() {

        BorderPane root2 = new BorderPane();
        ToolBar menuBar = createMenuBar();
        root2.setTop(menuBar);
        serieButton.setStyle("-fx-text-fill: black;");
        movieButton.setStyle("-fx-text-fill: white;");

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        ObservableList<Serie> series = FXCollections.observableArrayList(adminC.getListSeries());

        TableColumn<Serie, String> IdColumn = new TableColumn<>("Id");
        TableColumn<Serie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Serie, String> directorColumn = new TableColumn<>("Director");

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        IdColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(3.6));
        nameColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(3.6));
        directorColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(3.6));

        // Configurar estilo de las columnas
        IdColumn.setStyle("-fx-alignment: CENTER;");
        nameColumn.setStyle("-fx-alignment: CENTER;");
        directorColumn.setStyle("-fx-alignment: CENTER;");

        tablaSerie.getColumns().addAll(IdColumn, nameColumn, directorColumn);

        // Establecer ancho máximo para la tabla
        tablaSerie.setMaxWidth(900);

        // Agregar columna de botones
        TableColumn<Serie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCeldaSerie());
        tablaSerie.getColumns().add(accionesColumna);

        tablaSerie.setItems(series);

        StackPane stackPane = new StackPane(tablaSerie);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
        StackPane.setMargin(tablaSerie, new Insets(30)); // Agregar margen a la tabla

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root2.setCenter(stackPane);

        // Crear un botón flotante
        ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        iconoAgregar.setFitWidth(32);
        iconoAgregar.setFitHeight(32);
        Button addNewButton = new Button();
        addNewButton.setTranslateY(-20);
        addNewButton.getStyleClass().add("boton-flotante");
        addNewButton.setGraphic(iconoAgregar);

        // Agregar el botón flotante en la esquina inferior derecha
        BorderPane.setAlignment(addNewButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(addNewButton, new Insets(15));
        root2.setBottom(addNewButton);

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root2.setCenter(stackPane);

        scene2 = new Scene(root2, screenWidth, screenHeight);

        // Configurar la escena y mostrarla
        scene2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());

        primaryStage.setScene(scene2);
        primaryStage.setTitle("Series admin");
        primaryStage.setMaximized(true);
        // Add new Movie scene

        addNewButton.setOnAction(event -> switchNewSerieScene());

    }

    public class BotonCeldaSerie extends TableCell<Serie, Void> {
        private final Button btnEliminar = new Button();
        private final Button btnModificar = new Button();
        private final Button btnVer = new Button();

        public BotonCeldaSerie() {
            // Configura los íconos para los botones

            ImageView iconoEliminar = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
            ImageView iconoModificar = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));
            ImageView iconover = new ImageView(new Image("file:" + "src\\prograIconos\\ver.png"));

            iconoEliminar.setFitWidth(16);
            iconoEliminar.setFitHeight(16);

            iconoModificar.setFitWidth(16);
            iconoModificar.setFitHeight(16);

            iconover.setFitWidth(16);
            iconover.setFitHeight(16);

            btnEliminar.setGraphic(iconoEliminar);
            btnModificar.setGraphic(iconoModificar);
            btnVer.setGraphic(iconover);

            btnEliminar.getStyleClass().add("boton-eliminar");
            btnModificar.getStyleClass().add("boton-modificar");
            btnVer.getStyleClass().add("seeButton");

            btnEliminar.setOnAction(event -> {
                Serie serie = getTableView().getItems().get(getIndex());
                // Mostrar una ventana emergente de confirmación
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this serie?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    adminC.deleteSerie(serie.getId());
                    tablaSerie.getItems().remove(serie);
                }
            });

            btnVer.setOnAction(event -> {
                Stage secundaryStage = new Stage();
                seeSerieScreen(secundaryStage, getTableView().getItems().get(getIndex()));
                serieReturn = getTableView().getItems().get(getIndex());
            });

            btnModificar.setOnAction(event -> {
                switchEditSerieScene(getTableView().getItems().get(getIndex()));

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
                HBox botonesContainer = new HBox(btnVer, btnEliminar, btnModificar);
                botonesContainer.setSpacing(5);
                setGraphic(botonesContainer);
            }
        }
    }

    void seeMovieScreen(Movie movie) {
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setTitle("Movie Information");

        VBox rootPane = new VBox();
        rootPane.setId("root2");
        rootPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setSpacing(10);

        ImageView imageView = new ImageView(
                new Image("file:" + "src\\multimediaCovers\\Movies\\" + movie.getCoverImage()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(820);
        imageView.setFitHeight(400);

        StackPane gradientPane = new StackPane();
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT), new Stop(1, Color.BLACK));
        Region gradientRegion = new Region();
        gradientRegion.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        gradientPane.getChildren().add(gradientRegion);

        Button playButton = new Button("Play");
        playButton.setAlignment(Pos.BOTTOM_LEFT);
        playButton.setPadding(new Insets(5));
        VBox.setMargin(playButton, new Insets(15));

        playButton.setId("button");
        playButton.setOnAction(event -> {
            switchReproductionScene(movie.getFileVideo(), true);
            secondaryStage.close();
        });

        StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(playButton, new Insets(0, 35, 15, 25));
        gradientPane.getChildren().add(playButton);

        StackPane imageStackPane = new StackPane();
        imageStackPane.getChildren().addAll(imageView, gradientPane);

        rootPane.getChildren().add(imageStackPane);

        GridPane movieInfoGrid = new GridPane();
        movieInfoGrid.setHgap(10);
        movieInfoGrid.setVgap(5);
        movieInfoGrid.setPadding(new Insets(10));
        movieInfoGrid.setStyle("-fx-text-fill: white;");

        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);
        Label directorLabel = new Label("Director:");
        directorLabel.setTextFill(Color.WHITE);
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setTextFill(Color.WHITE);
        Label categoryLabel = new Label("Category:");
        categoryLabel.setTextFill(Color.WHITE);
        Label durationLabel = new Label("Duration:");
        durationLabel.setTextFill(Color.WHITE);

        Label name = new Label(movie.getName());
        name.setTextFill(Color.WHITE);
        Text director = new Text(movie.getAuthor());
        director.setFill(Color.WHITE);
        Text description = new Text(movie.getDescription());
        description.setFill(Color.WHITE);
        Label category = new Label(movie.getCategory());
        category.setTextFill(Color.WHITE);
        Label duration = new Label(String.valueOf(movie.getDuration()));
        duration.setTextFill(Color.WHITE);

        description.setWrappingWidth(400);
        description.maxWidth(400);
        description.maxHeight(60);

        director.setWrappingWidth(200);
        director.maxWidth(200);
        director.maxHeight(60);

        movieInfoGrid.add(nameLabel, 0, 0);
        movieInfoGrid.add(name, 1, 0);
        movieInfoGrid.add(durationLabel, 2, 0);
        movieInfoGrid.add(duration, 3, 0);
        movieInfoGrid.add(directorLabel, 2, 1);
        movieInfoGrid.add(director, 3, 1);
        movieInfoGrid.add(descriptionLabel, 0, 1);
        movieInfoGrid.add(description, 1, 1);
        movieInfoGrid.add(categoryLabel, 2, 2);
        movieInfoGrid.add(category, 3, 2);
        rootPane.getChildren().add(movieInfoGrid);
        rootPane.setId("rootPane");

        Scene seeMovieScene = new Scene(rootPane, 820, 600);
        seeMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\see.css").toURI().toString());
        secondaryStage.setScene(seeMovieScene);
        secondaryStage.showAndWait();
    }

    public void seeSerieScreen(Stage secundaryStage, Serie serie) {
        this.secundaryStage = secundaryStage;
        secundaryStage.initModality(Modality.APPLICATION_MODAL);
        secundaryStage.setTitle("Serie Information");

        VBox rootPane = new VBox();
        rootPane.setId("root2");
        rootPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView imageView = new ImageView(
                new Image("file:" + "src\\multimediaCovers\\Series\\" + serie.getCoverImage()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(820);
        imageView.setFitHeight(400);

        StackPane gradientPane = new StackPane();
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT), new Stop(1, Color.BLACK));
        Region gradientRegion = new Region();
        gradientRegion.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        gradientPane.getChildren().add(gradientRegion);

        Button playButton = new Button("Play");
        playButton.setAlignment(Pos.BOTTOM_LEFT);
        playButton.setPadding(new Insets(5));
        VBox.setMargin(playButton, new Insets(15));

        playButton.setId("button");
        playButton.setOnAction(event -> {
            switchReproductionScene(serie.getFileVideo(), false);
            secundaryStage.close();
        });

        StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(playButton, new Insets(0, 35, 15, 25));
        gradientPane.getChildren().add(playButton);

        StackPane imageStackPane = new StackPane();
        imageStackPane.getChildren().addAll(imageView, gradientPane);
        VBox infoPane = new VBox();
        infoPane.setSpacing(10);
        infoPane.setPadding(new Insets(10));
        infoPane.setStyle("-fx-text-fill: white;");

        GridPane serieInfoGrid = new GridPane();
        serieInfoGrid.setHgap(10);
        serieInfoGrid.setVgap(5);

        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);
        Label directorLabel = new Label("Director:");
        directorLabel.setTextFill(Color.WHITE);
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setTextFill(Color.WHITE);
        Label categoryLabel = new Label("Category:");
        categoryLabel.setTextFill(Color.WHITE);

        Label name = new Label(serie.getName());
        name.setTextFill(Color.WHITE);
        Text director = new Text(serie.getAuthor());
        director.setFill(Color.WHITE);
        Text description = new Text(serie.getDescription());
        description.setFill(Color.WHITE);
        Label category = new Label(serie.getCategory());
        category.setTextFill(Color.WHITE);

        description.setWrappingWidth(400);
        description.maxWidth(400);
        description.maxHeight(60);

        director.setWrappingWidth(200);
        director.maxWidth(200);
        director.maxHeight(60);

        serieInfoGrid.add(nameLabel, 0, 0);
        serieInfoGrid.add(name, 1, 0);
        serieInfoGrid.add(directorLabel, 2, 1);
        serieInfoGrid.add(director, 3, 1);
        serieInfoGrid.add(descriptionLabel, 0, 1);
        serieInfoGrid.add(description, 1, 1);
        serieInfoGrid.add(categoryLabel, 2, 2);
        serieInfoGrid.add(category, 3, 2);

        infoPane.getChildren().add(serieInfoGrid);

        HBox seasonsPane = new HBox();
        seasonsPane.setSpacing(10);
        seasonsPane.setPadding(new Insets(10));
        seasonsPane.setStyle("-fx-text-fill: white;");

        Label seasonsLabel = new Label("Temporadas");
        seasonsLabel.setTextFill(Color.WHITE);
        ChoiceBox<String> seasonsChoiceBox = new ChoiceBox<>();
        seasonsChoiceBox.setPrefWidth(200);
        seasonsChoiceBox.getItems()
                .addAll(serie.getSeasons().stream().map(Season::getSeasonName).collect(Collectors.toList()));

        TableView<MultimediaContent> episodesTable = new TableView<>();
        episodesTable.setStyle("-fx-background-color: black;");
        episodesTable.setStyle("-fx-text-fill: white;");
        // Crear la columna para el nombre del episodio
        TableColumn<MultimediaContent, String> episodeNameColumn = new TableColumn<>("Name");

        episodeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<MultimediaContent, String> episodeDescriptionColumn = new TableColumn<>("Description");
        episodeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<MultimediaContent, Image> episodeImageColumn = new TableColumn<>("Image");
        episodeImageColumn.setCellValueFactory(cellData -> {
            // Aquí creas una propiedad observable que contiene la imagen
            Image image = new Image("file:" + "src\\multimediaCovers\\Series\\" + serie.getCoverImage());
            return new SimpleObjectProperty<>(image);
        });

        // Especificar el ancho predeterminado para la columna de imagen
        episodeImageColumn.setPrefWidth(300); // Ajusta el valor según tus necesidades
        episodeNameColumn.setPrefWidth(200);
        episodeDescriptionColumn.setPrefWidth(300);

        episodeImageColumn.setCellFactory(param -> new ImageTableCell<>());

        // Agregar las columnas a la tabla
        episodesTable.getColumns().addAll(episodeImageColumn, episodeNameColumn, episodeDescriptionColumn);

        // Escuchar cambios en la selección del ChoiceBox de temporadas
        seasonsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Season selectedSeason = serie.getSeasons().stream()
                    .filter(season -> season.getSeasonName().equals(newValue))
                    .findFirst()
                    .orElse(null);
            if (selectedSeason != null) {
                // Limpiar y agregar los episodios de la temporada seleccionada a la tabla
                episodesTable.getItems().clear();
                episodesTable.getItems().addAll(selectedSeason.getchapters());
            }
        });

        seasonsPane.getChildren().addAll(seasonsLabel, seasonsChoiceBox);

        rootPane.getChildren().addAll(imageStackPane, infoPane, seasonsPane, episodesTable);
        rootPane.setId("rootPane");

        ScrollPane scrollPane = new ScrollPane(rootPane);

        Scene seeSerieScene = new Scene(scrollPane, 820, 600);

        seeSerieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\see.css").toURI().toString());
        secundaryStage.setScene(seeSerieScene);
        secundaryStage.showAndWait();
    }

    public class ImageTableCell<S, T> extends TableCell<S, Image> {
        private final ImageView imageView;

        public ImageTableCell() {
            this.imageView = new ImageView();
            this.imageView.setFitWidth(300); // ajusta el ancho de la imagen según sea necesario
            this.imageView.setFitHeight(300);
            this.imageView.setPreserveRatio(true); // conserva la relación de aspecto de la imagen
            setGraphic(imageView);
        }

        @Override
        protected void updateItem(Image item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                imageView.setImage(null); // si la celda está vacía, elimina la imagen
            } else {
                imageView.setImage(item); // establece la imagen en la celda
            }
        }
    }

    public static void switchReproductionScene(String nameFile, boolean type) {
        displayScreen = new DisplayMultimediaScreen();
        primaryStage.setScene(displayScreen.multimediaScene(nameFile, type, "EntryWindow"));
    }

    public void cambiarAEscena1() {
        primaryStage.setScene(scene1);
    }

    public void cambiarAEscena2() {
        primaryStage.setScene(scene2);
    }

    public Scene getScene1() {
        showMovieScene();
        return scene1;
    }

    public Scene getScene2() {
        entryWindowSerie();
        return scene2;
    }
}
