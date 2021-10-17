/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.*;
import entity.*;
import adt.*;
import java.awt.*;
import java.text.*;
import java.io.*;
/**
 *
 * @author Lee Kah Wei
 */
public class PaymentModule {
    
    private StackWithIteratorInterface<Transaction> transactionStack = new MiddleOperationEnabledLinkedStack<>();
    private LinkedListInterface<Stock> stockList = new NoDuplicateLinkedList<>();
    private MapInterface<String, Report> reportList = new HashTable<String, Report>();
    
    public static SimpleDateFormat tf = new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss");
    public static DecimalFormat df2 = new DecimalFormat("0.00");
 
    private String cont;
    private int stackSize = 0;
    
    public PaymentModule(){
        transactionStack = readTransFromBinFile();
        stockList = readStockFromBinFile();
        reportList = readReportFromBinFile();
        System.out.println(stockList);
    }
    
    public void paymentMain(){
//        PaymentModule paymentModule = new PaymentModule();
        int paymentModuleChoice;
        int idCount = 1001;
        
        do{
            paymentModuleChoice = displayTransOptions();
            
            switch(paymentModuleChoice){
                case 1 -> {
                    if(transactionStack.returnSize() == 0){
                        addTransactionToStack(idCount);
                        idCount++;
                    }
                    else{
                       idCount = 1001;
                       idCount = idCount + transactionStack.returnSize() + stackSize; 
                       addTransactionToStack(idCount);
                       idCount++;
                    }    
                }
                case 2 -> searchTransaction();
                
                case 3 -> showAllTransaction();
                
                case 4 -> deleteTransaction();
                
                case 5 -> modifyTransaction();
                
                case 6 -> {writeTransToBinFile(transactionStack);
                            writeStockToBinFile(stockList);
                            writeReportToBinFile(reportList);
                            System.out.println("Exiting...");}
                }
            } while(paymentModuleChoice != 6);
        }
        
    
    public void invalidInputMsg() {
        System.out.println("Invalid Input ! Please Enter Again..");
        
    }
    
    public int displayTransOptions() {
        int choice = 0;
        Scanner scan = new Scanner(System.in);
        
        do{  
            try{
                System.out.println("\nTransaction MENU");
                System.out.println("===========================");
                System.out.println("|1. Add Transaction         |");
                System.out.println("|2. Search Transaction      |");
                System.out.println("|3. Show All Transaction    |");
                System.out.println("|4. Delete Transaction      |");
                System.out.println("|5. Modify Transaction      |");
                System.out.println("|6. Back To Main Menu       |");
                System.out.println("===========================");

                System.out.println("Enter your choice(1 - 6): "); 
                choice = scan.nextInt();
                
                if (choice <= 0 || choice > 6){
                    invalidInputMsg();
                }
            } catch (Exception e) {
                scan.nextLine();
                invalidInputMsg();
            }
            
        } while (choice <= 0 || choice > 6); 
        
        return choice;
    }

