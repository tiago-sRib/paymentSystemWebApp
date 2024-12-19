package pageController;

import account.*;
import db.User_db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.Const;

import java.io.IOException;
import java.util.Objects;

import static util.Const.*;

public class Home extends GenericPage {

    @FXML Label accountTypeInfo;
    @FXML Label welcomeText;
    private Wallet wallet;
    private Transfers transfers;
    private Profile profile;
    private User_db user_db = new User_db();

    public void setPage(User user){
        setUserInfo(user);
        welcomeText.setText("Welcome, " + user.name);
        accountTypeInfo.setText("You are using a " + user.type + " Account");
    }

    public void setPage(Home home){
        setUserInfo(user_db.querySQLfromEmail(home.user.email));
        welcomeText.setText("Welcome, " + home.user.name);
        accountTypeInfo.setText("You are using a " + home.user.type + " Account");
    }

    @FXML private void initialize(){
    }

    @FXML private void goToWallet(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pages/wallet.fxml"));
        root = loader.load();
        wallet = loader.getController();
        wallet.injectMain(this);
        wallet.setPage();

        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void goToTransfers(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pages/transfers.fxml"));
        root = loader.load();
        transfers = loader.getController();
        transfers.injectMain(this);
        transfers.setPage();

        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void goToProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pages/profile.fxml"));
        root = loader.load();
        profile = loader.getController();
        profile.injectMain(this);
        profile.setPage();

        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void logout(ActionEvent actionEvent) throws IOException {
        // implement close connection

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, Const.WIDTH, Const.HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private Button profileButton;

    @FXML private void goToProfileOnMouseEntered(MouseEvent actionEvent) {
        profileButton.setStyle("-fx-background-radius: 90");
    }

    @FXML private void goToProfileOnMouseExited(MouseEvent actionEvent) {
        profileButton.setStyle("-fx-background-radius: 90");
    }
}
