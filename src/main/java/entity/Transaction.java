/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import adt.*;
import java.io.*;
import java.util.Objects;
/**
 *
 * @author Lee Kah Wei
 */
public class Transaction implements Serializable, Comparable<Transaction> {
    final static long serialVersionUID = -7467133292096460342L;
    public static int idCount = 1001;
    private String transactionID;
    private String transactionDateTime;
    private StackWithIteratorInterface<ProdsForTrans> itemStack;
    
    public Transaction() {
        this.transactionID = "T" + String.valueOf(idCount);
        this.transactionDateTime = null;
        this.itemStack = new MiddleOperationEnabledLinkedStack<>();
        idCount += 1;
    }
    
    public Transaction(String transactionID){
        this.transactionID = transactionID;
    }
    
    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionDate() {
        return transactionDateTime;
    }

    public MiddleOperationEnabledLinkedStack<ProdsForTrans> getItemStack() {
        return (MiddleOperationEnabledLinkedStack<ProdsForTrans>) itemStack;
    }
    
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    
    public void setIDCount(int idCount) {
        this.idCount = idCount;
    }
    
    public void setProduct(ProdsForTrans product) {
        this.itemStack.push(product);
    }
    
    public void setNewProductList(){
        this.itemStack = new MiddleOperationEnabledLinkedStack<>();;
    }

    public void setTransactionDate(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    @Override
    public String toString() {
        return "\nTransactionID: " + transactionID + "\nTransaction Date: " + transactionDateTime + "\n\nItem List: ";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
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
        final Transaction other = (Transaction) obj;
        if (!Objects.equals(this.transactionID, other.transactionID)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(Transaction t) {
        return this.transactionID.compareTo(t.transactionID);
    }
}
