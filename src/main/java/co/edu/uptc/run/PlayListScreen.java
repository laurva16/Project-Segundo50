package co.edu.uptc.run;

import java.io.File;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.PlayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private TableView<PlayList> tablePlayList;
    private Stage primaryStage;
    private AdminController adminC;
    private Scene scene1;
    private UserRegisteredController userRegisteredController;
    private ObservableList<PlayList> grupos;

    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public PlayListScreen() {
        primaryStage = LogInWindow.getPrimaryStage();
        adminC = new AdminController();
        userRegisteredController = new UserRegisteredController();
        userRegisteredController.addPlayList("pl1");
        userRegisteredController.addPlayList("pl2");
        userRegisteredController.addPlayList("pl3");
    }

    public void showPlayListScene() {
        BorderPane root = new BorderPane();
        userScreen = new UserScreen();
        root.setTop(userScreen.getMenuBar());
        tablePlayList = new TableView<>();
        grupos = FXCollections.observableArrayList(userRegisteredController.getPlayList());

        TableColumn<PlayList, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PlayList, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tablePlayList.getColumns().addAll(nameColumn, accionesColumna);

        accionesColumna.prefWidthProperty().bind(tablePlayList.widthProperty().divide(2));
        nameColumn.prefWidthProperty().bind(tablePlayList.widthProperty().divide(2));
        nameColumn.setStyle("-fx-alignment: CENTER;");
        accionesColumna.setStyle("-fx-alignment: CENTER;");
        tablePlayList.setMaxWidth(600);

        tablePlayList.setItems(grupos);
        StackPane stackPane = new StackPane(tablePlayList);
        stackPane.setAlignment(Pos.CENTER);
        BorderPane.setMargin(stackPane, new Insets(35, 0, 60, 0));

        root.setCenter(stackPane);
        scene1 = new Scene(root, screenWidth, screenHeight);

        scene1.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(scene1);
        primaryStage.setTitle("PlayList");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public class BotonCelda extends TableCell<PlayList, Void> {
        Button buttonDelete = new Button();
        Button buttonDetails = new Button();
        Button buttonMovies = new Button();
        Button buttonSeries = new Button();

        public BotonCelda() {
            buttonDelete.setCursor(Cursor.HAND);
            buttonDetails.setCursor(Cursor.HAND);
            buttonMovies.setCursor(Cursor.HAND);
            buttonSeries.setCursor(Cursor.HAND);

            // Configura los íconos para los botones
            ImageView iconDelete = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
            ImageView iconDetails = new ImageView(new Image("file:" + "src\\prograIconos\\detalle.png"));
            ImageView iconMovies = new ImageView(new Image("file:" + "src\\prograIconos\\letra-m.png"));
            ImageView iconSeries = new ImageView(new Image("file:" + "src\\prograIconos\\letra-s.png"));

            iconDelete.setFitWidth(16);
            iconDelete.setFitHeight(16);
            iconDetails.setFitWidth(16);
            iconDetails.setFitHeight(16);
            iconMovies.setFitWidth(16);
            iconMovies.setFitHeight(16);
            iconSeries.setFitWidth(16);
            iconSeries.setFitHeight(16);

            buttonDelete.setGraphic(iconDelete);
            buttonDetails.setGraphic(iconDetails);
            buttonMovies.setGraphic(iconMovies);
            buttonSeries.setGraphic(iconSeries);

            buttonDelete.setOnAction(event -> {
            });

            buttonDelete.getStyleClass().add("seeButton");
            buttonDetails.getStyleClass().add("boton-modificar");
            buttonMovies.getStyleClass().add("boton-modificar");
            buttonSeries.getStyleClass().add("boton-modificar");

            // Configura el contenido de las celdas para mostrar los botones
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox botonesContainer = new HBox(buttonDelete, buttonDetails, buttonMovies, buttonSeries);
                botonesContainer.setSpacing(5);
                setGraphic(botonesContainer);
            }
        }
    }
}
