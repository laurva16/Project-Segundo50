package co.edu.uptc.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Payment;
import co.edu.uptc.model.UserRegistered;
import co.edu.uptc.utilities.FileManagement;
import javafx.geometry.HPos;
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
    private static GridPane gridPane, gridPaneErrors;
    private StackPane stackPane;
    private static Button buttonSignUp, buttonReturn;
    private static Label labelTitle, labelNameError, labelLastNameError, labelEmailError, labelPasswordError,
            labelPasswordError2;
    private static TextField textFirstName, textLastName, textEmail;
    private static PasswordField textPassword, textPassword2;
    private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth(),
            screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

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
        labelNameError = new Label();
        labelLastNameError = new Label();
        labelEmailError = new Label();
        labelPasswordError = new Label();
        labelPasswordError2 = new Label();
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
        GridPane.setMargin(buttonSignUp, new Insets(15, 0, 20, 0));
        GridPane.setMargin(buttonReturn, new Insets(15, 0, 20, 0));
    }

    public static void gridPane2() {
        gridPaneErrors.setId("gridpaneErrors");

        labelNameError.getStyleClass().add("error-label");
        labelLastNameError.getStyleClass().add("error-label");
        labelEmailError.getStyleClass().add("error-label");
        labelPasswordError.getStyleClass().add("error-label");
        labelPasswordError2.getStyleClass().add("error-label");

        GridPane.setConstraints(labelNameError, 0, 0);
        GridPane.setConstraints(labelLastNameError, 0, 1);
        GridPane.setConstraints(labelEmailError, 0, 2);
        GridPane.setConstraints(labelPasswordError, 0, 3);
        GridPane.setConstraints(labelPasswordError2, 0, 4);

        textFieldAux();
        gridPaneErrors.setAlignment(Pos.TOP_CENTER);
        gridPaneErrors.setVgap(30);

        GridPane.setHalignment(labelNameError, HPos.LEFT);

        labelNameError.setAlignment(Pos.BASELINE_LEFT);
        // setVisibleFalse();
    }

    public static void textFieldAux() {
        TextField aux = new TextField(), aux1 = new TextField(), aux2 = new TextField(),
                aux3 = new TextField(), aux4 = new TextField();
        aux.setMaxWidth(5);
        aux1.setMaxWidth(5);
        aux2.setMaxWidth(5);
        aux3.setMaxWidth(5);
        aux4.setMaxWidth(5);
        GridPane.setConstraints(aux, 1, 0);
        GridPane.setConstraints(aux1, 1, 1);
        GridPane.setConstraints(aux2, 1, 2);
        GridPane.setConstraints(aux3, 1, 3);
        GridPane.setConstraints(aux4, 1, 4);
        aux.setVisible(false);
        aux1.setVisible(false);
        aux2.setVisible(false);
        aux3.setVisible(false);
        aux4.setVisible(false);

        gridPaneErrors.getChildren().addAll(labelNameError, labelLastNameError, labelEmailError,
                labelPasswordError, labelPasswordError2, aux, aux1, aux2, aux3, aux4);
    }

    public static void setVisibleFalse() {
        labelNameError.setVisible(false);
        labelLastNameError.setVisible(false);
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
            setVisibleFalse();
            // Se deben poner las validadciones para cada label
            Payment newPayment;
            if (allValidation() && (newPayment = paymentSimulation()) != null) {
                userRegisteredController.addUser(textFirstName.getText(), textLastName.getText(), textEmail.getText(),
                        textPassword.getText(), newPayment);
                UserRegistered userRegistered = getUserRegistered();
                //PDF
                FileManagement fm = new FileManagement();
                fm.generatePaymentPdf(userRegistered);
                //
                UserScreen userScreen = new UserScreen();
                userScreen.setUserRegistered(userRegistered);
                userScreen.showMovieScene();
                setVisibleFalse();
            }
        });
    }

    public static Payment paymentSimulation() {
        PaymentScreen ps = new PaymentScreen();
        ps.showPaymentScreen();
        return ps.addPayment();
    }

    public static boolean allValidation() {
        boolean valName = false, valLastName = false, valEmail = false, valPassrord1 = false, valPassrord2 = false;
        if (nameEmptyValidation()) {
            valName = nameCharacterValidation();
        }
        if (lastNameEmptyValidation()) {
            valLastName = lastNameCharacterValidation();
        }
        if (emailEmptyValidation()) {
            if (emailDomainValidation()) {
                valEmail = existingEmaildValidation();
            }
        }
        if (passwordEmptyValidation()) {
            valPassrord1 = passwordStringValidation();
        }
        if (password2EmptyValidation() && passwordStringValidation()) {
            valPassrord2 = passwordSameValidation2();
        }
        if (valName == true && valLastName == true && valEmail == true && valPassrord1 == true
                && valPassrord2 == true) {
            return true;
        }
        return false;
    }

    public static boolean nameCharacterValidation() {
        AdminController adminController = new AdminController();
        if (adminController.validarSinCharacterSpecial(textFirstName.getText())) {
            return true;
        }
        labelNameError.setText(
                "* The name must have only letters                    ");
        labelNameError.setVisible(true);
        return false;
    }

    public static boolean lastNameCharacterValidation() {
        AdminController adminController = new AdminController();
        if (adminController.validarSinCharacterSpecial(textLastName.getText())) {
            return true;
        }
        labelLastNameError.setText(
                "* The last name must have only letters               ");
        labelLastNameError.setVisible(true);
        return false;
    }

    public static boolean nameEmptyValidation() {
        if (emptyValidation(textFirstName, labelNameError,
                "name                                                   ")) {
            return true;
        }
        return false;
    }

    public static boolean lastNameEmptyValidation() {
        if (emptyValidation(textLastName, labelLastNameError,
                "last name                                            ")) {
            return true;
        }
        return false;
    }

    public static boolean emailEmptyValidation() {
        if (emptyValidation(textEmail, labelEmailError,
                "email                                                ")) {
            return true;
        }
        return false;
    }

    public static boolean existingEmaildValidation() {
        if (!userRegisteredController.searchEmail(textEmail.getText())) {
            return true;
        }
        labelEmailError.setText(
                "* This email is already recorded                     ");
        labelEmailError.setVisible(true);
        return false;
    }

    public static boolean emailDomainValidation() {
        if (userRegisteredController.userValidation(textEmail.getText())) {
            return true;
        }
        labelEmailError.setText(
                "* Enter one of the following domains:              \n"
                        + "outlook.com, gmail.com, uptc.edu.co.                 ");
        labelEmailError.setVisible(true);
        return false;
    }

    public static boolean passwordEmptyValidation() {
        if (emptyValidation(textPassword, labelPasswordError,
                "password                                             ")) {
            return true;
        }
        return false;
    }

    public static boolean password2EmptyValidation() {
        if (emptyValidation(textPassword2, labelPasswordError2,
                "password                                             ")) {
            return true;
        }
        return false;
    }

    public static boolean passwordStringValidation() {
        if (userRegisteredController.validatePassword(textPassword.getText()))
            return true;

        labelPasswordError.setText(
                "* The password must have at least one number, one\n"
                        + "upper case, one lower case and one special character.");
        // StackPane.setMargin(gridPaneErrors, new Insets(0, 0, 0, 800));
        labelPasswordError.setVisible(true);
        return false;
    }

    public static boolean passwordSameValidation2() {
        if (userRegisteredController.verifyPassword(textPassword.getText(), textPassword2.getText())) {
            return true;
        }
        labelPasswordError2.setText(
                "* Enter the same password.                           ");
        labelPasswordError2.setVisible(true);
        return false;
    }

    public static boolean emptyValidation(TextField textField, Label label, String data) {
        String empty = "* Enter the ";
        if (!textFirstName.getText().isEmpty() && !textLastName.getText().isEmpty() && !textEmail.getText().isEmpty()
                && !textPassword.getText().isEmpty() && !textPassword2.getText().isEmpty()) {
            return true;
        }
        if (textField.getText().isEmpty()) {
            label.setText(empty + data);
            label.setVisible(true);
            return false;
        }
        return true;
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

        StackPane.setMargin(gridPaneErrors, new Insets(0, 0, 0, 800));
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
