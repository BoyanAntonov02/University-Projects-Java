package org.example;

import org.example.cashier.Cashier;
import org.example.cashier.PayDesk;
import org.example.exception.ExpiredDateException;
import org.example.goods.Goods;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class BuyProducts {
    private PayDesk payDesk;

    public BuyProducts(PayDesk payDesk) {
        this.payDesk = payDesk;
    }

    public BigDecimal productsSum(Map<Goods, Double> products) throws ExpiredDateException {
        BigDecimal sum = BigDecimal.valueOf(0);
        for(Map.Entry<Goods, Double> entry : products.entrySet()){
            Goods k = entry.getKey();
            double v = entry.getValue();
            sum = sum.add(payDesk.getStore().getStoreService().salePrice(k).multiply(BigDecimal.valueOf(v)));
        }
        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean sellProducts(Client client) throws ExpiredDateException {
        System.out.println("");
        BigDecimal sum = productsSum(client.getCart());
        if(client.getWallet().compareTo(sum) != -1){
            Receipt receipt = new Receipt(payDesk.getCashier(), payDesk, client.getCart());
                String receiptNumber = Integer.toString(receipt.getId())+".txt";
                try(FileWriter fileWriter=new FileWriter(receiptNumber)){
                    fileWriter.write(receipt.printReceipt());
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                System.out.println(receipt.printReceipt());
                return true;
        }
        return false;
    }
}
