package co.edu.uptc.run;

import java.io.File;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.controller.PlayListController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.PlayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PlayListScreen {
    private UserScreen userScreen;
    private TableView<Movie> tablePlayList;
    private Stage primaryStage;
    private AdminController adminC;
    private Scene scene1;

    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    ObservableList<Movie> grupos;

    public PlayListScreen() {
        primaryStage = LogInWindow.getPrimaryStage();
        adminC = new AdminController();
    }

    public void showPlayListScene() {
        BorderPane root = new BorderPane();
        userScreen = new UserScreen();
        root.setTop(userScreen.getMenuBar());

        tablePlayList = new TableView<>();

        grupos = FXCollections.observableArrayList(adminC.getMovies());

        TableColumn<Movie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        nameColumn.prefWidthProperty().bind(tablePlayList.widthProperty().divide(4));
        genreColumn.prefWidthProperty().bind(tablePlayList.widthProperty().divide(4));
        descriptionColumn.prefWidthProperty().bind(tablePlayList.widthProperty().divide(4));

        // Configurar estilo de las columnas
        nameColumn.setStyle("-fx-alignment: CENTER;");
        genreColumn.setStyle("-fx-alignment: CENTER;");
        descriptionColumn.setStyle("-fx-alignment: CENTER;");

        tablePlayList.getColumns().addAll(nameColumn, genreColumn, descriptionColumn);

        // Establecer ancho máximo para la tabla
        tablePlayList.setMaxWidth(600);

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tablePlayList.getColumns().add(accionesColumna);

        tablePlayList.setItems(grupos);
        StackPane stackPane = new StackPane(tablePlayList);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane
        BorderPane.setMargin(stackPane, new Insets(35, 0, 60, 0));

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root.setCenter(stackPane);

        scene1 = new Scene(root, screenWidth, screenHeight);

        // Configurar la escena y mostrarla
        scene1.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(scene1);
        primaryStage.setTitle("PlayList");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public class BotonCelda extends TableCell<Movie, Void> {
        Button btnWatch = new Button();
        Button btnDetails = new Button();
        MenuButton btnPlayList = new MenuButton();

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

            btnWatch.setOnAction(event -> {
            });

            btnWatch.getStyleClass().add("seeButton");
            btnDetails.getStyleClass().add("boton-modificar");
            btnPlayList.getStyleClass().add("playListButton");

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
}
