package account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommercialTest {

    @Test
    void issueGoodTransaction() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var comercial = new Commercial(user);
        assertNotEquals(-1, comercial.issueTransaction(68, 70, 12.0));
    }

    @Test
    void issueBadSellerIDTransaction() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var comercial = new Commercial(user);
        assertEquals(-1, comercial.issueTransaction(10, 70, 12.0));
    }

    @Test
    void testGoodSendMoney() {
        var user = new User();
        user = user.Login("alex@g.com", "12345");
        var comercial = new Commercial(user);
        assertNotEquals(false, comercial.sendMoney(68, 70, 4000.0));
    }
}