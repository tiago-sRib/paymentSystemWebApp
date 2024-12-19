package application;

import db.User_db;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.Const;

import java.io.IOException;
import java.util.Objects;

import static javafx.application.Platform.exit;

public class RunApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try{
            //database
            User_db db = new User_db();
            String log = db.connect();

            if(log.equals("The connection attempt failed.")){
                System.out.println(log);
                exit();
                return;
            }
        }
        catch (Exception e){
            System.out.println("We couldn't connect to our database :(");
            return;
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
        Scene scene = new Scene(root, Const.WIDTH, Const.HEIGHT);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/icon/icon.png")).toString()));
        stage.setTitle("PayPobre");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}