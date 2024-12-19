package pageController;

import account.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Macros;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import wallet.CreditCardValidation;

import java.io.DataOutput;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Objects;

import static util.Const.*;

public class Wallet extends GenericSubPage {
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private String balance;
    private Image img;

    @FXML private ImageView cardVector;
    @FXML private Label logMessage;
    @FXML private TextField withdrawAmount;
    @FXML private TextField depositAmount;
    @FXML private Label cardBalance;
    @FXML private Label cardNumber;
    @FXML private Label header;

    public void setPage(){
        int prefixCard = 0;

        logMessage.setText("");
        balance = formatter.format(home.user.wallet.money);
        header.setText(home.user.name);
        cardBalance.setText("Balance: " + balance);
        cardNumber.setText("**** **** **** " + home.user.wallet.card.substring(home.user.wallet.card.length() - 4));
        prefixCard = CreditCardValidation.prefixCheck(home.user.wallet.card);

        switch (prefixCard){
            case prefix_VISA -> img = new Image(Objects.requireNonNull(getClass().getResource("/images/card/" + "visa.png")).toString());
            case prefix_MASTER ->  img = new Image(Objects.requireNonNull(getClass().getResource("/images/card/" + "master.png")).toString());
            case prefix_DISCOVER ->  img = new Image(Objects.requireNonNull(getClass().getResource("/images/card/" + "discover.png")).toString());
            case prefix_AMERICAN_EXPRESS ->  img = new Image(Objects.requireNonNull(getClass().getResource("/images/card/" + "express.png")).toString());
        }
        cardVector.setImage(img);
    }

    @FXML private void backHome(ActionEvent actionEvent) throws IOException {
        goToHome(actionEvent);
    }

    @FXML private void initialize(){
        depositAmount.addEventFilter(KeyEvent.KEY_TYPED, Macros.numeric_Validation(10));
        withdrawAmount.addEventFilter(KeyEvent.KEY_TYPED, Macros.numeric_Validation(10));
    }

    @FXML private void deposit(ActionEvent actionEvent) {
        if (depositAmount.getText().isEmpty()) {
            logMessage.setText("This field is empty, please insert an amount");
            logMessage.setTextFill(Color.RED);
        }

        double amount = 0.0;
        try {
            amount = Double.parseDouble(depositAmount.getText());
        } catch (NumberFormatException e) {
            logMessage.setText("Please insert a valid amount to deposit");
            logMessage.setTextFill(Color.RED);
            depositAmount.clear();
            return;
        }

        if(!home.user.wallet.deposit(amount, home.user)){
            logMessage.setText("Something went wrong");
            logMessage.setTextFill(Color.RED);
            return;
        }
        depositAmount.clear();
        logMessage.setText("You deposited " + formatter.format(amount) + " successfully!");
        logMessage.setTextFill(Color.GREEN);
        balance = formatter.format(home.user.wallet.money);

        cardBalance.setText("Balance: " + balance);
    }

    @FXML private void withdraw(ActionEvent actionEvent) {
        if (withdrawAmount.getText().isEmpty()) {
            logMessage.setText("This field is empty, please insert an amount");
            logMessage.setTextFill(Color.RED);
            return;
        }

        double amount = 0.0;
        try {
            amount = Double.parseDouble(withdrawAmount.getText());

        } catch (NumberFormatException e) {
            logMessage.setText("Please insert a valid amount to withdraw");
            logMessage.setTextFill(Color.RED);
            withdrawAmount.clear();
            return;
        }

        if(!home.user.wallet.withdraw(amount, home.user)){
            logMessage.setText("You don´t have enough money in your\n account to perform this operation");
            logMessage.setTextFill(Color.RED);
            return;
        }

        withdrawAmount.clear();
        logMessage.setTextFill(Color.GREEN);
        logMessage.setText("You withdrew " + formatter.format(amount) + " successfully");
        balance = formatter.format(home.user.wallet.money);
        cardBalance.setText("Balance: " + balance);
    }

    @FXML private void goToProfile(ActionEvent actionEvent) {
        logMessage.setText("This feature isn´t implemented yet");
        logMessage.setTextFill(Color.RED);
    }
}
