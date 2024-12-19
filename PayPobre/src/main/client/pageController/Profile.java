package pageController;

import account.*;
import db.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import util.Macros;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import static util.Const.*;

public class Profile extends GenericSubPage {

    @FXML private Label logMessagePass;
    @FXML private PasswordField newPass;
    @FXML private PasswordField oldPass;
    @FXML private PasswordField confirmPass;
    @FXML private Label logMessageCard;
    @FXML private PasswordField passCardField;
    @FXML private TextField cardField;
    @FXML private Label name;
    @FXML private Label email;
    @FXML private Label memberDate;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passProfileField;
    @FXML private Label logMessageProfile;
    @FXML private Label header;
    private User_db user_db;

    @FXML private void initialize(){
        upProfilePopup.setVisible(false);
        changeCardPopup.setVisible(false);
        changePassPopup.setVisible(false);
        aboutUsPopup.setVisible(false);
        helpPopup.setVisible(false);

        logMessageCard.setText("");
        logMessagePass.setText("");
        logMessageProfile.setText("");

        cardField.addEventFilter(KeyEvent.KEY_TYPED, Macros.numeric_Validation(16));
    }

    @FXML private void backHome(ActionEvent actionEvent) throws IOException {
        goToHome(actionEvent);
    }

    public void setPage() {
        header.setText(home.user.name);
        name.setText(home.user.name);
        email.setText(home.user.email);
        user_db = new User_db();
        home.user.created_on = user_db.querySQLfromID(home.user.user_id).created_on;
        memberDate.setText("Member since " + home.user.created_on);
    }

    @FXML private Button upProfile;
    @FXML private Button changeCard;
    @FXML private Button changePass;
    @FXML private HBox upProfilePopup;
    @FXML private HBox changeCardPopup;
    @FXML private HBox changePassPopup;
    @FXML private HBox aboutUsPopup;
    @FXML private HBox helpPopup;


    @FXML private void upProfileOnMouseEntered(MouseEvent actionEvent) {
        upProfile.setStyle("-fx-background-color: whitesmoke");
    }

    @FXML private void upProfileOnMouseExited(MouseEvent actionEvent) {
        upProfile.setStyle("-fx-background-color: transparent");
    }

    @FXML private void changeCardOnMouseEntered(MouseEvent actionEvent) {
        changeCard.setStyle("-fx-background-color: whitesmoke");
    }

    @FXML private void changeCardOnMouseExited(MouseEvent actionEvent) {
        changeCard.setStyle("-fx-background-color: transparent");
    }
    @FXML private void changePassOnMouseEntered(MouseEvent actionEvent) {
        changePass.setStyle("-fx-background-color: whitesmoke");
    }

    @FXML private void changePassOnMouseExited(MouseEvent actionEvent) {
        changePass.setStyle("-fx-background-color: transparent");
    }

    @FXML private void upProfileOnAction(ActionEvent actionEvent) {
        upProfilePopup.setVisible(true);
        passProfileField.clear();
        emailField.clear();
        nameField.clear();
        logMessageProfile.setText("");
    }

    @FXML private void applyUpProfile(ActionEvent actionEvent) {
        logMessageProfile.setTextFill(Color.BLACK);
        logMessageProfile.setText("");
        if(passProfileField.getText().isEmpty() && nameField.getText().isEmpty() && emailField.getText().isEmpty()){
            logMessageProfile.setText("Please, insert your Password.\nUpdate at least one element");
            logMessageProfile.setTextFill(Color.RED);
            return;
        }

        if(nameField.getText().isEmpty() && emailField.getText().isEmpty()){
            logMessageProfile.setText("Please, update at least\none element");
            logMessageProfile.setTextFill(Color.RED);
            return;
        }

        int eName, eEmail;
        String logName = "", logEmail = "", log;
        eName = home.user.changeName(passProfileField.getText(), nameField.getText());
        eEmail = home.user.changeEmail(passProfileField.getText(), emailField.getText());

        switch (eName){
            case e_SKIP_CHANGE_NAME -> logName = "";
            case e_WRONG_CREDENTIALS -> logMessageProfile.setText("Wrong Password");
            case e_SUCCESS -> logName = "Name updated successfully";
        }
        switch (eEmail){
            case e_SKIP_CHANGE_EMAIl -> logEmail = "";
            case e_WRONG_CREDENTIALS -> logMessageProfile.setText("Wrong Password");
            case e_SUCCESS -> logEmail = "Email updated successfully";
            case e_INVALID_EMAIL -> logEmail = "Please insert a valid email format";
        }

        log = logMessageProfile.getText();
        logMessageProfile.setText(log + logName + '\n' + logEmail);

        if(eName == e_SUCCESS){
            home.user.name = nameField.getText();
            header.setText(nameField.getText());
            name.setText(nameField.getText());
        }
        if(eEmail == e_SUCCESS){
            home.user.email = emailField.getText();
            email.setText(emailField.getText());
        }

        if(eEmail == e_SUCCESS && eName == e_SUCCESS) logMessageProfile.setTextFill(Color.GREEN);
    }

