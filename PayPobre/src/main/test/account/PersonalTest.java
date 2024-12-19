package account;

import authenticationController.Login;
import org.junit.jupiter.api.Test;
import transactions.Transaction;

import static org.junit.jupiter.api.Assertions.*;

class PersonalTest {

    @Test
    void acceptTransaction() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var personal = new Personal(user);
        assertNotEquals(false, personal.acceptTransaction(918459));
    }

    @Test
    void acceptBadIDTransaction() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var personal = new Personal(user);
        assertFalse(personal.acceptTransaction(000000));
    }

    @Test
    void cancelTransaction() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var personal = new Personal(user);
        assertNotEquals(false, personal.cancelTransaction(738686));
    }

    @Test
    void testGoodSendMoney() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var personal = new Personal(user);
        assertNotEquals(false, personal.sendMoney(68, 70, 4000.0));
    }

    @Test
    void testBadSendMoney() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var personal = new Personal(user);
        assertFalse(personal.sendMoney(10, 70, 4000.0));
    }
}