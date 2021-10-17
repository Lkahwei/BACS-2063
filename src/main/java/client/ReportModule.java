/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import adt.*;
import static client.PaymentModule.df2;
import entity.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;

/**
 *
 * @author Kong Mun Jun
 */
public class ReportModule {    
    private LinkedListInterface<Stock> stockList = new NoDuplicateLinkedList<>();
    private StackWithIteratorInterface<Transaction> transactionStack = new MiddleOperationEnabledLinkedStack<>();
    private MapInterface<String, Report> reportList = new HashTable<String, Report>();
        
    public ReportModule() {
        
        transactionStack = readTransFromBinFile();
        stockList = readStockFromBinFile();
        reportList = readReportFromBinFile();
       
        for (int i = 0; i < stockList.getSize(); i++){
            String stockID = stockList.getElement(i).getStockID();
            String stockName = stockList.getElement(i).getStockName();
            String stockCategory = stockList.getElement(i).getStockCategory();
//            int stockIn = 0;
//            int stockOut = 0;
            int stockIn = reportList.getValue(stockID).getStockInQty();
            int stockOut = reportList.getValue(stockID).getStockOutQty();
            reportList.add(stockID, new Report(stockName, stockCategory, stockIn, stockOut));
        }
    }
    
    public void reportMain( ) throws FileNotFoundException{
        int reportMainChoice = 0;
        FileOutputStream fos;
        do{
            reportMainChoice = displayMainReportMenu();
            switch(reportMainChoice){
                case 1 -> {
                    int reportStockChoice = 0;
                    do{
                        switch(reportStockChoice = DisplayStockReportMenu()){
                            case 1 -> displayStockReport();
                            case 2 -> printStockReport();
                            case 3 -> System.out.println("Exiting Stock Report Menu....");
                        }
                    }while(reportStockChoice != 3);
                }
                case 2 -> {
                    int reportSalesChoice = 0;
                    do{
                        switch(reportSalesChoice = DisplaySalesReportMenu()){
                            case 1 -> displaySalesReport();
                            case 2 -> printSalesReport();
                            case 3 -> System.out.println("Exiting Sales Report Menu....");
                        }
                    }while(reportSalesChoice != 3);
                }
                case 3 -> System.out.println("Exiting Report Main Menu....");
            }
            writeReportToBinFile(reportList);
        }while(reportMainChoice != 3);
    }
    
    public int displayMainReportMenu() {
        int reportMenuChoice =0;
        Scanner scan = new Scanner(System.in);

        do{
            try{
                System.out.println("\n\n\n");
                System.out.println("        REPORT TYPE");
                System.out.println("==================================");
                System.out.println("|    1. Stock Report             |");
                System.out.println("|    2. Sales Report             |");
                System.out.println("|    3. Back to Main Menu        |");
                System.out.println("==================================");
                System.out.println("\n     Enter your choice ---> ");
                
                reportMenuChoice = scan.nextInt();
                
                if(reportMenuChoice <= 0 || reportMenuChoice >= 4){
                    System.out.println("Invalid input. Please try again!");
                }
            }
            catch(Exception e){
                System.out.println("\nInvalid input. Please try again!");
            }
        }while(reportMenuChoice <= 0 || reportMenuChoice >= 4);
        
        return reportMenuChoice;            
    }
    
    public int DisplayStockReportMenu(){

        int StockMenuChoice =0;
        Scanner scan1 = new Scanner(System.in);

        do{
            try{
                System.out.println("\n\n\n");
                System.out.println("        STOCK REPORT");
                System.out.println("==================================");
                System.out.println("|    1. Display Report           |");
                System.out.println("|    2. Print Report             |");
                System.out.println("|    3. Back to Report Menu      |");
                System.out.println("==================================");
                System.out.println("\n     Enter your choice ---> ");
                
                StockMenuChoice = scan1.nextInt();
                
                if(StockMenuChoice <= 0 || StockMenuChoice >= 4){
                    System.out.println("Invalid input. Please try again!");
                }
            }
            catch(Exception e){
                System.out.println("\nInvalid input. Please try again!");
            }
        }while(StockMenuChoice <= 0 || StockMenuChoice >= 4);
        
        return StockMenuChoice;   
    }
    
    public int DisplaySalesReportMenu(){

        int SalesMenuChoice =0;
        Scanner scan2 = new Scanner(System.in);

        do{
            try{
                System.out.println("\n\n\n");
                System.out.println("        SALES REPORT");
                System.out.println("==================================");
                System.out.println("|    1. Display Report           |");
                System.out.println("|    2. Print Report             |");
                System.out.println("|    3. Back to Report Menu      |");
                System.out.println("==================================");
                System.out.println("\n     Enter your choice ---> ");
                
                SalesMenuChoice = scan2.nextInt();
                
                if(SalesMenuChoice <= 0 || SalesMenuChoice >= 4){
                    System.out.println("Invalid input. Please try again!");
                }
            }
            catch(Exception e){
                System.out.println("\nInvalid input. Please try again!");
            }
        }while(SalesMenuChoice <= 0 || SalesMenuChoice >= 4);
        
        return SalesMenuChoice;   
    }
    public void displayStockReport(){
        System.out.println("\n\n\n");
        System.out.println("                            Stock Report");
        System.out.println("                          =================");
        System.out.println("                      Total " + reportList.getSize() + " stock record(s).");
        System.out.println("                    =============================");
        
        System.out.printf("%-20s %-30s %-15s %-10s %-10s","Stock ID", "Name", "Stock Category", "Stock In", "Stock Out");
        System.out.println(String.format("\n%-20s %-30s %-15s %-10s %-10s", "====================", "==============================", "===============", "==========", "=========="));
        if (!reportList.isEmpty()) {
            for(int i = 0; i < stockList.getSize(); i++) {
                String str = stockList.getElement(i).getStockID();
                //if can seperate food and drink
                System.out.printf("%-20s %-30s %-15s %-10d %-10d\n",stockList.getElement(i).getStockID(), reportList.getValue(str).getStockName(),
                        reportList.getValue(str).getStockCategory(),reportList.getValue(str).getStockInQty(), reportList.getValue(str).getStockOutQty());
            }
            System.out.println("=========================================================================================");

        }
        else {
            System.out.println("No Records Found.");
            System.out.println("=========================================================================================");
        }
    }
    
