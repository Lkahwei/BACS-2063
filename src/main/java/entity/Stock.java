/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Chin Jun Wai
 */
public class Stock implements Serializable{
    final static long serialVersionUID = -4496672615704491764L;
    public static int count = 0;
    private String stockID;
    private String stockName;
    private int stockQty;
    private double costPricePerItem;
    private double sellingPricePerItem;
    private Calendar stockExpiredDate;
    private String stockCategory;

    public Stock() {

    }

    public Stock(String stockID) {
        this.stockID = stockID;
    }

    public Stock(String stockID, String stockName, int stockQty, double costPricePerItem, double sellingPricePerItem, Calendar stockExpiredDate, String stockCategory) {
        this.stockID = stockID;
        this.stockName = stockName;
        this.stockQty = stockQty;
        this.costPricePerItem = costPricePerItem;
        this.sellingPricePerItem =sellingPricePerItem;
        this.stockExpiredDate = stockExpiredDate;
        this.stockCategory = stockCategory;
        count++;
    }

    public static int getCount() {
        return count;
    }

    public Stock(String stockID, String stockName, int stockQty, String stockCategory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getStockID() {
        return stockID;
    }

    public String getStockName() {
        return stockName;
    }

    public int getStockQty() {
        return stockQty;
    }

    public double getCostPricePerItem() {
        return costPricePerItem;
    }

    public double getSellingPricePerItem() {
        return sellingPricePerItem;
    }

    public Calendar getStockExpiredDate() {
        return stockExpiredDate;
    }

    public static void setCount(int count) {
        Stock.count = count;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public void setCostPricePerItem(double costPricePerItem) {
        this.costPricePerItem = costPricePerItem;
    }

    public void setSellingPricePerItem(double sellingPricePerItem) {
        this.sellingPricePerItem = sellingPricePerItem;
    }

    public void setStockExpiredDate(Calendar stockExpiredDate) {
        this.stockExpiredDate = stockExpiredDate;
    }

    public String getOnlyDate(Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date.getTime());
    }

    public void setStockCategory(String stockCategory) {
        this.stockCategory = stockCategory;
    }

    public String getStockCategory() {
        return stockCategory;
    }
    
    public void stockIn(int stockInQty){
        this.stockQty += stockInQty;
    }
    
    public void stockOut(int stockOutQty){
        this.stockQty -= stockOutQty;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stock other = (Stock) obj;
        if (!Objects.equals(this.stockID, other.stockID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-20s %-30s %-7d %-11.2f %-20.2f %-12s %-20s", stockID, stockName, stockQty, costPricePerItem, sellingPricePerItem, getOnlyDate(stockExpiredDate), stockCategory);
    }

}
