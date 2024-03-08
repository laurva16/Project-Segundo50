package co.edu.uptc.run;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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

    Label warningNameCard;
    Label warningCardNumber;
    Label warningSecurityCode;
    Label warningExpirationDate;

    Label labelnameCard;
    Label labelCardNumber;
    Label labelSecurityCode;
    Label labelexpirationDate;

    ComboBox<Integer> yearComboBox;
    ComboBox<String> monthComboBox;

    public void showPaymentScreen() {
        Stage secundaryStage = new Stage();
        secundaryStage.initModality(Modality.APPLICATION_MODAL);
        secundaryStage.setTitle("Payment Details");

        VBox box = new VBox();

        labelnameCard = new Label("Name on card: ");
        labelCardNumber = new Label("Credit card number: ");
        labelSecurityCode = new Label("Security code");
        labelexpirationDate = new Label("Expiration date");

        warningNameCard = new Label("* error1");
        warningCardNumber = new Label("* error2");
        warningSecurityCode = new Label("* error3");
        warningExpirationDate = new Label("* error4-5");

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
        acceptButton.setOnAction(event -> secundaryStage.close());
        acceptButton.setId("button");

        HBox boxButton = new HBox(acceptButton, closeButton);
        HBox warningBox = new HBox(warningSecurityCode, warningExpirationDate);
        warningExpirationDate.setTranslateX(167);

        box.getChildren().addAll(labelnameCard, nameCard, warningNameCard, labelCardNumber, cardNumber,
                warningCardNumber, labelBox, textBox, warningBox, boxButton);

        Scene paymentScene = new Scene(box, 530, 430);
        // CSS
        paymentScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\payment.css").toURI().toString());
        box.setId("box");
        closeButton.setId("closebutton");
        acceptButton.setId("acceptbutton");
        warningNameCard.setId("warning");
        warningCardNumber.setId("warning");
        warningSecurityCode.setId("warning");
        warningExpirationDate.setId("warning");
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

}
