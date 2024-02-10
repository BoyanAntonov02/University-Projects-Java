package org.example;

import org.example.cashier.Cashier;
import org.example.cashier.PayDesk;
import org.example.exception.ExpiredDateException;
import org.example.goods.Goods;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;

public class Receipt {
    private static int cnt = 0;
    private final int id;
    private Cashier cashier;
    private LocalDate dateIssue;
    private Map<Goods, Double> goodsMap;
    private PayDesk payDesk;

    public Receipt(Cashier cashier, PayDesk payDesk, Map<Goods,Double> stocks) {
        cnt++;
        id = cnt;
        this.cashier = cashier;
        this.dateIssue = LocalDate.now();
        this.payDesk = payDesk;
        goodsMap = stocks;
    }

    public int getId() {
        return id;
    }

    public String printReceipt() throws ExpiredDateException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println("Receipt");
        printWriter.println("number of bill:" + id );
        printWriter.println("cashier: " + cashier.getId());
        printWriter.println("Date: " + dateIssue);
        for(Map.Entry<Goods, Double> entry : goodsMap.entrySet()){
            Goods k = entry.getKey();
            Double v = entry.getValue();
            printWriter.println(k.getName());
            printWriter.println(v + " * " + payDesk.getStore().getStoreService().salePrice(k).setScale(2, RoundingMode.HALF_UP) + "       = " +
                    payDesk.getStore().getStoreService().salePrice(k).multiply(BigDecimal.valueOf(v)).setScale(2,RoundingMode.HALF_UP));
        }
        printWriter.println("Sum: " + payDesk.getBuyProducts().productsSum(goodsMap).setScale(2, RoundingMode.HALF_UP));
        return stringWriter.toString();
    }
}
