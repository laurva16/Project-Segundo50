package co.edu.uptc.run;

import java.io.File;
import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.Serie;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class EntryWindow extends Application {
    private TableView<Movie> tablaMovie;
    private TableView<Serie> tablaSerie = new TableView<>();
    private Stage primaryStage;
    private AdminController gc;
    private Scene scene1, scene2;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public EntryWindow() {
        gc = new AdminController();
    }

    @Override
    public void start(Stage primaryStage) {
        tablaMovie = new TableView<>();
        this.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        ToolBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        ObservableList<Movie> grupos = FXCollections.observableArrayList(gc.getMovies());

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

    }

    public static void main(String[] args) {
        launch(args);
    }

    public class BotonCelda extends TableCell<Movie, Void> {
        private final Button btnEliminar = new Button();
        private final Button btnModificar = new Button();
        private final Button btnVer = new Button();

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
            btnVer.setGraphic(iconover);

            btnEliminar.getStyleClass().add("boton-eliminar");
            btnModificar.getStyleClass().add("boton-modificar");
            btnVer.getStyleClass().add("boton-ver");

            btnEliminar.setOnAction(event -> {
                Movie grupo = getTableView().getItems().get(getIndex());
                // Mostrar una ventana emergente de confirmación
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this movie?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    gc.deleteMovie(grupo.getId());

                    tablaMovie.getItems().remove(grupo);
                }
            });

            btnVer.setOnAction(event -> {
                // Group group = getTableView().getItems().get(getIndex());
                // modifyGroup modifyGroupWindow = new modifyGroup(gc, group, group.getId());
            });

            btnModificar.setOnAction(event -> {
                // Group group = getTableView().getItems().get(getIndex());
                // modifyGroup modifyGroupWindow = new modifyGroup(gc, group, group.getId());

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

    private ToolBar createMenuBar() {
        Button movieButton = new Button("Movie");
        Button serieButton = new Button("Serie");

        // Asignar acciones a los botones
        movieButton.setOnAction(event -> {
            // Aquí va la lógica para mostrar la ventana de películas
            cambiarAEscena1();
        });

        serieButton.setOnAction(event -> {
            entryWindowSerie();
        });

        // Crear la barra de herramientas y agregar los botones
        ToolBar toolBar = new ToolBar(movieButton, serieButton);
        toolBar.getStyleClass().add("menubar");
        movieButton.getStyleClass().add("menu");
        serieButton.getStyleClass().add("menu");

        return toolBar;
    }

    private void entryWindowSerie() {
        if (scene2 == null) {
            BorderPane root2 = new BorderPane();
            ToolBar menuBar = createMenuBar();
            root2.setTop(menuBar);

            // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
            ObservableList<Serie> series = FXCollections.observableArrayList(gc.getListSeries());

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
        }
        primaryStage.setScene(scene2);
        primaryStage.setTitle("JavaFX series with CSS");
        primaryStage.setMaximized(true);
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
            btnVer.getStyleClass().add("boton-ver");

            btnEliminar.setOnAction(event -> {
                Serie serie = getTableView().getItems().get(getIndex());
                // Mostrar una ventana emergente de confirmación
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this serie?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    gc.deleteSerie(serie.getId());
                    tablaSerie.getItems().remove(serie);
                }
            });

            btnVer.setOnAction(event -> {
                // Serie serie = getTableView().getItems().get(getIndex());
                // modifySerie modifySerieWindow = new modifySerie(gc, serie, serie.getId());
            });

            btnModificar.setOnAction(event -> {
                // Serie serie = getTableView().getItems().get(getIndex());
                // modifySerie modifySerieWindow = new modifySerie(gc, serie, serie.getId());
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

    private void cambiarAEscena1() {
        primaryStage.setScene(scene1);
    }
}
