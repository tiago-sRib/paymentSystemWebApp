package pageController;

import account.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class GenericPage {
    protected User user = new User();

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    public void setUserInfo(User user){
        this.user = user;
    }

    public void setUserInfo(Home home){
        this.user = home.user;
    }
}