    public void printStockReport() throws FileNotFoundException{
        PrintWriter out = new PrintWriter("Stock Report.txt");
        out.println("\n");
        out.println("                            Stock Report");
        out.println("                          =================");
        out.println("                      Total " + reportList.getSize() + " stock record(s).");
        out.println("                    =============================");
        
        out.printf("%-20s %-30s %-15s %-10s %-10s","Stock ID", "Name", "Stock Category", "Stock In", "Stock Out");
        out.println(String.format("\n%-20s %-30s %-15s %-10s %-10s", "====================", "==============================", "===============", "==========", "=========="));
        if (!reportList.isEmpty()) {
            for(int i = 0; i < stockList.getSize(); i++) {
                String str = stockList.getElement(i).getStockID();
                //if can seperate food and drink
                out.printf("%-20s %-30s %-15s %-10d %-10d\n",stockList.getElement(i).getStockID(), reportList.getValue(str).getStockName(),
                        reportList.getValue(str).getStockCategory(),reportList.getValue(str).getStockInQty(), reportList.getValue(str).getStockOutQty());
            }
            out.println("=========================================================================================");

        }
        else {
            System.out.println("No Records Found.");
            System.out.println("=========================================================================================");
        }

        out.close();
        System.out.println("\n\n\n");
        System.out.println("Report created.");
    }

       public void displaySalesReport(){
        System.out.println("\n\n\n");
        System.out.println("                           Sales Report");
        System.out.println("                          =================");
        System.out.println("                      Total " + transactionStack.returnSize() + " transaction record(s).");
        System.out.println("                    =============================");
        
        System.out.printf("%-20s %-30s %-15s","Transaction ID", "Date", "Sub Total");
        System.out.println(String.format("\n%-20s %-30s %-15s", "====================", "==============================", "==============="));

        double totalSales = 0.00;
        if(!(transactionStack.isEmpty())){
            for(int i = 0; i < transactionStack.returnSize(); i++) {
                System.out.printf("%-20s %-30s %-15s\n",transactionStack.getElement(i).getTransactionID(), transactionStack.getElement(i).getTransactionDate(),
                        subTotalPrice(transactionStack.getElement(i).getItemStack()));
                totalSales += Double.parseDouble(subTotalPrice(transactionStack.getElement(i).getItemStack()));
            }
            System.out.println("\n");
            System.out.println("**********************\n");
            System.out.println("Total Sales : RM " + totalSales);
            System.out.println("**********************\n");
            System.out.println("===================================================================");
        }
        else{
            System.out.println("No Transaction Found");
            System.out.println("===================================================================");
        }
    }
    
    private void printSalesReport() throws FileNotFoundException {
        PrintWriter out = new PrintWriter("Sales Report.txt");
        out.println("\n");
        out.println("                           Sales Report");
        out.println("                          =================");
        out.println("                      Total " + transactionStack.returnSize() + " transaction record(s).");
        out.println("                    =============================");
        
        out.printf("%-20s %-30s %-15s","Transaction ID", "Date", "Sub Total");
        out.println(String.format("\n%-20s %-30s %-15s", "====================", "==============================", "==============="));

        double totalSales = 0.00;
        if(!(transactionStack.isEmpty())){
            for(int i = 0; i < transactionStack.returnSize(); i++) {
                out.printf("%-20s %-30s %-15s\n",transactionStack.getElement(i).getTransactionID(), transactionStack.getElement(i).getTransactionDate(),
                        subTotalPrice(transactionStack.getElement(i).getItemStack()));
                totalSales += Double.parseDouble(subTotalPrice(transactionStack.getElement(i).getItemStack()));
            }
            out.println("Total Sales : RM " + totalSales);
            out.println("===================================================================");
        }
        else{
            out.println("No Transaction Found");
            out.println("===================================================================");
        }
        
        out.close();
        System.out.println("Report created.");
    }
    
    public String subTotalPrice(StackWithIteratorInterface<ProdsForTrans> itemStack){
        double subtotalPrice = 0.00;
        
        Iterator<ProdsForTrans> iterator = itemStack.getIterator();
        while (iterator.hasNext()) {
            ProdsForTrans returnItem = iterator.next();
            subtotalPrice +=  (returnItem.getProductQty() * returnItem.getProductPrice());
        }
        return df2.format(subtotalPrice);
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

    private LinkedListInterface<Stock> readStockFromBinFile() {
        FileOutputStream fos;
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
    
    public void writeStockToBinFile(NoDuplicateLinkedList<Stock> stockList) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("stock.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stockList);
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    private StackWithIteratorInterface<Transaction> readTransFromBinFile() {
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
}
