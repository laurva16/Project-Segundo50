package co.edu.uptc.run;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaymentScreen {
    TextField nameCard;
    TextField cardNumber;
    TextField securityCode;

    Label labelnameCard;
    Label labelCardNumber;
    Label labelSecurityCode;
    Label labelexpirationDate;
    Label labelWarning = new Label();

    ComboBox<Integer> yearComboBox;
    ComboBox<String> monthComboBox;
    AdminController ac = new AdminController();

    Stage secundaryStage;
    public void showPaymentScreen() {
        secundaryStage = new Stage();
        secundaryStage.initModality(Modality.APPLICATION_MODAL);
        secundaryStage.setTitle("Payment Details");

        VBox box = new VBox();

        labelnameCard = new Label("Name on card: ");
        labelCardNumber = new Label("Credit card number: ");
        labelSecurityCode = new Label("Security code");
        labelexpirationDate = new Label("Expiration date");

        nameCard = new TextField();
        cardNumber = new TextField();
        securityCode = new TextField();

        nameCard.setMaxWidth(415);
        cardNumber.setMaxWidth(415);
        securityCode.setMaxWidth(415);
        loadDateChoiseBox();

        yearComboBox.setVisibleRowCount(5);
        monthComboBox.setVisibleRowCount(5);
        box.setSpacing(10);
        box.setTranslateX(50);
        box.setTranslateY(50);
        box.setAlignment(Pos.TOP_LEFT);

        HBox labelBox = new HBox(labelSecurityCode, labelexpirationDate);
        HBox textBox = new HBox(securityCode, yearComboBox, monthComboBox);

        labelexpirationDate.setTranslateX(97);
        labelBox.setSpacing(10);
        textBox.setSpacing(10);

        Button closeButton = new Button();
        closeButton.setTranslateY(50);
        closeButton.setTranslateX(215);
        closeButton.setText("Return");
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> secundaryStage.close());
        closeButton.setId("button");

        Button acceptButton = new Button();
        acceptButton.setTranslateY(50);
        acceptButton.setText("Finish");
        acceptButton.setPrefWidth(100);
        acceptButton.setOnAction(event -> validationPayment());
        acceptButton.setId("button");

        HBox boxButton = new HBox(acceptButton, closeButton);

        box.getChildren().addAll(labelnameCard, nameCard, labelCardNumber, cardNumber,
                labelBox, textBox, labelWarning, boxButton);

        Scene paymentScene = new Scene(box, 530, 430);
        // CSS
        paymentScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\payment.css").toURI().toString());
        box.setId("box");
        closeButton.setId("closebutton");
        acceptButton.setId("acceptbutton");
        //
        secundaryStage.setScene(paymentScene);
        secundaryStage.showAndWait();
    }

    public void loadDateChoiseBox() {
        // months
        List<String> monthList = Arrays.asList("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        monthComboBox = new ComboBox<>(FXCollections.observableArrayList(monthList));

        // years
        int actual = java.time.Year.now().getValue();
        ObservableList<Integer> yearsList = FXCollections.observableArrayList(
                IntStream.rangeClosed(1900, actual).boxed().toArray(Integer[]::new));

        Collections.reverse(yearsList);
        yearComboBox = new ComboBox<>(yearsList);
    }

    public Payment addPayment() {
        UserRegisteredController urc = new UserRegisteredController();
        return urc.addPayment(nameCard.getText(), Integer.parseInt(cardNumber.getText()), securityCode.getText(),
                Integer.parseInt(yearComboBox.getValue().toString()), monthComboBox.getValue());
    }

    public void validationPayment() {
        if (nameCard.getText().isBlank() && cardNumber.getText().isBlank() && securityCode.getText().isBlank()
                && yearComboBox.getValue() == null && monthComboBox.getValue() == null) {
            ac.showErrorTimeline(nameCard, labelWarning, "* All fields must be filled!");
            ac.showErrorTimeline(cardNumber, labelWarning, "* All fields must be filled!");
            ac.showErrorTimeline(securityCode, labelWarning, "* All fields must be filled!");
            ac.showErrorTimelineIntComboBox(yearComboBox, labelWarning, "* All fields must be filled!");
            ac.showErrorTimelineStringComboBox(monthComboBox, labelWarning, "* All fields must be filled!");
            return;

        } else if (nameCard.getText().isBlank() || !ac.validateName(nameCard.getText()) 
                || !ac.validarSinCharacterSpecial(nameCard.getText())) {
            ac.showErrorTimeline(nameCard, labelWarning,
                    "* Invalid name on card. Min. 6 letters, no numbers or special characters.");
            return;

        } else if (cardNumber.getText().isBlank() || !ac.validateNumbers(cardNumber.getText()) || ac.validateNumbersDigits(cardNumber.getText())) {
            ac.showErrorTimeline(cardNumber, labelWarning,
                    "* Invalid card number. Min. 6 digits, no characters.");
            return;
        } else if (securityCode.getText().isBlank() || !ac.validateDescription(securityCode.getText())
                || !ac.validarSinCharacterSpecial(nameCard.getText())) {
            ac.showErrorTimeline(securityCode, labelWarning,
                    "* Invalid security code. Min. 5 characters.");
            return;
        } else if (yearComboBox.getValue() == null) {
            ac.showErrorTimelineIntComboBox(yearComboBox, labelWarning, "* Select the expiration year.");
            return;
        } else if (monthComboBox.getValue() == null) {
            ac.showErrorTimelineStringComboBox(monthComboBox, labelWarning, "* Select the expiration  month.");
            return;
        }
        secundaryStage.close();
    }
}
