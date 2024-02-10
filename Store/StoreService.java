package org.example.store;

import org.example.cashier.Cashier;
import org.example.cashier.PayDesk;
import org.example.exception.ExpiredDateException;
import org.example.goods.Goods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;

import static java.time.temporal.ChronoUnit.DAYS;

public class StoreService {
    private Store store;

    public StoreService(Store store) {
        this.store = store;
    }

    public BigDecimal salePrice(Goods goods) throws ExpiredDateException {
       BigDecimal overPrice = store.getOverPrices().get(goods.getGoodsType());
        long daysBetween = DAYS.between(LocalDate.now(), goods.getExpiredDate());

       if(daysBetween >= 0){
           BigDecimal price = goods.getPrice().add(goods.getPrice().multiply(overPrice).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

           if(store.getBeforeExpire() >= daysBetween){
               price = price.subtract(price.multiply(store.getPercentDiscount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
           }
            return price;
       }
       else throw new ExpiredDateException();
    }

}
