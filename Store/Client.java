package org.example;

import org.example.exception.NotEnoughAmountException;
import org.example.goods.Goods;
import org.example.store.Store;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private Map<Goods, Double> cart;
    private BigDecimal wallet;

    private Store store;

    public Client(BigDecimal wallet, Store store) {
        this.wallet = wallet;
        this.store = store;
        cart = new HashMap<>();
    }

    public void addGoodsToCart(Goods goods, double amount) throws NotEnoughAmountException {
        if(store.getGoodsInStore().get(goods) >= amount){
           if (cart.containsKey(goods)){
               double currentAmount = cart.get(goods);
               cart.put(goods, currentAmount + amount);
           }
            cart.put(goods, amount);

        } else throw new NotEnoughAmountException();
    }

    public Map<Goods, Double> getCart() {
        return cart;
    }

    public BigDecimal getWallet() {
        return wallet;
    }
}