    @FXML private void closeUpProfile(ActionEvent actionEvent) {
        upProfilePopup.setVisible(false);
        passProfileField.clear();
        emailField.clear();
        nameField.clear();
        logMessageProfile.setText("");
    }

    @FXML private void changeCardOnAction(ActionEvent actionEvent) {
        changeCardPopup.setVisible(true);
        cardField.clear();
        passCardField.clear();
        logMessageCard.setText("");
    }

    @FXML private void applyChangeCard(ActionEvent actionEvent) {
        logMessageCard.setTextFill(Color.RED);
        if(cardField.getText().isEmpty() || passCardField.getText().isEmpty()){
            logMessageCard.setText("Please, you must fulfill every field");
            return;
        }

        int log = home.user.changeCard(passCardField.getText(), cardField.getText());

        switch (log){
            case e_INVALID_CREDIT_CARD -> logMessageCard.setText("This credit card is invalid");
            case e_WRONG_CREDENTIALS -> logMessageCard.setText("Wrong password");
            case e_SUCCESS -> logMessageCard.setText("Credit card updated successfully");
        }

        if(log == e_SUCCESS) logMessageCard.setTextFill(Color.GREEN);
    }

    @FXML private void closeChangeCard(ActionEvent actionEvent) {
        changeCardPopup.setVisible(false);
        cardField.clear();
        passCardField.clear();
        logMessageCard.setText("");
    }

    @FXML private void changePassOnAction(ActionEvent actionEvent) {
        changePassPopup.setVisible(true);
        logMessagePass.setText("");
        newPass.clear();
        oldPass.clear();
        confirmPass.clear();
    }

    @FXML private void applyChangePass(ActionEvent actionEvent) {
        logMessagePass.setTextFill(Color.RED);
        if(confirmPass.getText().isEmpty() || oldPass.getText().isEmpty() || newPass.getText().isEmpty()){
            logMessagePass.setText("Please, you must fulfill every field");
            return;
        }

        if(!newPass.getText().equals(confirmPass.getText())){
            logMessagePass.setText("Passwords don't match");
            confirmPass.clear();
            return;
        }

        if(home.user.changePass(oldPass.getText(), newPass.getText())){
            logMessagePass.setText("Password Updated");
            logMessagePass.setTextFill(Color.GREEN);
        }

        else logMessagePass.setText("Old password is incorrect");
    }

    @FXML private void closeChangePass(ActionEvent actionEvent) {
        changePassPopup.setVisible(false);
        logMessagePass.setText("");
        newPass.clear();
        oldPass.clear();
        confirmPass.clear();
    }

    @FXML private void aboutUsOnAction(ActionEvent actionEvent) {
        aboutUsPopup.setVisible(true);
    }

    @FXML private void closeAboutUs(ActionEvent actionEvent) {
        aboutUsPopup.setVisible(false);
    }

    @FXML private void helpOnAction(ActionEvent actionEvent) {
        helpPopup.setVisible(true);
    }

    @FXML private void closeHelp(ActionEvent actionEvent) {
        helpPopup.setVisible(false);
    }
}
