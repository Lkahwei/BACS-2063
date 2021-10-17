/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Kong Mun Jun
 */
public class Report implements Serializable{

    public static int count = 0;
    private String stockName;
    private int stockInQty;
    private int stockOutQty;
    private String stockCategory;

    public Report() {

    }

    /**
     *
     * @param stockID
     * @param stockName
     * @param stockInQty
     * @param stockOutQty
     * @param stockCategory
     */
    public Report(String stockName, String stockCategory, int stockInQty, int stockOutQty) {
    
        this.stockName = stockName;
        this.stockInQty = stockInQty;
        this.stockOutQty = stockOutQty;
        this.stockCategory = stockCategory;
        count++;
    }

    public Report(String stockName) {
        this.stockName = stockName;
    }
//
//    public Report(String stockID, String stockName, int stockQty, String stockCategory) {
//        this.stockID = stockID;
//        this.stockName = stockName;
//        this.stockQty = stockQty;
//        this.stockCategory = stockCategory;
//        count++;
//    }

    public static int getCount() {
        return count;
    }

    public String getStockName() {
        return stockName;
    }

    public static void setCount(int count) {
        Stock.count = count;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockCategory(String stockCategory) {
        this.stockCategory = stockCategory;
    }

    public String getStockCategory() {
        return stockCategory;
    }
    
    public void stockIn(int stockInQty){
        this.stockInQty += stockInQty;
        
    }
    
    public void stockOut(int stockOutQty){
        this.stockOutQty += stockOutQty;
        
    }

    public int getStockInQty() {
        return stockInQty;
    }

    public void setStockInQty(int stockInQty) {
        this.stockInQty = stockInQty;
    }

    public int getStockOutQty() {
        return stockOutQty;
    }

    public void setStockOutQty(int stockOutQty) {
        this.stockOutQty = stockOutQty;
    }

    @Override
    public String toString() {
        return String.format("%-30s %-20s %-7d %-7d", stockName, stockCategory, stockInQty, stockOutQty);
    }
    
    
}
