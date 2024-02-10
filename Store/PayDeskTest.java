package org.example.cashier;

import org.example.exception.DeskHaveCashierException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PayDeskTest {

    @Test
    void setCashierToPayDesk() throws DeskHaveCashierException {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        PayDesk payDesk = new PayDesk();

        boolean actual = payDesk.setCashierToPayDesk(cashier);

        assertEquals(true, actual);
    }
    @Test
    void setCashierToPayDeskAlreadyHaveCashier() throws DeskHaveCashierException {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        Cashier cashier2 = new Cashier("Pesho", 2, BigDecimal.valueOf(1000));
        PayDesk payDesk = new PayDesk();

        payDesk.setCashierToPayDesk(cashier);

        assertThrowsExactly(DeskHaveCashierException.class, ()-> payDesk.setCashierToPayDesk(cashier2));
    }

    @Test
    void setCashierToPayDeskToDifferent() throws DeskHaveCashierException {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        PayDesk payDesk = new PayDesk();
        PayDesk payDesk2 = new PayDesk();


        payDesk.setCashierToPayDesk(cashier);
        boolean actual = payDesk2.setCashierToPayDesk(cashier);

        assertEquals(false, actual);
    }

}