package co.edu.uptc.run;

import java.io.File;
import java.util.stream.Collectors;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.PlayList;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import co.edu.uptc.model.UserRegistered;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
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

public class UserScreen {

    private UserRegisteredController userRegisteredController;
    private UserRegistered userRegistered;

    private ScrollPane scrollPane;
    private BorderPane root;
    private double screenWidth, screenHeight;
    private AdminController adminController;
    private LogInWindow logInWindow;
    private Stage primaryStage, secundaryStage;

    // First screen
    private ImageView image;
    private Label labelNameF;
    private FlowPane flowPane;
    private Scene sceneF, scene2F;
    private String pathMovies;
    private String pathSeries;
    private Tooltip tooltipName, tooltipPlayList;
    Button movieButton = new Button("Movie");
    Button serieButton = new Button("Serie");
    Button playListButton = new Button("PlayList");
    Button subscriptionButton = new Button("Subscription");
    Button returnButton = new Button("Log Out");
    MenuButton btnPlayList;

    public UserScreen() {
        screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        scrollPane = new ScrollPane();
        adminController = new AdminController();
        labelNameF = new Label();
        flowPane = new FlowPane();
        logInWindow = new LogInWindow();
        primaryStage = LogInWindow.getPrimaryStage();
        pathMovies = "src\\multimediaCovers\\Movies\\";
        pathSeries = "src\\multimediaCovers\\Series\\";
        userRegistered = new UserRegistered();
        logInWindow = new LogInWindow();
        primaryStage = LogInWindow.getPrimaryStage();
    }

    public UserRegistered getUserRegistered() {
        return userRegistered;
    }

    public void setUserRegistered(UserRegistered userRegistered) {
        this.userRegistered = userRegistered;
        userRegisteredController = new UserRegisteredController();
        userRegisteredController.setCurrentUser(userRegistered);
        PlayListScreen.setUserRegistered(userRegistered);
    }

