package authenticationController;

import account.User;
import javafx.scene.paint.Color;
import pageController.Home;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

import static util.Const.*;

public class Login {
    @FXML public TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Label logMessage;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private void tryLogin(ActionEvent actionEvent) throws IOException {
        String email = emailField.getText();
        String pass = passField.getText();

        if(email.isEmpty() || pass.isEmpty()){
            logMessage.setTextFill(Color.RED);
            logMessage.setText("You must fulfill everything");
            return;
        }

        User user = new User();
        user = user.Login(emailField.getText(), passField.getText());
        if(user == null){
            logMessage.setTextFill(Color.RED);
            logMessage.setText("This user does not exist");
            return;
        }
        logMessage.setTextFill(Color.RED);
        switch (user.logERROR){
            case e_SUCCESS -> goToHome(actionEvent, user);
            case e_WRONG_CREDENTIALS -> logMessage.setText("Invalid credentials");
        }
    }

    @FXML private void SignupPage(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/signup.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    private void goToHome(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pages/home.fxml"));
        root = loader.load();
        Home homeControl = loader.getController();
        homeControl.setPage(user);

        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.show();
    }
}