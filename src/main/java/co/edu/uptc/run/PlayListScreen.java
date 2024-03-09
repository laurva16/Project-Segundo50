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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PlayListScreen {
    private static UserScreen userScreen;
    private static Stage primaryStage = LogInWindow.getPrimaryStage();
    private static Scene scene;
    private static UserRegisteredController userRegisteredController;
    private static UserRegistered userRegistered;
    private static Button addNewButton;
    static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    private static BorderPane root;
    private static ScrollPane scrollPanePrincipal;
    private static VBox vBoxPrincipal;
    private static HBox hBoxPrincipalPlayList;
    private static BorderPane borderPanePrincipalPlayList;
    private static Image image;
    private static Label namePlayList;
    private static HBox hBoxActions;
    private static Button buttonAddMultimedia;
    private static Button buttonDeletePlayList;
    private static ImageView imageAddMultimedia;
    private static ImageView imageDeletePlayList;
    private static Button buttonAddPlayList;
    private static ImageView imageAddPlayList;

    // private static TableView<PlayList> tablePlayList;
    // private static ObservableList<PlayList> grupos;

    public PlayListScreen() {
        userRegistered = new UserRegistered();

        userRegisteredController = getUserRegisteredController();
        root = new BorderPane();
        hBoxPrincipalPlayList = new HBox();
        borderPanePrincipalPlayList = new BorderPane();
        imageAddPlayList = new ImageView();
        imageAddMultimedia = new ImageView();
        imageDeletePlayList = new ImageView();

        // tablePlayList = new TableView<>();
    }

    public static void setUserRegistered(UserRegistered userRegistered) {
        PlayListScreen.userRegistered = userRegistered;
        userRegisteredController = new UserRegisteredController();
        userRegisteredController.setCurrentUser(userRegistered);
    }

    public static UserRegistered getUserRegistered() {
        return userRegistered;
    }

    public static UserRegisteredController getUserRegisteredController() {
        return userRegisteredController;
    }

    public static void showPlayListScene() {
        userScreen = new UserScreen();
        root = new BorderPane();
        Rectangle rectangle = new Rectangle(200, screenHeight, Color.valueOf("#191919"));
        Rectangle rectangle2 = new Rectangle(200, screenHeight, Color.valueOf("#191919"));
        addNewPlayList();
        principal();

        root.setTop(userScreen.getMenuBar());
        root.setLeft(rectangle);
        root.setRight(rectangle2);
        root.setCenter(scrollPanePrincipal);

        scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\playList.css").toURI().toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("PlayList");
        primaryStage.setMaximized(true);
    }

    public static void addNewPlayList() {
        buttonAddPlayList = new Button();

        imageAddPlayList = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        imageAddPlayList.setFitHeight(50);
        imageAddPlayList.setFitWidth(50);

        buttonAddPlayList.setGraphic(imageAddPlayList);
        buttonAddPlayList.setStyle("-fx-background-color: transparent;");
        BorderPane.setMargin(buttonAddPlayList,
                new Insets(20, imageAddPlayList.getFitWidth(), 0, imageAddPlayList.getFitWidth()));
    }

    public static void principal() {
        namePlayList = new Label();
        scrollPanePrincipal = new ScrollPane();
        vBoxPrincipal = new VBox();
        hBoxPrincipalPlayList = new HBox();
        buttonAddMultimedia = new Button();
        buttonDeletePlayList = new Button();
        hBoxActions = new HBox();

        scrollPanePrincipal.setContent(vBoxPrincipal);
        scrollPanePrincipal.setStyle("-fx-background-color: #5c5c5c;");
        System.out.println(screenWidth);
        scrollPanePrincipal.setMaxSize(screenWidth, screenHeight);

        vBoxPrincipal.setPrefSize(screenWidth - 190, screenHeight);
        vBoxPrincipal.setStyle("-fx-background-color: #5c5c5c;");

        for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
            namePlayList = new Label(playList.getName());

            buttonsActions();
            HBox hBoxPrincipalPlayList = new HBox(namePlayList, hBoxActions);
            hBoxPrincipalPlayList.getStyleClass().add("hBoxPrincipal");

            vBoxPrincipal.getChildren().add(hBoxPrincipalPlayList);
            vBoxPrincipal.setAlignment(Pos.CENTER);
            hBoxPrincipalPlayList.setMaxWidth(500);
            vBoxPrincipal.setMaxSize(screenWidth - 400, screenHeight);
            VBox.setMargin(hBoxPrincipalPlayList, new Insets(5, 0, 5, 0));
        }
    }

    public static void buttonsActions() {
        Region spacer = new Region();
        spacer.setPrefWidth(100);

        buttonAddMultimedia = new Button();
        imageAddMultimedia = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        imageAddMultimedia.setFitHeight(30);
        imageAddMultimedia.setFitWidth(30);
        buttonAddMultimedia.setGraphic(imageAddMultimedia);

        buttonDeletePlayList = new Button();
        imageDeletePlayList = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        imageDeletePlayList.setFitHeight(30);
        imageDeletePlayList.setFitWidth(30);
        buttonDeletePlayList.setGraphic(imageDeletePlayList);

        hBoxActions = new HBox(spacer, buttonAddMultimedia, buttonDeletePlayList);
    }

    // public static void showPlayListScene2() {
    // userScreen = new UserScreen();

    // BorderPane root = new BorderPane();
    // tablePlayList = new TableView<>();
    // grupos =
    // FXCollections.observableArrayList(userRegisteredController.getPlayList());

    // TableColumn<PlayList, String> nameColumn = new TableColumn<>("Name");
    // nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    // nameColumn.prefWidthProperty().bind(tablePlayList.widthProperty().divide(2));
    // nameColumn.setStyle("-fx-alignment: CENTER;");
    // tablePlayList.setMaxWidth(600);

    // tablePlayList.getColumns().addAll(nameColumn);

    // TableColumn<PlayList, Void> accionesColumna = new TableColumn<>("Actions");
    // accionesColumna.setCellFactory(param -> new BotonCelda());
    // accionesColumna.prefWidthProperty().bind(tablePlayList.widthProperty().divide(2));
    // tablePlayList.getColumns().add(accionesColumna);

    // tablePlayList.setItems(grupos);
    // StackPane stackPane = new StackPane(tablePlayList);
    // stackPane.setAlignment(Pos.CENTER);
    // BorderPane.setMargin(stackPane, new Insets(35, 0, 60, 0));

    // buttonFloat();
    // root.setTop(userScreen.getMenuBar());
    // root.setCenter(stackPane);
    // root.setBottom(addNewButton);
    // scene1 = new Scene(root, screenWidth, screenHeight);

    // addNewButton.setOnAction(event -> addPlayList());

    // scene1.getStylesheets().add(new
    // File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
    // primaryStage.setScene(scene1);
    // primaryStage.setTitle("PlayList");
    // primaryStage.setMaximized(true);
    // primaryStage.show();
    // }

    // public static void buttonFloat() {
    // addNewButton = new Button();
    // ImageView iconoAgregar = new ImageView(new Image("file:" +
    // "src\\prograIconos\\anadir.png"));
    // iconoAgregar.setFitWidth(22);
    // iconoAgregar.setFitHeight(22);
    // addNewButton.setTranslateY(-20);
    // addNewButton.getStyleClass().add("boton-flotante");
    // addNewButton.setGraphic(iconoAgregar);

    // BorderPane.setAlignment(addNewButton, Pos.BOTTOM_RIGHT);
    // BorderPane.setMargin(addNewButton, new Insets(15));
    // }

    // public static void addPlayList() {
    // TextInputDialog addPlayList = new TextInputDialog();
    // addPlayList.setTitle("Add PlayList");
    // addPlayList.setHeaderText("Enter the name of the new PlayList");
    // addPlayList.setContentText("Name: ");

    // Optional<String> name = addPlayList.showAndWait();
    // name.ifPresent(namePlayList -> {
    // userRegisteredController.addPlayList(namePlayList);
    // showPlayListScene();
    // });
    // }

    // public static class BotonCelda extends TableCell<PlayList, Void> {
    // Button buttonDelete = new Button();
    // Button buttonDetails = new Button();
    // Button buttonMovies = new Button();
    // Button buttonSeries = new Button();

    // public BotonCelda() {
    // buttonDelete.setCursor(Cursor.HAND);
    // buttonDetails.setCursor(Cursor.HAND);
    // buttonMovies.setCursor(Cursor.HAND);
    // buttonSeries.setCursor(Cursor.HAND);

    // // Configura los íconos para los botones
    // ImageView iconDelete = new ImageView(new Image("file:" +
    // "src\\prograIconos\\eliminar.png"));
    // ImageView iconDetails = new ImageView(new Image("file:" +
    // "src\\prograIconos\\detalle.png"));
    // ImageView iconMovies = new ImageView(new Image("file:" +
    // "src\\prograIconos\\letra-m.png"));
    // ImageView iconSeries = new ImageView(new Image("file:" +
    // "src\\prograIconos\\letra-s.png"));

    // iconDelete.setFitWidth(16);
    // iconDelete.setFitHeight(16);
    // iconDetails.setFitWidth(16);
    // iconDetails.setFitHeight(16);
    // iconMovies.setFitWidth(16);
    // iconMovies.setFitHeight(16);
    // iconSeries.setFitWidth(16);
    // iconSeries.setFitHeight(16);

    // buttonDelete.setGraphic(iconDelete);
    // buttonDetails.setGraphic(iconDetails);
    // buttonMovies.setGraphic(iconMovies);
    // buttonSeries.setGraphic(iconSeries);

    // buttonDelete.getStyleClass().add("seeButton");
    // buttonDetails.getStyleClass().add("boton-modificar");
    // buttonMovies.getStyleClass().add("boton-modificar");
    // buttonSeries.getStyleClass().add("boton-modificar");
    // setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    // }

    // @Override
    // protected void updateItem(Void item, boolean empty) {
    // super.updateItem(item, empty);
    // if (empty) {
    // setGraphic(null);
    // } else {
    // HBox botonesContainer = new HBox(buttonDelete, buttonDetails, buttonMovies,
    // buttonSeries);
    // botonesContainer.setSpacing(10);
    // setGraphic(botonesContainer);
    // }
    // }
    // }
}
