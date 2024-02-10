package org.example;

import org.example.cashier.Cashier;
import org.example.cashier.PayDesk;
import org.example.exception.DeskHaveCashierException;
import org.example.exception.ExpiredDateException;
import org.example.exception.NotEnoughAmountException;
import org.example.goods.Goods;
import org.example.goods.GoodsType;
import org.example.store.Store;
import org.example.store.StoreService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws DeskHaveCashierException, NotEnoughAmountException, ExpiredDateException {
        Store store = new Store(10, BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        PayDesk payDesk = new PayDesk(1);
        Cashier cashier = new Cashier("Gosho", 1, BigDecimal.valueOf(1000));
        payDesk.setStore(store);
        store.addCashier(cashier);
        payDesk.setCashierToPayDesk(cashier);
        Goods goods = new Goods(5, "Zele", BigDecimal.valueOf(0.6), LocalDate.of(2023, 8 , 12), GoodsType.FOOD);
        Goods goods1 = new Goods(6, "Domati", BigDecimal.valueOf(1.6), LocalDate.of(2023, 7 , 12), GoodsType.FOOD);
        Goods goods2 = new Goods(12, "Zarq", BigDecimal.valueOf(5.6), LocalDate.of(2024, 7 , 12), GoodsType.NON_FOOD);

        store.addGoodsToStore(goods, 20.0);
        store.addGoodsToStore(goods1 , 50.0);
        store.addGoodsToStore(goods2, 40.0);

        Client client = new Client(BigDecimal.valueOf(100), store);

        client.addGoodsToCart(goods, 1.0);
        client.addGoodsToCart(goods1, 2.0);
        client.addGoodsToCart(goods2, 3.0);

        payDesk.sellProducts(client);
        payDesk.sellProducts(client);


    }
}