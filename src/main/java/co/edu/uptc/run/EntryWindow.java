package co.edu.uptc.run;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
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

public class EntryWindow {
    private TableView<Movie> tablaMovie;
    private TableView<Serie> tablaSerie = new TableView<>();
    private Stage primaryStage, secundaryStage;
    private LogInWindow logInWindow;
    private AdminController adminC;
    private CategoryController categoryC;
    private Scene scene1, scene2;
    private Serie serieReturn;
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

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
    DisplayMultimediaScreen displayScreen;
    ModifySerie modifySerie;

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

        tablaMovie = new TableView<>();

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        ObservableList<Movie> grupos = FXCollections.observableArrayList(adminC.getMovies());

        TableColumn<Movie, String> IdColumn = new TableColumn<>("Id");
        TableColumn<Movie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> directorColumn = new TableColumn<>("Director");

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        IdColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(4));
        nameColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(4));
        directorColumn.prefWidthProperty().bind(tablaMovie.widthProperty().divide(4));

        // Configurar estilo de las columnas
        IdColumn.setStyle("-fx-alignment: CENTER;");
        nameColumn.setStyle("-fx-alignment: CENTER;");
        directorColumn.setStyle("-fx-alignment: CENTER;");

        tablaMovie.getColumns().addAll(IdColumn, nameColumn, directorColumn);

        // Establecer ancho máximo para la tabla
        tablaMovie.setMaxWidth(600);

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tablaMovie.getColumns().add(accionesColumna);

        tablaMovie.setItems(grupos);
        StackPane stackPane = new StackPane(tablaMovie);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
        StackPane.setMargin(tablaMovie, new Insets(20)); // Agregar margen a la tabla

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root.setCenter(stackPane);

        // Crear un botón flotante
        ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        iconoAgregar.setFitWidth(22);
        iconoAgregar.setFitHeight(22);
        Button addNewButton = new Button();
        addNewButton.setTranslateY(-20);
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
        primaryStage.setTitle("JavaFX MenuBar with CSS");
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
        createSerie = new CreateSerie(primaryStage, adminC);
        primaryStage.setScene(createSerie.modifySeries2(serie));
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
        Button movieButton = new Button("Movie");
        Button serieButton = new Button("Serie");
        Button returnButton = new Button("Log Out");
        movieButton.setCursor(Cursor.HAND);
        serieButton.setCursor(Cursor.HAND);
        returnButton.setCursor(Cursor.HAND);

        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Asignar acciones a los botones
        movieButton.setOnAction(event -> {
            // Aquí va la lógica para mostrar la ventana de películas
            cambiarAEscena1();
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

    void entryWindowSerie() {
        if (scene2 == null) {
            BorderPane root2 = new BorderPane();
            ToolBar menuBar = createMenuBar();
            root2.setTop(menuBar);

            // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
            ObservableList<Serie> series = FXCollections.observableArrayList(adminC.getListSeries());

            TableColumn<Serie, String> IdColumn = new TableColumn<>("Id");
            TableColumn<Serie, String> nameColumn = new TableColumn<>("Name");
            TableColumn<Serie, String> directorColumn = new TableColumn<>("Director");

            IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            directorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

            IdColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(4));
            nameColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(4));
            directorColumn.prefWidthProperty().bind(tablaSerie.widthProperty().divide(4));

            // Configurar estilo de las columnas
            IdColumn.setStyle("-fx-alignment: CENTER;");
            nameColumn.setStyle("-fx-alignment: CENTER;");
            directorColumn.setStyle("-fx-alignment: CENTER;");

            tablaSerie.getColumns().addAll(IdColumn, nameColumn, directorColumn);

            // Establecer ancho máximo para la tabla
            tablaSerie.setMaxWidth(600);

            // Agregar columna de botones
            TableColumn<Serie, Void> accionesColumna = new TableColumn<>("Actions");
            accionesColumna.setCellFactory(param -> new BotonCeldaSerie());
            tablaSerie.getColumns().add(accionesColumna);

            tablaSerie.setItems(series);

            StackPane stackPane = new StackPane(tablaSerie);
            stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
            StackPane.setMargin(tablaSerie, new Insets(20)); // Agregar margen a la tabla

            // Agregar el StackPane que contiene la tabla al centro del BorderPane
            root2.setCenter(stackPane);

            // Crear un botón flotante
            ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
            iconoAgregar.setFitWidth(22);
            iconoAgregar.setFitHeight(22);
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
            primaryStage.setTitle("JavaFX series with CSS");
            primaryStage.setMaximized(true);
            // Add new Movie scene
            addNewButton.setOnAction(event -> switchNewSerieScene());
        }
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
        Label fileVideo = new Label(movie.getFileVideo());

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);
        GridPane.setConstraints(labelFileVideo, 0, 5);

        GridPane.setConstraints(name, 1, 0);
        GridPane.setConstraints(director, 1, 1);
        GridPane.setConstraints(description, 1, 2);
        GridPane.setConstraints(duration, 1, 3);
        GridPane.setConstraints(category, 1, 4);
        GridPane.setConstraints(fileVideo, 1, 5);

        Button closeButton = new Button();
        closeButton.setTranslateY(50);
        closeButton.setText("Close");
        closeButton.setPrefWidth(100);
        GridPane.setConstraints(closeButton, 1, 6);
        closeButton.setOnAction(event -> secundaryStage.close());
        closeButton.setId("button");

        gridPane.getChildren().setAll(labelName, labelDirector, labelDescription, labelDuration, labelCategory, name,
                director, description, duration, category, closeButton, fileVideo, labelFileVideo);

        // Configurar tamano description
        description.setMaxWidth(200);
        description.setWrapText(true);

        Scene seeMovieScene = new Scene(gridPane, 500, 550);
        seeMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        secundaryStage.setScene(seeMovieScene);
        secundaryStage.showAndWait();
    }

    public void seeSerieScreen(Stage secundaryStage, Serie serie) {

        this.secundaryStage = secundaryStage;
        secundaryStage.initModality(Modality.APPLICATION_MODAL);
        secundaryStage.setTitle("Serie Information");

        GridPane gridPane = new GridPane();
        gridPane.setId("root2");
        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);
        gridPane.setHgap(50);

        Label nameLabel = new Label("Name:");
        Label directorLabel = new Label("Director:");
        Label descriptionLabel = new Label("Description:");
        Label categoryLabel = new Label("Category:");

        Label name = new Label(serie.getName());
        Label director = new Label(serie.getAuthor());
        Label description = new Label(serie.getDescription());
        Label category = new Label(serie.getCategory());

        Button closeButton = new Button();
        closeButton.setTranslateY(50);
        closeButton.setText("Close");
        closeButton.setPrefWidth(100);
        GridPane.setConstraints(closeButton, 1, 6);
        closeButton.setOnAction(event -> secundaryStage.close());
        closeButton.setId("button");

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(name, 1, 0);
        gridPane.add(directorLabel, 0, 1);
        gridPane.add(director, 1, 1);
        gridPane.add(descriptionLabel, 0, 2);
        gridPane.add(description, 1, 2);
        gridPane.add(categoryLabel, 0, 3);
        gridPane.add(category, 1, 3);
        gridPane.add(closeButton, 1, 6);

        ObservableList<String> seasonsList = FXCollections.observableArrayList();
        ChoiceBox<String> additionalOptions = new ChoiceBox<>(seasonsList);
        additionalOptions.setPrefWidth(250);
        gridPane.add(new Label("Season:"), 0, 4);
        gridPane.add(additionalOptions, 1, 4);

        ObservableList<String> multimediaContentList = FXCollections.observableArrayList();
        ChoiceBox<String> additionalOptionsMultimediaContent = new ChoiceBox<>(multimediaContentList);
        additionalOptionsMultimediaContent.setPrefWidth(250);
        gridPane.add(new Label("Episode:"), 0, 5);
        gridPane.add(additionalOptionsMultimediaContent, 1, 5);

        for (Season season : serie.getSeasons()) {
            seasonsList.add(season.getSeasonName());
        }

        additionalOptions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                multimediaContentList.clear();
                Season selectedSeason = serie.getSeasons().stream()
                        .filter(season -> season.getSeasonName().equals(newValue))
                        .findFirst()
                        .orElse(null);
                if (selectedSeason != null && selectedSeason.getchapters() != null) {
                    for (MultimediaContent chapter : selectedSeason.getchapters()) {
                        multimediaContentList.add(chapter.getName());
                    }
                }
            }
        });

        additionalOptionsMultimediaContent.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Season selectedSeason = serie.getSeasons().stream()
                                .filter(season -> season.getSeasonName().equals(additionalOptions.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedSeason != null && selectedSeason.getchapters() != null) {
                            MultimediaContent selectedChapter = selectedSeason.getchapters().stream()
                                    .filter(chapter -> chapter.getName().equals(newValue))
                                    .findFirst()
                                    .orElse(null);
                            if (selectedChapter != null) {
                                displayEpisodeInfo(selectedChapter);
                            }
                        }
                    }
                });

        Scene seeSerieScene = new Scene(gridPane, 500, 550);
        seeSerieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        secundaryStage.setScene(seeSerieScene);
        secundaryStage.showAndWait();
    }

    private void displayEpisodeInfo(MultimediaContent episode) {
        Stage episodeStage = new Stage();
        episodeStage.setTitle("Episode Information");

        GridPane episodeGridPane = new GridPane();
        episodeGridPane.setId("root2");
        episodeGridPane.setMaxWidth(600);
        episodeGridPane.setMaxHeight(600);
        episodeGridPane.setAlignment(Pos.CENTER);
        episodeGridPane.setVgap(20);
        episodeGridPane.setHgap(50);

        Label nameLabel = new Label("Name:");
        Label descriptionLabel = new Label("Description:");
        Label durationLabel = new Label("Duration:");

        Button closeButton = new Button();
        closeButton.setTranslateY(50);
        closeButton.setText("Close");
        closeButton.setPrefWidth(100);
        GridPane.setConstraints(closeButton, 1, 6);
        closeButton.setOnAction(event -> episodeStage.close()); // Cambio aquí
        closeButton.setId("button");

        Label name = new Label(episode.getName());
        Label description = new Label(episode.getDescription());
        Label duration = new Label(String.valueOf(episode.getDuration()));

        episodeGridPane.add(nameLabel, 0, 0);
        episodeGridPane.add(name, 1, 0);
        episodeGridPane.add(descriptionLabel, 0, 1);
        episodeGridPane.add(description, 1, 1);
        episodeGridPane.add(durationLabel, 0, 2);
        episodeGridPane.add(duration, 1, 2);
        episodeGridPane.add(closeButton, 1, 3);

        Scene episodeScene = new Scene(episodeGridPane, 500, 550);
        episodeScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        episodeStage.setScene(episodeScene);
        episodeStage.show();
    }

    public void cambiarAEscena1() {
        primaryStage.setScene(scene1);
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
