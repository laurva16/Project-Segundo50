package co.edu.uptc.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Payment;
import co.edu.uptc.model.UserRegistered;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class RegisterWindow {

    private static Scene sceneRegister;
    private static UserRegisteredController userRegisteredController;
    private BorderPane root;
    private static VBox vBoxImage;
    private static GridPane gridPane;
    private static GridPane gridPaneErrors;
    private StackPane stackPane;
    private static Button buttonSignUp;
    private static Button buttonReturn;
    private static Label labelTitle;
    private static TextField textFirstName;
    private static TextField textLastName;
    private static TextField textEmail;
    private static PasswordField textPassword;
    private static PasswordField textPassword2;
    private static Label labelEmptyError;
    private static Label labelExixtingEmailError;
    private static Label labelEmailError;
    private static Label labelPasswordError;
    private static Label labelPasswordError2;
    private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public RegisterWindow() {
        userRegisteredController = new UserRegisteredController();
        root = new BorderPane();
        vBoxImage = new VBox();
        gridPane = new GridPane();
        gridPaneErrors = new GridPane();
        stackPane = new StackPane();
        buttonSignUp = new Button("Sign Up");
        buttonReturn = new Button("Return");
        labelTitle = new Label("Register");
        textFirstName = new TextField();
        textLastName = new TextField();
        textEmail = new TextField();
        textPassword = new PasswordField();
        textPassword2 = new PasswordField();
    }

    public static void gridPane1() {
        buttonSignUp.setCursor(Cursor.HAND);
        buttonReturn.setCursor(Cursor.HAND);

        Label labelFirstName = new Label("First name");
        Label labelLastName = new Label("Last name");
        Label labelEmail = new Label("Email");
        Label labelPassword = new Label("Password");
        Label labelPassword2 = new Label("Validation Password");
        labelTitle.setId("title");
        labelTitle.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(labelTitle, javafx.geometry.Pos.TOP_CENTER);
        BorderPane.setMargin(labelTitle, new Insets(30, 0, 30, 0));

        textFirstName.setPromptText("Enter your first name");
        textLastName.setPromptText("Enter your last name");
        textEmail.setPromptText("Enter your email");
        textPassword.setPromptText("Enter your password");
        textPassword2.setPromptText("Retype your password");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(30);
        gridPane.setHgap(100);

        GridPane.setHalignment(buttonSignUp, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(buttonSignUp, javafx.geometry.VPos.CENTER);
        GridPane.setHalignment(buttonReturn, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(buttonReturn, javafx.geometry.VPos.CENTER);

        GridPane.setConstraints(labelFirstName, 0, 0);
        GridPane.setConstraints(labelLastName, 0, 1);
        GridPane.setConstraints(labelEmail, 0, 2);
        GridPane.setConstraints(labelPassword, 0, 3);
        GridPane.setConstraints(labelPassword2, 0, 4);
        GridPane.setConstraints(buttonSignUp, 0, 5);

        GridPane.setConstraints(textFirstName, 1, 0);
        GridPane.setConstraints(textLastName, 1, 1);
        GridPane.setConstraints(textEmail, 1, 2);
        GridPane.setConstraints(textPassword, 1, 3);
        GridPane.setConstraints(textPassword2, 1, 4);
        GridPane.setConstraints(buttonReturn, 1, 5);

        gridPane.getChildren().addAll(labelFirstName, labelLastName, labelEmail, labelPassword, labelPassword2,
                textFirstName, textLastName, textEmail, textPassword, textPassword2, buttonReturn, buttonSignUp);

        //TEMPORAL 
        Button a = new Button();
        a.setOnAction(e -> paymentSimulation());
        gridPane.getChildren().add(a);
        //
        GridPane.setMargin(buttonSignUp, new Insets(15, 0, 20, 0));
        GridPane.setMargin(buttonReturn, new Insets(15, 0, 20, 0));
    }

    public static void gridPane2() {
        gridPaneErrors.setId("gridpaneErrors");

        labelEmptyError = new Label("* Complete all data");
        labelExixtingEmailError = new Label("* This email is already recorded\n");
        labelEmailError = new Label("* Enter one of the following domains: \n"
                + "outlook.com, gmail.com, uptc.edu.co.");
        labelPasswordError = new Label(
                "* The password must have at least one number, one\n"
                        + "upper case, one lower case and one special character.");
        labelPasswordError2 = new Label("* Enter the same password.\n");

        labelEmptyError.getStyleClass().add("error-label");
        labelExixtingEmailError.getStyleClass().add("error-label");
        labelEmailError.getStyleClass().add("error-label");
        labelPasswordError.getStyleClass().add("error-label");
        labelPasswordError2.getStyleClass().add("error-label");

        GridPane.setConstraints(labelEmptyError, 0, 0);
        GridPane.setConstraints(labelEmptyError, 0, 1);
        GridPane.setConstraints(labelExixtingEmailError, 0, 2);
        GridPane.setConstraints(labelEmailError, 0, 2);
        GridPane.setConstraints(labelPasswordError, 0, 3);
        GridPane.setConstraints(labelPasswordError2, 0, 4);

        gridPaneErrors.getChildren().addAll(labelEmptyError, labelExixtingEmailError, labelEmailError,
                labelPasswordError, labelPasswordError2);

        gridPaneErrors.setAlignment(Pos.TOP_RIGHT);
        GridPane.setMargin(labelEmptyError, new Insets(0, 0, 45, 0));
        GridPane.setMargin(labelPasswordError, new Insets(0, 50, 0, 0));
        gridPaneErrors.setVgap(30);
        setVisibleFalse();
    }

    public static void setVisibleFalse() {
        labelEmptyError.setVisible(false);
        labelExixtingEmailError.setVisible(false);
        labelEmailError.setVisible(false);
        labelPasswordError.setVisible(false);
        labelPasswordError2.setVisible(false);
    }

    public static void image() {
        try {
            Image logo = new Image(new FileInputStream("src\\prograIconos\\cinema.jpeg"));
            ImageView image = new ImageView(logo);
            vBoxImage.getChildren().add(image);
            vBoxImage.setAlignment(Pos.CENTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    public static void returnButton() {
        LogInWindow login = new LogInWindow();
        buttonReturn.setOnAction(e -> login.showSceneLoginView());
    }

    public static void signUp() {
        buttonSignUp.setOnAction(event -> {
            if (emptyValidation() && existingEmaildValidation() && emailValidation() && passwordValidation()
                    && passwordValidation2()) {

                userRegisteredController.addUser(textFirstName.getText(), textLastName.getText(), textEmail.getText(),
                       textPassword.getText(), paymentSimulation());
  
                UserRegistered userRegistered = getUserRegistered();
                UserScreen userScreen = new UserScreen();
                userScreen.setUserRegistered(userRegistered);
                userScreen.showMovieScene();
                setVisibleFalse();
            }
        });
    }
    
    public static Payment paymentSimulation(){
        PaymentScreen ps = new PaymentScreen();
        ps.showPaymentScreen();
        return ps.addPayment();
    }

    public static boolean emptyValidation() {
        if (!textFirstName.getText().isEmpty() && !textLastName.getText().isEmpty() && !textEmail.getText().isEmpty()
                && !textPassword.getText().isEmpty() && !textPassword2.getText().isEmpty()) {
            return true;
        }
        setVisibleFalse();
        labelEmptyError.setVisible(true);
        return false;
    }

    public static boolean existingEmaildValidation() {
        if (!userRegisteredController.searchEmail(textEmail.getText())) {
            return true;
        }
        setVisibleFalse();
        labelExixtingEmailError.setVisible(true);
        return false;
    }

    public static boolean emailValidation() {
        if (userRegisteredController.userValidation(textEmail.getText())) {
            return true;
        }
        setVisibleFalse();
        labelEmailError.setVisible(true);
        return false;
    }

    public static boolean passwordValidation() {
        if (userRegisteredController.validatePassword(textPassword.getText())) {
            return true;
        }
        setVisibleFalse();
        labelPasswordError.setVisible(true);
        return false;
    }

    public static boolean passwordValidation2() {
        if (userRegisteredController.verifyPassword(textPassword.getText(), textPassword2.getText())) {
            return true;
        }
        setVisibleFalse();
        labelPasswordError2.setVisible(true);
        return false;
    }

    public static UserRegistered getUserRegistered() {
        userRegisteredController.setCurrentUser(textEmail.getText());
        return userRegisteredController.getCurrentUser();
    }

    public void showSceneRegister() {
        gridPane1();
        gridPane2();
        image();
        signUp();
        returnButton();

        stackPane.getChildren().addAll(gridPaneErrors, gridPane);
        root.setTop(labelTitle);
        root.setCenter(stackPane);
        root.setBottom(vBoxImage);

        sceneRegister = new Scene(root, screenWidth, screenHeight);
        sceneRegister.getStylesheets().add(new File("src\\main\\java\\co\\styles\\register.css").toURI().toString());
    }

    public Scene getScene() {
        showSceneRegister();
        return sceneRegister;
    }
}