    public void principalScene() {
        messages();
        flowPane.setStyle("-fx-background-color: #191919;");
        flowPane.setHgap(10);
        flowPane.setAlignment(Pos.CENTER_LEFT);
        flowPane.setMaxHeight(screenHeight);
        flowPane.setPadding(new Insets(0, 90, 0, 90));

        scrollPane = new ScrollPane(flowPane);
        scrollPane.setStyle("-fx-background-color: #191919;");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    public void scene1() {
        BorderPane root = new BorderPane();
        ToolBar menuBar = createMenuBar();
        movieButton.setStyle("-fx-text-fill: black;");

        root.setTop(menuBar);
        root.setCenter(scrollPane);

        sceneF = new Scene(root, screenWidth, screenHeight);
        sceneF.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(sceneF);
        primaryStage.setTitle("Movies");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void scene2() {
        BorderPane root = new BorderPane();
        ToolBar menuBar = createMenuBar();
        serieButton.setStyle("-fx-text-fill: black;");

        root.setTop(menuBar);
        root.setCenter(scrollPane);

        scene2F = new Scene(root, screenWidth, screenHeight);
        scene2F.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(scene2F);
        primaryStage.setTitle("Series");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void moviesList() {
        for (Movie movies : adminController.getMovies()) {
            image = new ImageView(new Image("file:" + pathMovies + movies.getCoverImage()));
            image.setCursor(Cursor.HAND);
            image.setOnMouseClicked(event -> {
                seeMovieScreen(movies);
            });
            labelNameF = new Label(movies.getName());
            labelNameF.setId("labelName");
            VBox vBox = new VBox(labelNameF, image);

            image.setFitWidth(240);
            image.setFitHeight(120);
            labelNameF.setMaxWidth(180);
            FlowPane.setMargin(vBox, new Insets(20, 0, 20, 20));
            flowPane.getChildren().add(vBox);

            tooltipName = new Tooltip(movies.getName());
            Tooltip.install(labelNameF, tooltipName);
        }
    }

    public void seriesList() {
        for (Serie serie : adminController.getListSeries()) {
            image = new ImageView(new Image("file:" + pathSeries + serie.getCoverImage()));
            image.setCursor(Cursor.HAND);
            image.setOnMouseClicked(event -> {
                Stage secundaryStage = new Stage();
                seeSerieScreen(secundaryStage, serie);
            });
            labelNameF = new Label(serie.getName());
            labelNameF.setId("labelName");
            VBox vBox = new VBox(labelNameF, image);

            image.setFitWidth(240);
            image.setFitHeight(120);
            labelNameF.setMaxWidth(180);
            FlowPane.setMargin(vBox, new Insets(20, 0, 20, 20));
            flowPane.getChildren().add(vBox);

            tooltipName = new Tooltip(serie.getName());
            Tooltip.install(labelNameF, tooltipName);
        }
    }

    public void messages() {
        labelNameF.setOnMouseEntered(
                event -> tooltipName.show(labelNameF, event.getScreenX(), event.getScreenY() + 10));
        labelNameF.setOnMouseExited(event -> tooltipName.hide());
    }

    public void messagesPl() {
        btnPlayList.setOnMouseEntered(
                event -> tooltipPlayList.show(btnPlayList, event.getScreenX(), event.getScreenY() + 10));
        btnPlayList.setOnMouseExited(event -> tooltipPlayList.hide());
    }

    private ToolBar createMenuBar() {
        Region spacer = new Region();
        movieButton = new Button("Movie");
        serieButton = new Button("Serie");
        playListButton = new Button("PlayList");
        returnButton = new Button("Return");
        movieButton.setCursor(Cursor.HAND);
        serieButton.setCursor(Cursor.HAND);
        playListButton.setCursor(Cursor.HAND);
        returnButton.setCursor(Cursor.HAND);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        ToolBar toolBar = new ToolBar(movieButton, serieButton, playListButton, spacer, returnButton);
        toolBar.getStyleClass().add("menubar");
        movieButton.getStyleClass().add("menu");
        serieButton.getStyleClass().add("menu");
        playListButton.getStyleClass().add("playList");
        returnButton.getStyleClass().add("menu");

        // Asignar acciones a los botones
        movieButton.setOnAction(event -> {
            flowPane.getChildren().clear();
            moviesList();
            principalScene();
            scene1();
        });
        serieButton.setOnAction(event -> {
            flowPane.getChildren().clear();
            seriesList();
            principalScene();
            scene2();
        });

        playListButton.setOnAction(event -> {
            // Evita que el usuario registrado se vuelva null
            if (PlayListScreen.getUserRegistered() != null) {
                userRegistered = PlayListScreen.getUserRegistered();
            } else {
                PlayListScreen.setUserRegistered(userRegistered);
            }
            PlayListScreen.showPlayListScene();
        });

        returnButton.setOnAction(event -> LogInWindow.getSceneLogIn());
        return toolBar;
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
            // Lógica para reproducir la película
        });

        StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(playButton, new Insets(0, 35, 15, 25));
        gradientPane.getChildren().add(playButton);

        // PlayList
        playListButtonStyle();
        if (!userRegisteredController.getCurrentUser().getplayList().isEmpty()) {
            for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
                if (validationPlayListMovie(playList, movie.getName())) {
                    // Adquiere la playList
                    MenuItem namePlayList = new MenuItem(playList.getName());
                    namePlayList.setUserData(playList.getName());
                    btnPlayList.getItems().add(namePlayList);

                    namePlayList.setOnAction(event -> {
                        if (userRegisteredController.addMovieToPlayList(playList.getName(),
                                movie.getId())) {
                            PlayListScreen.setUserRegistered(userRegisteredController.getCurrentUser());
                            alertMovie(playList.getName(), movie.getName());
                        }
                    });
                }
            }
            if (btnPlayList.getItems().isEmpty()) {
                MenuItem nullPl = new MenuItem("It is found in all playList, \nadd a new one");
                btnPlayList.getItems().add(nullPl);
                nullPl.setOnAction(event -> {
                    PlayListScreen.showPlayListScene();
                    secondaryStage.close();
                });
            }
        } else {
            // Accede al botón como tal del btnPlaylist
            btnPlayList.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
                if (!isNowShowing) {
                    alertNonExistentPlayList();
                }
            });
        }
        btnPlayList.getStyleClass().add("playListButton");

        StackPane.setAlignment(btnPlayList, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnPlayList, new Insets(0, 25, 15, 35));
        gradientPane.getChildren().add(btnPlayList);

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

        ImageView imageView = new ImageView(new Image("file:" + "src\\multimediaCovers\\Series\\stranger.jpeg"));
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
            // Lógica para reproducir la película
        });

        StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(playButton, new Insets(0, 35, 15, 25));
        gradientPane.getChildren().add(playButton);

        // PlayList
        playListButtonStyle();
        if (!userRegisteredController.getCurrentUser().getplayList().isEmpty()) {
            for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
                if (validationPlayListSerie(playList, serie.getName())) {
                    // Adquiere la playList
                    MenuItem namePlayList = new MenuItem(playList.getName());
                    namePlayList.setUserData(playList.getName());
                    btnPlayList.getItems().add(namePlayList);

                    namePlayList.setOnAction(event -> {
                        if (userRegisteredController.addSerieToPlayList(playList.getName(),
                                serie.getId())) {
                            PlayListScreen.setUserRegistered(userRegisteredController.getCurrentUser());
                            alertSerie(playList.getName(), serie.getName());
                        }
                    });

                }
            }
            if (btnPlayList.getItems().isEmpty()) {
                MenuItem nullPl = new MenuItem("It is found in all playList, \nadd a new one");
                btnPlayList.getItems().add(nullPl);
                nullPl.setOnAction(event -> {
                    PlayListScreen.showPlayListScene();
                    secundaryStage.close();
                });
            }
        } else {
            // Accede al botón como tal del btnPlaylist
            btnPlayList.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
                if (!isNowShowing) {
                    alertNonExistentPlayList();
                }
            });
        }
        btnPlayList.getStyleClass().add("playListButton");

        StackPane.setAlignment(btnPlayList, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnPlayList, new Insets(0, 25, 15, 35));
        gradientPane.getChildren().add(btnPlayList);

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
            Image image = new Image("file:" + "src\\multimediaCovers\\Series\\stranger.jpeg");
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

    public Scene getScene1() {
        flowPane.getChildren().clear();
        moviesList();
        principalScene();
        scene1();
        return sceneF;
    }

    public Scene getScene2() {
        flowPane.getChildren().clear();
        seriesList();
        principalScene();
        scene2();
        return scene2F;
    }

    public ToolBar getMenuBar() {
        return createMenuBar();
    }

    public void alertMovie(String playListName, String movieName) {
        Alert comprobation = new Alert(AlertType.INFORMATION);
        comprobation.setTitle("PlayList");
        comprobation.setHeaderText("Successfully added to the playList");
        comprobation.setContentText("The movie " + movieName
                + " was added to PlayList " + playListName);
        comprobation.showAndWait();
    }

    public void alertSerie(String playList, String serieName) {
        Alert comprobation = new Alert(AlertType.INFORMATION);
        comprobation.setTitle("PlayList");
        comprobation.setHeaderText("Successfully added to the playList");
        comprobation.setContentText("The serie " + serieName
                + " was added to PlayList " + playList);
        comprobation.showAndWait();
    }

    public void alertNonExistentPlayList() {
        Alert comprobation = new Alert(AlertType.INFORMATION);
        comprobation.setTitle("PlayList");
        comprobation.setHeaderText("No playList found");
        comprobation.setContentText(null);
        comprobation.showAndWait();
    }

    public boolean validationPlayListMovie(PlayList playList, String name) {
        for (Movie movie : playList.getMovies()) {
            if (movie.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean validationPlayListSerie(PlayList playList, String nameSerie) {
        for (Serie serie : playList.getSeries()) {
            if (serie.getName().equals(nameSerie)) {
                return false;
            }
        }
        return true;
    }

    public void playListButtonStyle() {
        btnPlayList = new MenuButton();
        btnPlayList.setCursor(Cursor.HAND);
        btnPlayList.setStyle("-fx-background-color: transparent;");
        ImageView iconoPlayList = new ImageView(new Image("file:" +
                "src\\prograIconos\\agregarVerde.png"));
        iconoPlayList.setFitWidth(30);
        iconoPlayList.setFitHeight(30);
        btnPlayList.setGraphic(iconoPlayList);
        tooltipPlayList = new Tooltip("Add to playList");
        Tooltip.install(btnPlayList, tooltipPlayList);
        messagesPl();
    }
}
