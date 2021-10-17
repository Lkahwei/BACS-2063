/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
/**
 *
 * @author Lee Kah Wei
 */
public class ProdsForTrans implements Serializable {
    
    private String productID;
    private int productQty;
    private double productPrice;
    
    public ProdsForTrans(){
        
    }
    
    public ProdsForTrans(String productID){
        this.productID = productID;
    }
    
   public ProdsForTrans(String productID, int productQty, double productPrice) {
        this.productID = productID;
        this.productQty = productQty;
        this.productPrice = productPrice;
    }
   
   public String getProductID() {
        return productID;
    }

    public int getProductQty() {
        return productQty;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return String.format("Product ID : %s \nQuantity: %d units \nPrice: RM %.2f \nSubTotal: RM %.2f\n", productID, productQty, productPrice, (getProductQty() * getProductPrice()));
    }
    
}
