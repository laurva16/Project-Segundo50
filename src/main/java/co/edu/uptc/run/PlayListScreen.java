package co.edu.uptc.run;

import java.io.File;
import java.lang.ProcessBuilder.Redirect.Type;
import java.net.http.HttpResponse.BodyHandler;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
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
    private static StackPane stackPaneAddPlayList;
    private static Rectangle rectangle2;
    private static Tooltip tooltipAddPlayList, tooltipMultimedia, tooltipDelete, tooltipName;

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
        stackPaneAddPlayList = new StackPane();

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
        addNewPlayList();
        principal();
        messages();

        root.setTop(userScreen.getMenuBar());
        root.setLeft(stackPaneAddPlayList);
        root.setRight(rectangle);
        root.setCenter(scrollPanePrincipal);

        scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\playList.css").toURI().toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("PlayList");
        primaryStage.setMaximized(true);
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
        BorderPane.setMargin(scrollPanePrincipal, new Insets(0, 0, 80, 0));
        scrollPanePrincipal.setMaxSize(screenWidth, screenHeight);

        vBoxPrincipal.setPrefSize(screenWidth - 190, screenHeight);
        vBoxPrincipal.setStyle("-fx-background-color: #5c5c5c;");

        for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
            namePlayList = new Label(playList.getName());

            buttonsActions();
            BorderPane borderPanePrincipalPlayList = new BorderPane();
            hBoxActions.setSpacing(50);
            borderPanePrincipalPlayList.setRight(hBoxActions);
            borderPanePrincipalPlayList.setLeft(namePlayList);

            borderPanePrincipalPlayList.getStyleClass().add("hBoxPrincipal");
            borderPanePrincipalPlayList.setMinHeight(100);
            borderPanePrincipalPlayList.setMaxWidth(700);

            vBoxPrincipal.getChildren().add(borderPanePrincipalPlayList);
            vBoxPrincipal.setAlignment(Pos.CENTER);
            vBoxPrincipal.setMaxSize(screenWidth - 400, screenHeight);

            hBoxActions.setAlignment(Pos.CENTER_RIGHT);
            BorderPane.setAlignment(namePlayList, Pos.CENTER_LEFT);
            namePlayList.setMaxWidth(290);
            BorderPane.setMargin(hBoxActions, new Insets(0, 50, 0, 10));
            BorderPane.setMargin(namePlayList, new Insets(0, 10, 0, 50));
            VBox.setMargin(borderPanePrincipalPlayList, new Insets(5, 0, 5, 0));

            tooltipName = new Tooltip(namePlayList.getText());
            Tooltip.install(namePlayList, tooltipName);

            deletePlayList(namePlayList.getText());
        }
    }

    public static void addNewPlayList() {
        rectangle2 = new Rectangle(200, screenHeight, Color.valueOf("#191919"));
        buttonAddPlayList = new Button();
        StackPane.setAlignment(buttonAddPlayList, Pos.TOP_LEFT);
        StackPane.setMargin(buttonAddPlayList, new Insets(20, 0, 0, 20));

        imageAddPlayList = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        imageAddPlayList.setFitHeight(50);
        imageAddPlayList.setFitWidth(50);

        buttonAddPlayList.setGraphic(imageAddPlayList);
        buttonAddPlayList.setStyle("-fx-background-color: transparent;");
        BorderPane.setMargin(buttonAddPlayList,
                new Insets(20, imageAddPlayList.getFitWidth(), 0, imageAddPlayList.getFitWidth()));
        stackPaneAddPlayList = new StackPane(rectangle2, buttonAddPlayList);

        buttonAddPlayList.setOnAction(event -> addPlayList());
        buttonAddPlayList.setCursor(Cursor.HAND);

        tooltipAddPlayList = new Tooltip("Add new playList");
        Tooltip.install(buttonAddPlayList, tooltipAddPlayList);
    }

    public static void addPlayList() {
        TextInputDialog addPlayList = new TextInputDialog();
        addPlayList.setTitle("Add PlayList");
        addPlayList.setHeaderText("Enter the name of the new PlayList");
        addPlayList.setContentText("Name: ");

        Optional<String> name = addPlayList.showAndWait();
        name.ifPresent(namePlayList -> {
            boolean add = true;

            if (namePlayList.length() > 24) {
                Alert error = new Alert(AlertType.INFORMATION);
                error.setTitle("Error");
                error.setHeaderText("Name too long");
                error.setContentText(("Enter a name with a maximum of 25 characters"));
                error.show();
            } else {
                for (PlayList playList : userRegisteredController.getCurrentUser().getplayList()) {
                    add = !playList.getName().equals(namePlayList);
                }
                if (add) {
                    userRegisteredController.addPlayList(namePlayList);
                    Alert confirmation = new Alert(AlertType.INFORMATION);
                    confirmation.setTitle("Confirmation");
                    confirmation.setHeaderText("PLayList '" + namePlayList + "' added correctly");
                    confirmation.show();
                    showPlayListScene();
                } else {
                    Alert error = new Alert(AlertType.INFORMATION);
                    error.setTitle("Error");
                    error.setHeaderText("Existing PlayList");
                    error.setContentText(("Please enter another name"));
                    error.show();
                }
            }
        });
    }

    public static void buttonsActions() {
        Region spacer = new Region();
        spacer.setPrefWidth(100);

        buttonAddMultimedia = new Button();
        imageAddMultimedia = new ImageView(new Image("file:" + "src\\prograIconos\\agregarVerde.png"));
        imageAddMultimedia.setFitHeight(30);
        imageAddMultimedia.setFitWidth(30);
        buttonAddMultimedia.setGraphic(imageAddMultimedia);
        buttonAddMultimedia.setStyle("-fx-background-color: transparent;");

        buttonDeletePlayList = new Button();
        imageDeletePlayList = new ImageView(new Image("file:" + "src\\prograIconos\\borrarRojo.png"));
        imageDeletePlayList.setFitHeight(30);
        imageDeletePlayList.setFitWidth(30);
        buttonDeletePlayList.setGraphic(imageDeletePlayList);
        buttonDeletePlayList.setStyle("-fx-background-color: transparent;");

        hBoxActions = new HBox(spacer, buttonAddMultimedia, buttonDeletePlayList);
        buttonAddMultimedia.setCursor(Cursor.HAND);
        buttonDeletePlayList.setCursor(Cursor.HAND);

        tooltipMultimedia = new Tooltip("Add new movie or serie");
        Tooltip.install(buttonAddMultimedia, tooltipMultimedia);
        tooltipDelete = new Tooltip("Delete playList");
        Tooltip.install(buttonDeletePlayList, tooltipDelete);
    }

    public static void deletePlayList(String playListName) {
        buttonDeletePlayList.setOnAction(event -> {
            Alert delete = new Alert(AlertType.CONFIRMATION);
            delete.setTitle("Delete");
            delete.setHeaderText("Delete playList '" + playListName + "'?");
            delete.setContentText(("Are you sure you want to delete the playList"));

            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            delete.getButtonTypes().setAll(buttonTypeOK, buttonCancel);

            delete.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    if (userRegisteredController.removeplayList(playListName)) {
                        Alert correct = new Alert(AlertType.INFORMATION);
                        correct.setTitle("Delete playList");
                        correct.setHeaderText("PLayList removed '" + playListName + "'");
                        correct.setContentText(("The playList has been successfully deleted"));
                        correct.show();
                        showPlayListScene();
                    } else {
                        Alert error = new Alert(AlertType.INFORMATION);
                        error.setTitle("Error");
                        error.setHeaderText("Some error ocurred");
                        error.setContentText(("Please try again"));
                        error.show();
                    }
                }
            });
        });
    }

    public static void messages() {

        // Add new PlayList
        buttonAddPlayList.setOnMouseEntered(
                event -> tooltipAddPlayList.show(buttonAddPlayList, event.getScreenX(), event.getScreenY() + 10));
        buttonAddPlayList.setOnMouseExited(event -> tooltipAddPlayList.hide());

        // Add multimadia
        buttonAddMultimedia.setOnMouseEntered(
                event -> tooltipMultimedia.show(buttonAddMultimedia, event.getScreenX(), event.getScreenY() + 10));
        buttonAddMultimedia.setOnMouseExited(event -> tooltipMultimedia.hide());

        // Delete playList
        buttonDeletePlayList.setOnMouseEntered(
                event -> tooltipDelete.show(buttonDeletePlayList, event.getScreenX(), event.getScreenY() + 10));
        buttonDeletePlayList.setOnMouseExited(event -> tooltipDelete.hide());

        // PlayList Name
        namePlayList.setOnMouseEntered(
                event -> tooltipName.show(namePlayList, event.getScreenX(), event.getScreenY() + 10));
        namePlayList.setOnMouseExited(event -> tooltipName.hide());
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
