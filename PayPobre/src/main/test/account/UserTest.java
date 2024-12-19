package account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static util.Const.PERSONAL;
import static util.Const.e_SIGNUP_SUCCESSFUL;

class UserTest {
    @Test
    void signup() {
        var user = new User();
        assertEquals(e_SIGNUP_SUCCESSFUL, user.Signup("Example", "example@gmail.com", "12345", "4504969418181177", PERSONAL));
    }

    @Test
    void login() {
        var user = new User();
        assertNotEquals(null, user.Login("alex@g.com", "12345"));
    }
    @Test
    void badLogin() {
        var user = new User();
        assertEquals(null, user.Login("notUsername", "12345"));
    }
}