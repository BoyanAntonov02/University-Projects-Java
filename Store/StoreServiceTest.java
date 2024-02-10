package org.example.store;

import org.example.exception.ExpiredDateException;
import org.example.store.Store;
import org.example.store.StoreService;
import org.example.goods.Goods;
import org.example.goods.GoodsType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StoreServiceTest {


    @Test
    void salePriceOverPrice() throws ExpiredDateException {
        Goods goods = new Goods(1, "Garpes", BigDecimal.valueOf(5), LocalDate.of(2023,8,28), GoodsType.FOOD);
        Store store = new Store(3, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        StoreService storeService = new StoreService(store);

        BigDecimal expected = BigDecimal.valueOf(550, 2);
        BigDecimal actual = storeService.salePrice(goods);

        assertEquals(expected, actual);
    }

    @Test
    void salePriceDiscountExpire() throws ExpiredDateException {
        Goods goods = new Goods(1, "Garpes", BigDecimal.valueOf(5), LocalDate.of(2023,8,27), GoodsType.FOOD);
        Store store = new Store(200, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        StoreService storeService = new StoreService(store);

        BigDecimal expected = BigDecimal.valueOf(495, 2);
        BigDecimal actual = storeService.salePrice(goods);

        assertEquals(expected, actual);
    }

    @Test
    void salePriceException() throws ExpiredDateException {
        Goods goods = new Goods(1, "Garpes", BigDecimal.valueOf(5), LocalDate.of(2023,4,23), GoodsType.FOOD);
        Store store = new Store(3, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        StoreService storeService = new StoreService(store);

        assertThrowsExactly(ExpiredDateException.class,()-> storeService.salePrice(goods));
    }
}