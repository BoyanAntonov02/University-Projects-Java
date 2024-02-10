package org.example.cashier;

import org.example.BuyProducts;
import org.example.Client;
import org.example.exception.DeskHaveCashierException;
import org.example.exception.ExpiredDateException;
import org.example.store.Store;

import java.util.Objects;

public class PayDesk {
    private Cashier cashier;
    private boolean isFree = true;
    private Store store = null;
    private int id;

    private BuyProducts buyProducts;



    public PayDesk() {
    }

    public PayDesk(int id) {
        this.id = id;
        buyProducts = new BuyProducts(this);
    }

    public boolean setCashierToPayDesk(Cashier cashier) throws DeskHaveCashierException {
        if(isFree){
            if(!cashier.isBusy()) {
                this.cashier = cashier;
                cashier.setBusy(true);
                isFree = false;
                return true;
            }
            return false;
        }else throw new DeskHaveCashierException();

    }

    public Cashier getCashier() {
        return cashier;
    }

    public BuyProducts getBuyProducts() {
        return buyProducts;
    }

    public boolean sellProducts(Client client) throws ExpiredDateException {
        return buyProducts.sellProducts(client);
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayDesk payDesk = (PayDesk) o;
        return id == payDesk.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
