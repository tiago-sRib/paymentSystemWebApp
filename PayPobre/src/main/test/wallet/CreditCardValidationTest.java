package wallet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static util.Const.*;

class CreditCardValidationTest {

    @Test
    void validation() {
        assertTrue(CreditCardValidation.validation("377119178515774"));
        assertFalse(CreditCardValidation.validation("String Manhosa"));
    }

    @Test
    void prefixCheck() {
        assertEquals(prefix_VISA , CreditCardValidation.prefixCheck("4504969418181177"));
        assertEquals(prefix_MASTER, CreditCardValidation.prefixCheck("5527787689696404"));
        assertEquals(prefix_AMERICAN_EXPRESS, CreditCardValidation.prefixCheck("377119178515774"));
        assertEquals(prefix_DISCOVER, CreditCardValidation.prefixCheck("6011804247598401"));
    }
}