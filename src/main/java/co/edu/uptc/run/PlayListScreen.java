package co.edu.uptc.run;

import java.io.File;
import java.util.Optional;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.PlayList;
import co.edu.uptc.model.UserRegistered;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PlayListScreen {
    private static UserScreen userScreen;
    private static TableView<PlayList> tablePlayList;
    private static Stage primaryStage = LogInWindow.getPrimaryStage();
    private static Scene scene1;
    private static UserRegisteredController userRegisteredController;
    private static UserRegistered userRegistered;
    private static ObservableList<PlayList> grupos;
    private static Button addNewButton;

    static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public PlayListScreen() {
        userRegisteredController = new UserRegisteredController();
        userRegistered = new UserRegistered();
        tablePlayList = new TableView<>();
        userScreen = new UserScreen();
    }

    public static void setUserRegistered(UserRegistered userRegistered) {
        PlayListScreen.userRegistered = userRegistered;
        userRegisteredController = new UserRegisteredController();
        userRegisteredController.setCurrentUser(userRegistered);
    }

    public static UserRegistered getUserRegistered() {
        return userRegistered;
    }

    public static void showPlayListScene() {
        userScreen = new UserScreen();

        BorderPane root = new BorderPane();
        tablePlayList = new TableView<>();
        grupos = FXCollections.observableArrayList(userRegisteredController.getPlayList());

        TableColumn<PlayList, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        nameColumn.prefWidthProperty().bind(tablePlayList.widthProperty().divide(2));
        nameColumn.setStyle("-fx-alignment: CENTER;");
        tablePlayList.setMaxWidth(600);

        tablePlayList.getColumns().addAll(nameColumn);

        TableColumn<PlayList, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        accionesColumna.prefWidthProperty().bind(tablePlayList.widthProperty().divide(2));
        tablePlayList.getColumns().add(accionesColumna);

        tablePlayList.setItems(grupos);
        StackPane stackPane = new StackPane(tablePlayList);
        stackPane.setAlignment(Pos.CENTER);
        BorderPane.setMargin(stackPane, new Insets(35, 0, 60, 0));

        buttonFloat();
        root.setTop(userScreen.getMenuBar());
        root.setCenter(stackPane);
        root.setBottom(addNewButton);
        scene1 = new Scene(root, screenWidth, screenHeight);

        addNewButton.setOnAction(event -> addPlayList());

        scene1.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(scene1);
        primaryStage.setTitle("PlayList");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void buttonFloat() {
        addNewButton = new Button();
        ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        iconoAgregar.setFitWidth(22);
        iconoAgregar.setFitHeight(22);
        addNewButton.setTranslateY(-20);
        addNewButton.getStyleClass().add("boton-flotante");
        addNewButton.setGraphic(iconoAgregar);

        BorderPane.setAlignment(addNewButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(addNewButton, new Insets(15));
    }

    public static void addPlayList() {
        TextInputDialog addPlayList = new TextInputDialog();
        addPlayList.setTitle("Add PlayList");
        addPlayList.setHeaderText("Enter the name of the new PlayList");
        addPlayList.setContentText("Name: ");

        Optional<String> name = addPlayList.showAndWait();
        name.ifPresent(namePlayList -> {
            userRegisteredController.addPlayList(name.toString());
            showPlayListScene();
        });
    }

    public static class BotonCelda extends TableCell<PlayList, Void> {
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

            buttonDelete.getStyleClass().add("seeButton");
            buttonDetails.getStyleClass().add("boton-modificar");
            buttonMovies.getStyleClass().add("boton-modificar");
            buttonSeries.getStyleClass().add("boton-modificar");
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox botonesContainer = new HBox(buttonDelete, buttonDetails, buttonMovies, buttonSeries);
                botonesContainer.setSpacing(10);
                setGraphic(botonesContainer);
            }
        }
    }
}