    public void addTransactionToStack(int idCount){
        //Variable 
        Transaction transaction = new Transaction();
        transaction.setIDCount(idCount);
        String prodIDEntered;
        Stock stock;
        ProdsForTrans product;
        int orderQty;
        Calendar transactionDate = Calendar.getInstance();
        Scanner scan = new Scanner(System.in);
        
        do{       
            transaction = new Transaction();
            
            do{
                product = new ProdsForTrans();
                System.out.println("Enter the product ID (X/x to exit): ");
                scan = new Scanner(System.in);
                prodIDEntered = scan.nextLine();
                stock = new Stock(prodIDEntered);
                    
                if(stockList.contains(stock)){
                    int index = stockList.getIndex(stock);
                    String rpID = stockList.getElement(index).getStockID();
                    System.out.printf("Product ID %s \nQuantity Available: %d\n", stockList.getElement(index).getStockID(), stockList.getElement(index).getStockQty());
                    if(!prodIDEntered.equalsIgnoreCase("X")){
                        product.setProductID(prodIDEntered);
                        System.out.println("Enter the quantity: ");
                        scan = new Scanner(System.in);
                        orderQty = scan.nextInt(); 
                        product.setProductQty(orderQty);
                        product.setProductPrice(stockList.getElement(index).getSellingPricePerItem());
                        int quantity = stockList.getElement(index).getStockQty() - orderQty;
                        stockList.getElement(index).setStockQty(quantity);
                        transaction.setProduct(product);
                        reportList.add(rpID, new Report(stockList.getElement(index).getStockID(),stockList.getElement(index).getStockID(), 
                                                    reportList.getValue(rpID).getStockInQty(), reportList.getValue(rpID).getStockOutQty()+orderQty));
                    }
                }     
                else if (prodIDEntered.equalsIgnoreCase("X")){
                    System.out.println("\nExiting from adding products..");
                }
                else{
                    System.out.println("\nProduct doesn't exists (Format- STOXXXX), Please Enter Again!!\n");
                }
            } while (!prodIDEntered.equalsIgnoreCase("X")); 
            transaction.setTransactionDate(tf.format(transactionDate.getTime()));
            transactionStack.push(transaction);
            receipt();
            System.out.println("Another Transaction? (Y/N): ");
            cont = scan.nextLine();
        } while (cont.equalsIgnoreCase("Y"));
        System.out.println(stockList);
    }
    
    public void receipt() {
        if(!(transactionStack.isEmpty())){
            System.out.println(transactionStack.peek());
            itemDetailsWithTotalPrice(transactionStack.peek().getItemStack(), false);
        }
        else{
            throw new NoSuchElementException("Illegal call; iterator is at end of list.");
        }
    }
    
