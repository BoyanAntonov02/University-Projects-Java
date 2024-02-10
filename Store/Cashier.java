package org.example.cashier;

import org.example.store.Store;

import java.math.BigDecimal;
import java.util.Objects;

public class Cashier {
    private String name;
    private final int id;
    private BigDecimal monthlySalary;
    private boolean busy = false;
    private Store store = null;

    public Cashier(String name, int id, BigDecimal monthlySalary) {
        this.name = name;
        this.id = id;
        this.monthlySalary = monthlySalary;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return id == cashier.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
