package org.example.store;

import org.example.cashier.Cashier;
import org.example.cashier.PayDesk;
import org.example.goods.Goods;
import org.example.goods.GoodsType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Store {

    private Map<GoodsType, BigDecimal> overPrices;
    private Map<Goods, Double> goodsInStore;
    private long beforeExpire;
    private BigDecimal percentDiscount;
    private HashSet<Cashier> cashierHashSet;
    private HashSet<PayDesk> payDeskHashSet;
    private StoreService storeService;

    public Store(long beforeExpire, BigDecimal percentDiscount, BigDecimal food, BigDecimal non_food) {
        overPrices = new HashMap<>();
        goodsInStore = new HashMap<>();
        this.beforeExpire = beforeExpire;
        this.percentDiscount = percentDiscount;
        overPrices.put(GoodsType.FOOD, food);
        overPrices.put(GoodsType.NON_FOOD, non_food);
        cashierHashSet = new HashSet<>();
        payDeskHashSet = new HashSet<>();
        storeService = new StoreService(this);
    }

    public Map<GoodsType, BigDecimal> getOverPrices() {
        return overPrices;
    }

    public void setOverPrices(Map<GoodsType, BigDecimal> overPrices) {
        this.overPrices = overPrices;
    }

    public Map<Goods, Double> getGoodsInStore() {
        return goodsInStore;
    }

    public void setGoodsInStore(Map<Goods, Double> goodsInStore) {
        this.goodsInStore = goodsInStore;
    }

    public long getBeforeExpire() {
        return beforeExpire;
    }

    public void setBeforeExpire(long beforeExpire) {
        this.beforeExpire = beforeExpire;
    }

    public BigDecimal getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(BigDecimal percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public boolean addCashier(Cashier cashier){
        if(cashier.getStore() == null){
            cashier.setStore(this);
            return cashierHashSet.add(cashier);
        }
        return false;
    }

    public boolean addPayDesk(PayDesk payDesk){
        if(payDesk.getStore() == null) {
            payDesk.setStore(this);
            return payDeskHashSet.add(payDesk);
        }
        return false;
    }

    public boolean addGoodsToStore(Goods goods, double amount){
        if(amount > 0) {
            if (goodsInStore.containsKey(goods)) {
                double cAmount = goodsInStore.get(goods);
                goodsInStore.put(goods, amount + cAmount);
            } else {
                goodsInStore.put(goods, amount);
            }
            return true;
        }else return false;
    }
}
