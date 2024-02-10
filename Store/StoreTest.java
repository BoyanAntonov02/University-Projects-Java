package org.example.store;

import org.example.cashier.Cashier;
import org.example.cashier.PayDesk;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    @Test
    void addCashier() {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));

        boolean actual = store.addCashier(cashier);

        assertEquals(true, actual);
    }

    @Test
    void addCashierTwoTimes() {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));

        store.addCashier(cashier);
        boolean actual = store.addCashier(cashier);

        assertEquals(false, actual);
    }

    @Test
    void addCashierEqualID() {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        Cashier cashier2 = new Cashier("Stamat", 1, BigDecimal.valueOf(1000));
        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));

        store.addCashier(cashier);
        boolean actual = store.addCashier(cashier2);

        assertEquals(false, actual);
    }

    @Test
    void addCashiersDiffID() {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        Cashier cashier2 = new Cashier("Stamat", 5, BigDecimal.valueOf(1000));
        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));

        store.addCashier(cashier);
        boolean actual = store.addCashier(cashier2);

        assertEquals(true, actual);
    }

    @Test
    void addCashierToTwoStores() {
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        Store store2 = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));

        store.addCashier(cashier);
        boolean actual = store2.addCashier(cashier);

        assertEquals(false, actual);
    }


    @Test
    void addPayDesk() {
        PayDesk payDesk = new PayDesk(1);
        PayDesk payDesk2 = new PayDesk(2);

        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));

        boolean actual = store.addPayDesk(payDesk);
        assertEquals(true, actual);
    }

    @Test
    void addPayDeskWithSameID() {
        PayDesk payDesk = new PayDesk(1);
        PayDesk payDesk2 = new PayDesk(1);

        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        store.addPayDesk(payDesk2);
        boolean actual = store.addPayDesk(payDesk);
        assertEquals(false, actual);
    }

    @Test
    void addTwicePayDesk() {
        PayDesk payDesk = new PayDesk(1);
        PayDesk payDesk2 = new PayDesk(1);

        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        store.addPayDesk(payDesk);
        boolean actual = store.addPayDesk(payDesk);
        assertEquals(false, actual);
    }

    @Test
    void addPayDeskToTwoStore() {
        PayDesk payDesk = new PayDesk(1);

        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        Store store1 = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        store.addPayDesk(payDesk);
        boolean actual = store1.addPayDesk(payDesk);
        assertEquals(false, actual);
    }
}