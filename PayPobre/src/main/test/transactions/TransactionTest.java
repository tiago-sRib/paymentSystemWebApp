package transactions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void getNameFromID() {
        Transaction t = new Transaction();
        System.out.println(t.getNameFromID(68));
    }
}