package co.edu.uptc.run;

import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.model.Movie;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class pruebafx extends Application {
    private TableView<Movie> tabla = new TableView<>();
    AdminController gc;

    public pruebafx() {
        gc = new AdminController();
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu MovieMenu = new Menu("Movie");
        Menu SerieMenu = new Menu("Serie");

        menuBar.getMenus().add(MovieMenu);
        menuBar.getMenus().add(SerieMenu);

        menuBar.getStyleClass().add("menubar");

        root.setTop(menuBar);

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        ObservableList<Movie> grupos = FXCollections.observableArrayList(gc.getMovies());

        TableColumn<Movie, String> IdColumn = new TableColumn<>("Id");
        TableColumn<Movie, String> facultyColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> nombreGrupoColumn = new TableColumn<>("Director");

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nombreGrupoColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        tabla.getColumns().addAll(IdColumn, facultyColumn, nombreGrupoColumn);

        // Establecer ancho máximo para la tabla
        tabla.setMaxWidth(600);

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tabla.getColumns().add(accionesColumna);

        tabla.setItems(grupos);

        StackPane stackPane = new StackPane(tabla);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane

        // Crear un botón flotante
        ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        iconoAgregar.setFitWidth(22);
        iconoAgregar.setFitHeight(22);
        Button botonFlotante = new Button();
        botonFlotante.getStyleClass().add("boton-flotante");
        botonFlotante.setGraphic(iconoAgregar);

        // Agregar el botón flotante en la esquina inferior derecha
        BorderPane.setAlignment(botonFlotante, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(botonFlotante, new Insets(15));
        root.setBottom(botonFlotante);

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root.setCenter(stackPane);

        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        // Configurar la escena y mostrarla
        scene.getStylesheets().add(getClass().getResource("/co/styles/principal.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX MenuBar with CSS");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class BotonCelda extends TableCell<Movie, Void> {
        // private final Button btnVer = new Button();
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
                alert.setContentText("Are you sure you want to delete this group?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    gc.deleteMovie(grupo.getId());

                    tabla.getItems().remove(grupo);
                    // gc.creararchivoJson(gc.getGroupList(),
                    // "src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json");

                }
            });

            btnVer.setOnAction(event -> {
                // Group group = getTableView().getItems().get(getIndex());
                // modifyGroup modifyGroupWindow = new modifyGroup(gc, group, group.getId());

                // Obtiene el Stage actual
                Stage currentStage = (Stage) tabla.getScene().getWindow();

                // Cierra la ventana actual (TestTabla)
                currentStage.close();

                // Abre la ventana modifyGroup
                Stage modifyGroupStage = new Stage();
                // modifyGroupWindow.start(modifyGroupStage);
            });

            btnModificar.setOnAction(event -> {
                // Group group = getTableView().getItems().get(getIndex());
                // modifyGroup modifyGroupWindow = new modifyGroup(gc, group, group.getId());

                // Obtiene el Stage actual
                Stage currentStage = (Stage) tabla.getScene().getWindow();

                // Cierra la ventana actual (TestTabla)
                currentStage.close();

                // Abre la ventana modifyGroup
                Stage modifyGroupStage = new Stage();
                // modifyGroupWindow.start(modifyGroupStage);
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

}