    public void itemDetailsWithTotalPrice(StackWithIteratorInterface<ProdsForTrans> itemStack, boolean isSearch){
        int choice;
        int wrongAttempt = 0;
        double totalPrice = 0.00;
        double amountGiven;
        String paymentType = "";
        String password;
        Scanner scan = new Scanner(System.in);
        Iterator<ProdsForTrans> iterator = itemStack.getIterator();
        while (iterator.hasNext()) {
            ProdsForTrans returnItem = iterator.next();
            System.out.println(returnItem);
            totalPrice +=  (returnItem.getProductQty() * returnItem.getProductPrice());
        }
        
        System.out.println("Total Price: RM " + df2.format(totalPrice));
        if(isSearch != true){
           do{
               if(wrongAttempt == 3){
                   System.out.println("\nPlease Select Another Payment Method, wrong Attempt Reached!!!");
                   wrongAttempt = 0;
               }
                System.out.println("\nSelect Payment Type");
                System.out.println("===========================");
                System.out.println("|1. Debit/Credit Card      |");
                System.out.println("|2. Cash                   |");
                System.out.println("===========================");
                choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        paymentType = "Debit/Credit Card";
                        do{
                            System.out.println("Enter Password...: ");
                            scan = new Scanner(System.in);
                            password = scan.nextLine();
                            System.out.println(password.length());
                            while(password.length() != 6){
                                System.out.println("Please Enter 6-digit password only");
                                System.out.println("\nEnter Password...: ");
                                scan = new Scanner(System.in);
                                password = scan.nextLine();
                            }
                            
                            //Hardcoded password for testing purpose only
                            if(password.equals("123456")){
                                Toolkit.getDefaultToolkit().beep();
                                System.out.println("Payment Successful!!");
                            }
                        
                            else{
                                wrongAttempt++;
                                System.out.println("Incorrect Password!! " + (3-wrongAttempt) + " chances left, Please Enter Again!!");     
                            }
                        }while(!(password.equals("123456")) && wrongAttempt < 3);
                    }
                    case 2 -> {
                        paymentType = "Cash";
                        do{
                            System.out.println("Enter Amount...: ");
                            scan = new Scanner(System.in);
                            amountGiven = scan.nextDouble();
                            if(amountGiven < totalPrice) {
                                System.out.println("Please Enter Again.. Invalid Amount!!");
                            }
                            else{
                                System.out.println("Change:" + df2.format(amountGiven- totalPrice));
                            }
                        }while(amountGiven < totalPrice);
                    }
                    default -> System.out.println("Please Enter Again.. Invalid input!!");
                }
                }while((choice != 1 && choice != 2) || wrongAttempt == 3);  
        }    
    }
    
    public void showAllTransaction(){
        if(!(transactionStack.isEmpty())){
            Iterator<Transaction> iterator = transactionStack.getIterator();
            while (iterator.hasNext()) {
                Transaction returnTrans = iterator.next();
                System.out.println(returnTrans);
                itemDetailsWithTotalPrice(returnTrans.getItemStack(), true);
            }
        }
        else{
            System.out.println("No transaction Available yet, Please ADD a new transaction first\n");
        }
        
        
    }
    
    public void searchTransaction(){
        String transactionID;
        Scanner scan;
        
        if(!(transactionStack.isEmpty())){
            do{
                System.out.println("Enter the transaction ID you wanna find: ");
                scan = new Scanner(System.in);
                transactionID = scan.nextLine();
                Transaction t = new Transaction(transactionID);
                if(transactionStack.contains(t)){
                    int index = transactionStack.indexOf(t);
                    System.out.println("Transaction ID: " + transactionStack.getElement(index).getTransactionID() + "\nTransaction Date: " + transactionStack.getElement(index).getTransactionDate() + "\n\nItem List: ");
                    itemDetailsWithTotalPrice(transactionStack.getElement(index).getItemStack(), true);
                }
                
                else{
                System.out.println("No such Transaction, please Enter again");
                }
                
                System.out.println("Continue Searching? (Y/N): ");
                scan = new Scanner(System.in);
                cont = scan.nextLine();
            } while(cont.equalsIgnoreCase("Y")); 
        }
        else{
            System.out.println("No transaction Available yet, Please ADD a new transaction first\n");
        }
    }

    public void deleteTransaction(){
        String transactionID;
        Scanner scan; 
        StackWithIteratorInterface<ProdsForTrans> itemStack = new MiddleOperationEnabledLinkedStack<>();
        Stock stock;
        
        if(!(transactionStack.isEmpty())){
            do{
                System.out.println("Enter the transaction ID you want to delete: ");
                scan = new Scanner(System.in);
                transactionID = scan.nextLine();
                Transaction t = new Transaction(transactionID);
                if(transactionStack.contains(t)){
                    int index = transactionStack.indexOf(t);
                    System.out.println("Transaction ID: " + transactionStack.getElement(index).getTransactionID() + "\nTransaction Date: " + transactionStack.getElement(index).getTransactionDate() + "\n\nItem List: ");
                    itemDetailsWithTotalPrice(transactionStack.getElement(index).getItemStack(), true);
                    System.out.println("Confirm Delete? (Y/N): ");
                    scan = new Scanner(System.in);
                    cont = scan.nextLine();
                    if(cont.equalsIgnoreCase("Y")){
                        itemStack = transactionStack.getElement(index).getItemStack();
                        int size = itemStack.returnSize();
                        System.out.println(size);
                        for(int i = 0; i < size; i++){
                            stock = new Stock(itemStack.peek().getProductID());
                            if(stockList.contains(stock)){
                                index = stockList.getIndex(stock);
                                String rpID = stockList.getElement(index).getStockID();
                                int quantity = itemStack.peek().getProductQty();
                                quantity += stockList.getElement(index).getStockQty();
                                stockList.getElement(index).setStockQty(quantity);
                                reportList.add(rpID, new Report(stockList.getElement(index).getStockID(),stockList.getElement(index).getStockID(), 
                                                    reportList.getValue(rpID).getStockInQty()+quantity, reportList.getValue(rpID).getStockOutQty()-quantity));
                            }
                            itemStack.pop();
                        }
                        System.out.println("Transaction ID: " + transactionStack.removeItemBasedID(t).getTransactionID() + " has been deleted successfully!!");
                        stackSize += 1;
                        System.out.println("Continue Deletion? (Y/N): ");
                        scan = new Scanner(System.in);
                        cont = scan.nextLine();
                    }
                    else{
                        System.out.println("Cancel Successfully!!");
                        System.out.println("Continue Deletion? (Y/N): ");
                        scan = new Scanner(System.in);
                        cont = scan.nextLine(); 
                    }
                }
                
                else{
                    System.out.println("No such Transaction, please Enter again");
                    System.out.println("Continue Deletion? (Y/N): ");
                    scan = new Scanner(System.in);
                    cont = scan.nextLine(); 
                } 
                
            } while(cont.equalsIgnoreCase("Y"));
           
        }
        
        else{
            System.out.println("No transaction Available yet, Please ADD a new transaction first\n");
        }
        System.out.println(stockList);
    }

    
    public void modifyTransaction(){
        String transactionID;
        Scanner scan;
        int index;
        int orderQty;
        String prodIDEntered;
        Stock stock;
        ProdsForTrans product;
        String transactionDate = "";
        Transaction transaction = new Transaction();
        StackWithIteratorInterface<ProdsForTrans> itemStack = new MiddleOperationEnabledLinkedStack<>();
        
        
        if(!(transactionStack.isEmpty())){
            do{
                System.out.println("Enter the transaction ID you want to modify: ");
                scan = new Scanner(System.in);
                transactionID = scan.nextLine();
                Transaction t = new Transaction(transactionID);
                transaction = new Transaction();
                if(transactionStack.contains(t)){
                    index = transactionStack.indexOf(t);
                    transactionID = transactionStack.getElement(index).getTransactionID();
                    transactionDate = transactionStack.getElement(index).getTransactionDate();
                    System.out.println("Transaction ID: " + transactionID + "\nTransaction Date: " + transactionDate + "\n\nItem List: ");
                    itemDetailsWithTotalPrice(transactionStack.getElement(index).getItemStack(), true);
                    System.out.println("Confirm Modify? (Y/N): ");
                    scan = new Scanner(System.in);
                    cont = scan.nextLine();
                    if(cont.equalsIgnoreCase("Y")){
                        itemStack = transactionStack.getElement(index).getItemStack();
                        int size = itemStack.returnSize();
                        System.out.println(size);
                        for(int i = 0; i < size; i++){   
                            stock = new Stock(itemStack.peek().getProductID());
                            if(stockList.contains(stock)){
                                index = stockList.getIndex(stock);
                                String rpID = stockList.getElement(index).getStockID();
                                int quantity = itemStack.peek().getProductQty();
                                quantity += stockList.getElement(index).getStockQty();
                                stockList.getElement(index).setStockQty(quantity);
                                reportList.add(rpID, new Report(stockList.getElement(index).getStockID(),stockList.getElement(index).getStockID(), 
                                                    reportList.getValue(rpID).getStockInQty()+quantity, reportList.getValue(rpID).getStockOutQty()-quantity));
                            }
                            itemStack.pop();
                        }
                        transactionStack.removeItemBasedID(t);
                        transaction.setTransactionID(transactionID);
                        transaction.setTransactionDate(transactionDate);
                        
                        do{
                            product = new ProdsForTrans();
                            System.out.println("Enter the product ID (X/x to exit): ");
                            scan = new Scanner(System.in);
                            prodIDEntered = scan.nextLine();
                            stock = new Stock(prodIDEntered);
                    
                        if(stockList.contains(stock)){
                            index = stockList.getIndex(stock);
                            String rpID = stockList.getElement(index).getStockID();
                            System.out.printf("Product ID %s \nQuantity Available: %d\n", stockList.getElement(index).getStockID(), stockList.getElement(index).getStockQty());
                            if(!prodIDEntered.equalsIgnoreCase("X")){
                                product.setProductID(prodIDEntered);
                                System.out.println("Enter the quantity: ");
                                scan = new Scanner(System.in);
                                orderQty = scan.nextInt(); 
                                product.setProductQty(orderQty);
                                product.setProductPrice(stockList.getElement(index).getSellingPricePerItem());
                                int quantity = stockList.getElement(index).getStockQty() - orderQty;
                                stockList.getElement(index).setStockQty(quantity);
                                transaction.setProduct(product);
                                reportList.add(rpID, new Report(stockList.getElement(index).getStockID(),stockList.getElement(index).getStockID(), 
                                                    reportList.getValue(rpID).getStockInQty(), reportList.getValue(rpID).getStockOutQty()+orderQty));
                            }
                        }     
                        else if (prodIDEntered.equalsIgnoreCase("X")){
                            System.out.println("\nExiting from adding products..");
                        }
                        else{
                            System.out.println("\nProduct doesn't exists (Format- STOXXXX), Please Enter Again!!\n");
                        }
                    } while (!prodIDEntered.equalsIgnoreCase("X")); 
                    transactionStack.push(transaction);
                    receipt();
                    System.out.println("Continue Modification? (Y/N): ");
                    scan = new Scanner(System.in);
                    cont = scan.nextLine();
                    }
                    
                    else{
                    System.out.println("Cancel Successfully!!");
                    System.out.println("Continue Modification? (Y/N): ");
                    scan = new Scanner(System.in);
                    cont = scan.nextLine(); 
                    }
                }
                
                else{
                    System.out.println("No such Transaction, please Enter again");
                    System.out.println("Continue Modification? (Y/N): ");
                    scan = new Scanner(System.in);
                    cont = scan.nextLine(); 
                }
                
            } while(cont.equalsIgnoreCase("Y"));
        }
        else{
            System.out.println("No transaction Available yet, Please ADD a new transaction first\n");
        }
        System.out.println(stockList);
    }

    public NoDuplicateLinkedList<Stock> readStockFromBinFile() {
        NoDuplicateLinkedList<Stock> stockList = new NoDuplicateLinkedList<>();
        
        try {
            // read back
            FileInputStream fis = new FileInputStream("stock.bin");
            ObjectInputStream ois = new ObjectInputStream(fis); 
            Object obj = ois.readObject();
            stockList = (NoDuplicateLinkedList) obj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stockList;
    }
    
    public MiddleOperationEnabledLinkedStack<Transaction> readTransFromBinFile() {
        MiddleOperationEnabledLinkedStack<Transaction> transactionStack = new MiddleOperationEnabledLinkedStack<>();
        
        try {
            // read back
            FileInputStream fis = new FileInputStream("transaction.bin");
            ObjectInputStream ois = new ObjectInputStream(fis); 
            Object obj = ois.readObject();
            transactionStack = (MiddleOperationEnabledLinkedStack) obj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return transactionStack;
    }
    
    public void writeTransToBinFile(StackWithIteratorInterface<Transaction> transactionStack) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("transaction.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(transactionStack);
            System.out.println("Run Successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeStockToBinFile(LinkedListInterface<Stock> stockList) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("stock.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stockList);
            System.out.println("Run Successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
        public MapInterface<String, Report>/**/ readReportFromBinFile() {
        FileOutputStream fos;
        MapInterface<String, Report> reportList = new HashTable<>();
        try {
            // read back
            FileInputStream fis = new FileInputStream("report.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            reportList = (MapInterface) obj;

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return reportList;
    }
     
    public void writeReportToBinFile(MapInterface<String, Report> reportList) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("report.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(reportList);
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
