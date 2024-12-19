package authenticationController;

import account.Commercial;
import account.Personal;
import account.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pageController.Home;
import util.Const;
import util.Macros;

import java.io.IOException;
import java.util.Objects;

import static util.Const.*;

public class Signup {
    ObservableList<String> accTypeList = FXCollections.observableArrayList("Personal", "Commercial");
    @FXML private TextField emailField;
    @FXML private TextField cardField;
    @FXML private TextField nameField;
    @FXML private PasswordField passField;
    @FXML private ComboBox<String> accType;
    @FXML private Label logMessage;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private void initialize (){
        accType.setItems(FXCollections.observableArrayList("Personal", "Commercial"));
        cardField.addEventFilter(KeyEvent.KEY_TYPED, Macros.numeric_Validation(16));
        nameField.addEventFilter(KeyEvent.KEY_TYPED, Macros.letter_Validation(16));
    }

    @FXML private void trySignup(ActionEvent actionEvent) throws IOException {
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = passField.getText();
        String type = accType.getValue();
        String card = cardField.getText();


        User user = new User();
        int log = user.Signup(name, email, pass, card, type);
        user = user.setUser(name, email, type);

        logMessage.setTextFill(Color.RED);
        switch (log){
            case e_INVALID_CREDIT_CARD -> logMessage.setText("Invalid credit card");
            case e_INVALID_EMAIL -> logMessage.setText("Please insert a valid email: example@test.com");
            case e_USER_ALREADY_EXISTS -> logMessage.setText("User already exists");
            case e_EMPTY_FIELDS -> logMessage.setText("You must fulfill everything");
            case e_SIGNUP_SUCCESSFUL -> logMessage.setText("Your are now member of PayPobre! Please login");
        }
        if(log == e_SIGNUP_SUCCESSFUL) logMessage.setTextFill(Color.GREEN);
    }

    @FXML private void backToLogin(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, Const.WIDTH, Const.HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}