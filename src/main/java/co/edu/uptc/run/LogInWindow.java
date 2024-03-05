package co.edu.uptc.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Admin;
import co.edu.uptc.model.UserRegistered;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LogInWindow extends Application {

    private static Stage primaryStage;
    private static Scene sceneLogIn;
    private static AdminController adminController;
    private static UserRegisteredController userRegisteredController;
    private UserRegistered userRegistered;
    private static Admin admin;
    private static BorderPane root;
    private static VBox vBoxImage;
    private static GridPane gridPane;
    private static GridPane gridPaneErrors;
    private static StackPane stackPane;
    private static Button buttonSignIn;
    private static Button buttonRegister;
    private static Button buttonVisitor;
    private static StackPane stackPaneTop;
    private static Label labelTitle;
    private static TextField textEmail;
    private static PasswordField textPassword;
    private static Label labelEmailError;
    private static Label labelPasswordError;
    private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public LogInWindow() {
        adminController = new AdminController();
        userRegisteredController = new UserRegisteredController();
        userRegistered = new UserRegistered();
        admin = new Admin();
        root = new BorderPane();
        vBoxImage = new VBox();
        gridPane = new GridPane();
        gridPaneErrors = new GridPane();
        stackPane = new StackPane();
        stackPaneTop = new StackPane();
        buttonSignIn = new Button("Sign In");
        buttonRegister = new Button("Register");
        buttonVisitor = new Button("Visitor");
        labelTitle = new Label("Sign In");
        textEmail = new TextField();
        textPassword = new PasswordField();
    }

    public static void top() {
        labelTitle.setId("title");
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();

        hBox1.getChildren().add(labelTitle);
        hBox2.getChildren().add(buttonVisitor);
        stackPaneTop.getChildren().addAll(hBox1, hBox2);

        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.TOP_RIGHT);

        StackPane.setMargin(hBox1, new Insets(50, 0, 0, 0));
        StackPane.setMargin(hBox2, new Insets(5, 5, 0, 0));
        BorderPane.setMargin(stackPaneTop, new Insets(0, 0, 50, 0));
    }

    public static void gridPane1() {
        buttonSignIn.setCursor(Cursor.HAND);
        buttonRegister.setCursor(Cursor.HAND);

        Label labelEmail = new Label("Email");
        Label labelPassword = new Label("Password");

        textEmail.setPromptText("Enter your email");
        textPassword.setPromptText("Enter your password");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(30);
        gridPane.setHgap(100);

        GridPane.setHalignment(buttonSignIn, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(buttonSignIn, javafx.geometry.VPos.CENTER);

        GridPane.setHalignment(buttonRegister, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(buttonRegister, javafx.geometry.VPos.CENTER);

        GridPane.setHalignment(labelEmail, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(labelEmail, javafx.geometry.VPos.CENTER);

        GridPane.setHalignment(labelPassword, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(labelPassword, javafx.geometry.VPos.CENTER);

        GridPane.setConstraints(labelEmail, 0, 0);
        GridPane.setConstraints(labelPassword, 0, 1);
        GridPane.setConstraints(buttonSignIn, 0, 2);

        GridPane.setConstraints(textEmail, 1, 0);
        GridPane.setConstraints(textPassword, 1, 1);
        GridPane.setConstraints(buttonRegister, 1, 2);

        gridPane.getChildren().addAll(labelEmail, labelPassword, textEmail, textPassword, buttonRegister, buttonSignIn);

        GridPane.setMargin(buttonSignIn, new Insets(30, 0, 50, 0));
        GridPane.setMargin(buttonRegister, new Insets(30, 0, 50, 0));
    }

    public static void gridPane2() {
        gridPaneErrors.setId("gridpaneErrors");

        labelEmailError = new Label("* Email does not exist");
        labelPasswordError = new Label("* Incorrect password");

        labelEmailError.getStyleClass().add("error-label");
        labelPasswordError.getStyleClass().add("error-label");

        GridPane.setConstraints(labelEmailError, 0, 0);
        GridPane.setConstraints(labelPasswordError, 0, 1);

        gridPaneErrors.getChildren().addAll(labelEmailError,
                labelPasswordError);

        gridPaneErrors.setAlignment(Pos.TOP_RIGHT);
        GridPane.setMargin(labelPasswordError, new Insets(20, 270, 0, 0));
        gridPaneErrors.setVgap(30);
        setVisibleFalse();
    }

    public static void setVisibleFalse() {
        labelEmailError.setVisible(false);
        labelPasswordError.setVisible(false);
    }

    public static void image() {
        try {
            Image logo = new Image(new FileInputStream("src\\prograIconos\\cinema.jpeg"));
            ImageView image = new ImageView(logo);
            vBoxImage.getChildren().addAll(image);
            vBoxImage.setAlignment(Pos.CENTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void buttonVisitor() {
        buttonVisitor.setOnAction(event -> showSceneVisitor());
    }

    public static void signIn() {
        buttonSignIn.setOnAction(event -> {
            if (emailValidationAdmin() && passwordValidationAdmin()) {
                EntryWindow entryWindow = new EntryWindow();
                entryWindow.showMovieScene();
                setVisibleFalse();
            } else if (emailValidationUser() && passwordValidationUser()) {
                UserRegistered userRegistered = getUserRegistered();
                UserScreen userScreen = new UserScreen();
                userScreen.setUserRegistered(userRegistered);
                userScreen.showMovieScene();
                setVisibleFalse();
            }
        });
    }

    public static void buttonRegister() {
        buttonRegister.setOnAction(e -> showSceneRegister());
    }

    public static boolean emailValidationAdmin() {
        if (adminController.getAdmin().getUser().equals(textEmail.getText())) {
            return true;
        }
        setVisibleFalse();
        labelEmailError.setVisible(true);
        return false;
    }

    public static boolean passwordValidationAdmin() {
        if (adminController.getAdmin().getPassword().equals(textPassword.getText())) {
            return true;
        }
        setVisibleFalse();
        labelPasswordError.setVisible(true);
        return false;
    }

    public static boolean emailValidationUser() {
        if (userRegisteredController.userFound(textEmail.getText())) {
            return true;
        }
        setVisibleFalse();
        labelEmailError.setVisible(true);
        return false;
    }

    public static boolean passwordValidationUser() {
        if (userRegisteredController.couldLogIn(textEmail.getText(), textPassword.getText())) {
            return true;
        }
        setVisibleFalse();
        labelPasswordError.setVisible(true);
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LogInWindow.primaryStage = primaryStage;
        showSceneLoginView();
    }

    public static void showSceneLoginView() {
        gridPane1();
        gridPane2();
        top();
        image();
        signIn();
        buttonRegister();
        buttonVisitor();

        stackPane.getChildren().addAll(gridPaneErrors, gridPane);
        root.setTop(stackPaneTop);
        root.setCenter(stackPane);
        root.setBottom(vBoxImage);

        sceneLogIn = new Scene(root, screenWidth, screenHeight);
        sceneLogIn.getStylesheets().add(new File("src\\main\\java\\co\\styles\\register.css").toURI().toString());
        primaryStage.setScene(sceneLogIn);
        primaryStage.setTitle("Register");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void showSceneRegister() {
        RegisterWindow registerWindow = new RegisterWindow();
        primaryStage.setScene(registerWindow.getScene());
        primaryStage.setTitle("Register");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void showSceneVisitor() {
        VisitorScreen visitorScreen = new VisitorScreen();
        visitorScreen.showMovieScene();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getSceneLogIn() {
        showSceneLoginView();
        return sceneLogIn;
    }

    public static UserRegisteredController getUserRegisteredController() {
        return userRegisteredController;
    }

    public static UserRegistered getUserRegistered() {
        userRegisteredController.setCurrentUser(textEmail.getText());
        return userRegisteredController.getCurrentUser();
    }

    public void setUserRegistered(UserRegistered userRegistered) {
        this.userRegistered = userRegistered;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
