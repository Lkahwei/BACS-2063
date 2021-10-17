/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import adt.HashTable;
import adt.NoDuplicateLinkedList;
import adt.LinkedListInterface;
import adt.MapInterface;
import entity.Report;
import entity.Stock;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Chin Jun Wai
 */

public class StockModule {

    private LinkedListInterface<Stock> stockList = new NoDuplicateLinkedList<>();
    private MapInterface<String, Report> reportList = new HashTable<String, Report>();

    public StockModule() {
        stockList = readStockFromBinFile();
        reportList = readReportFromBinFile();
    }

    public void stockMain() {
        Scanner scan = new Scanner(System.in);
        StockModule stockModule = new StockModule();
        Calendar calendar = Calendar.getInstance();

        FileOutputStream fos;

        int stockMenuChoice;
        do {
            do {
                stockMenuChoice = 0;
                stockMenuChoice = stockModule.displayStockMenu();

                if (!(stockMenuChoice <= 0 || stockMenuChoice > 6)) {
                    switch (stockMenuChoice) {
                        case 1:
                            addNewStock();
                            break;

                        case 2:

                            updateStock();
                            break;

                        case 3:

                            deleteStock();
                            break;

                        case 4:
                            displayStockDetails();
                            break;

                        case 5:
                            searchStock();
                            break;
                        case 6:
                            break;
                        default:
                            break;
                    }
                } else {
                    invalidChoiceMsg();

                }

            } while (stockMenuChoice <= 0 || stockMenuChoice > 6);

            writeToBinFile();
            writeReportToBinFile(reportList);
        } while (stockMenuChoice != 6);

    }

    public void invalidChoiceMsg() {
        System.out.println("Invalid Selection ! Please select again. ");
    }

    public void invalidInputMsg() {
        System.out.println("Invalid Input ! Please enter again.");
    }

    public int displayStockMenu() {
        int choice = 0;
        Scanner input = new Scanner(System.in);
        do {

            try {
                System.out.println("\nSTOCK MENU");
                System.out.println("===========================");
                System.out.println("|1. Add New Stock         |");
                System.out.println("|2. Update Stock          |");
                System.out.println("|3. Delete Stock          |");
                System.out.println("|4. Display Stock Details |");
                System.out.println("|5. Search Stock          |");
                System.out.println("|6. Back To Main Menu     |");
                System.out.println("===========================");

                System.out.println("Enter your choice(1 - 6): ");

                choice = input.nextInt();

                if (choice <= 0 || choice > 6) {
                    invalidInputMsg();
                }
            } catch (Exception e) {
                input.nextLine();
                invalidInputMsg();
            }

        } while (choice <= 0 || choice > 6);
        return choice;

    }

    public void addNewStock() {
        Scanner scan = new Scanner(System.in);
        char continue_add_stock = 'y';

        while (continue_add_stock == 'y') {

            System.out.println(" ");
            String stockID = "";
            String stockName = "";
            int stockQty = 0;
            double costPricePerItem = 0.0;
            double sellingPricePerItem = 0.0;
            String expiredDate = "";
            String stockCategory = "";
            boolean checkInput = true;
            boolean inputValidation = true;

            do {

                checkInput = true;
                inputValidation = true;

                System.out.println("\nEvery detail must be filled in, no empty field is allowed.");

                System.out.println("\nReminder: Updating Stock ID is not allowed, please be careful. You may delete the stock record if the stock ID is incorrect.");
                Stock lastStock = (Stock) stockList.getLast();
                if (lastStock != null) {
                    System.out.println("\nThe last stock ID inserted is: " + lastStock.getStockID());
                }

                try {
                    System.out.println("\nPlease enter stock ID (STOXXXX / e to exit): ");
                    stockID = scan.nextLine();
                } catch (Exception e) {
                    scan.nextLine();
                    checkInput = false;
                }
                if (stockID.toLowerCase().equals("e")) {
                    break;
                }
                try {
                    System.out.println("\nEnter stock Name (Max 30 characters): ");
                    stockName = scan.nextLine().toUpperCase();
                } catch (Exception e) {
                    scan.nextLine();
                    checkInput = false;
                }

                try {
                    System.out.println("\nEnter stock Quantity (Min:1, Max:100) : ");
                    if (scan.hasNextInt()) {
                        stockQty = scan.nextInt();
                    } else {
                        checkInput = false;
                        scan.nextLine();
                    }

                } catch (Exception e) {
                    scan.nextLine();
                    checkInput = false;
                }
                try {
                    System.out.println("\nEnter stock price per item (RM): ");
                    if (scan.hasNextDouble()) {
                        costPricePerItem = scan.nextDouble();

                    } else {
                        checkInput = false;
                        scan.nextLine();
                    }

                } catch (Exception e) {
                    scan.nextLine();
                    checkInput = false;
                }
                try {
                    System.out.println("\nEnter selling price per item (RM): ");
                    if (scan.hasNextDouble()) {
                        sellingPricePerItem = scan.nextDouble();
                        scan.nextLine();
                    } else {
                        scan.nextLine();
                        checkInput = false;

                    }

                } catch (Exception e) {

                    scan.nextLine();
                    checkInput = false;
                }
                try {
                    System.out.println("\nEnter stock Expired Date (dd-mm-yyyy): ");
                    expiredDate = scan.nextLine();
                } catch (Exception e) {
                    checkInput = false;
                }
                try {
                    System.out.println("\nEnter stock Category (Food / Drink / Other): ");
                    stockCategory = scan.nextLine().toUpperCase();
                } catch (Exception e) {
                    checkInput = false;
                }
                inputValidation = checkUserInput(stockID, stockName, stockQty, costPricePerItem, sellingPricePerItem, expiredDate, stockCategory);

            } while ((checkInput == false) || (inputValidation == false));

            if (!stockID.toLowerCase().equals("e")) {
                System.out.println(" ");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                try {
                    Date date = dateFormat.parse(expiredDate);
                    calendar.setTime(date);
                } catch (ParseException p) {

                }
                Stock stock = new Stock();
                System.out.println("\nStock Details Entered ");
                System.out.println("---------------------");
                System.out.println("Stock ID            : " + stockID);
                System.out.println("Stock Name          : " + stockName);
                System.out.println("Stock Quantity      : " + stockQty);
                System.out.println("Stock Price (RM)    : " + costPricePerItem);
                System.out.println("Selling Price (RM)  : " + sellingPricePerItem);
                System.out.println("Stock Expired Date  : " + stock.getOnlyDate(calendar));
                System.out.println("Stock Category      : " + stockCategory);
                char confirmation_add = 'n';
                do {
                    try {
                        System.out.println("\nConfirm to add (y = yes /n = no): ");
                        confirmation_add = scan.next().toLowerCase().charAt(0);
                        scan.nextLine();
                    } catch (Exception e) {
                        invalidInputMsg();
                    }

                } while (!(confirmation_add == 'n' || confirmation_add == 'y'));
                if (confirmation_add == 'y') {
                    if (stockList.add(new Stock(stockID, stockName, stockQty, costPricePerItem, 
                            sellingPricePerItem, calendar, stockCategory))) {
                        reportList.add(stockID, new Report(stockName, stockCategory, stockQty, 0));
                        System.out.println("\nThe stock is successfully added into the system.");

                    } else {
                        System.out.println("\nThe stock is failed to add into the system. Stock is already exist.");
                    }
                }
                boolean invalid_input = true;
                while (invalid_input) {
                    try {
                        System.out.println("\nDo you want to continue add more stocks (y = yes /n = no): ");
                        continue_add_stock = scan.next().toLowerCase().charAt(0);
                        scan.nextLine();
                    } catch (Exception e) {

                    }
                    if (continue_add_stock == 'y' || continue_add_stock == 'n') {
                        invalid_input = false;
                    } else {
                        invalidInputMsg();
                    }
                }
            } else if (stockID.toLowerCase().equals("e")) {
                break;
            }

        }
    }

    public boolean checkUserInput(String stockID, String stockName, int stockQty, double costPricePerItem, 
            double sellingPricePerItem, String expiredDate, String stockCategory) {
        String stockIdRegex = "STO[0-9][0-9][0-9][0-9]";
        Pattern pattern = Pattern.compile(stockIdRegex);
        Matcher matcher = pattern.matcher(stockID);
        if (stockID.isBlank()) {
            System.out.println("\nStock ID must not be blank");
            return false;
        } else if (!matcher.matches()) {
            System.out.println("\nStock ID must be in the format (STOXXXX). For example, STO1001");
            return false;
        } else if (stockName.isBlank()) {
            System.out.println("\nStock Name must not be blank");
            return false;
        } else if (stockName.length() > 30) {
            System.out.println("\nStock Name has exceed more than 30 characters");
            return false;
        } else if (stockCategory.isBlank()) { // check catergory
            System.out.println("\nStock Category must not be blank");
            return false;
        } else if (!(stockCategory.toLowerCase().equals("food") || stockCategory.toLowerCase().equals("drink") || stockCategory.toLowerCase().equals("other"))) {
            System.out.println("\nOnly 'food' or 'drink' or 'other' is allowed for stock catergory");
            return false;
        } else if (!checkExpiredDateInput(expiredDate)) {
            return false;
        } else if (costPricePerItem <= 0) {
            System.out.println("\nStock price cannot be negative or 0");
            return false;
        } else if (sellingPricePerItem <= 0) {
            System.out.println("\nSelling price cannot be negative or 0");
            return false;
        } else if (stockQty <= 0 || stockQty > 100) {
            System.out.println("\nStock quantity cannot be less than 0 or more than 100");
            return false;
        }
        return true;
    }

    public boolean checkExpiredDateInput(String date) {
        if (date.trim().equals("")) { // check if date input is empty
            System.out.println("\nExpired Date cannot be blank");
            return false;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateFormat.setLenient(false);
            Date expiredDate = new Date();
            try {
                expiredDate = dateFormat.parse(date);
//                System.out.println(date + " is valid date format");
            } catch (ParseException e) {
                System.out.println("\nExpired Date format is not correct. Please check again");
                return false;
            }
            Date today = new Date();

            if (expiredDate.before(today)) {
                System.out.println("\nThe expired date must be in future date. Please check again");
                return false; // if expiredDate is the date before today = invalid;
            }
        }

        return true;
    }

    public void deleteStock() {
        int index = -1;
        Scanner scan = new Scanner(System.in);
        char continue_delete_stock = 'y';
        if (!stockList.isEmpty()) {
            do {
                displayStockDetails();
                do {
                    System.out.println("\nPlease enter the stock id (e to exit): ");
                    String stockID = scan.nextLine();
                    if (!stockID.toLowerCase().equals("e")) {
                        index = stockList.getIndex(new Stock(stockID));
                        Stock foundStock = (Stock) stockList.getElement(index);
                        if (index != -1) {
                            printStockFound(index);

                            boolean input = true;
                            char confirmation = 'n';
                            while (input) {

                                try {
                                    System.out.println("\nConfirmation to delete this stock (y = yes / n = no): ");
                                    confirmation = scan.next().toLowerCase().charAt(0);
                                    scan.nextLine();
                                } catch (Exception e) {
                                    invalidInputMsg();
                                }
                                if (confirmation == 'n' || confirmation == 'y') {
                                    input = false;
                                } else {
                                    invalidInputMsg();
                                }

                            }
                            if (confirmation == 'y') {
                                stockList.remove(index);
                                System.out.println("\nSuccessfully removed " + foundStock.getStockName() + 
                                        " from the system.");
                            }

                        } else {
                            System.out.println("\nThe stock does not exist in the system. Please enter the stock ID again.");
                        }
                    } else {
                        return;
                    }

                } while (index == -1);

                do {
                    try {
                        System.out.println("\nDo you want to continue to delete stocks (y = yes / n = no): ");
                        continue_delete_stock = scan.next().toLowerCase().charAt(0);
                        scan.nextLine();
                    } catch (Exception e) {
                        invalidInputMsg();
                    }
                    if (!(continue_delete_stock == 'n' || continue_delete_stock == 'y')) {
                        invalidInputMsg();
                    }

                } while (!(continue_delete_stock == 'n' || continue_delete_stock == 'y'));
            } while (continue_delete_stock == 'y');
        } else {
            System.out.println("You have no any stock record in the system, please add some stock.");
        }

    }

    public int displayUpdateMenu() {
        int choice = 0;
        Scanner input = new Scanner(System.in);
        try {
            System.out.println("\n------------------------------");
            System.out.println("|1. Update Stock Name        |");
            System.out.println("|2. Update Stock Category    |");
            System.out.println("|3. Update Stock Expired Date|");
            System.out.println("|4. Update Stock Price       |");
            System.out.println("|5. Update Selling Price     |");
            System.out.println("|6. Stock In                 |");
            System.out.println("|7. Stock Out                |");
            System.out.println("|8. Back to Stock Menu       |");
            System.out.println("------------------------------");

            System.out.println("Enter your choice(1 - 8): ");
            choice = input.nextInt();
        } catch (Exception e) {
        }

        return choice;

    }

    public void printStockFound(int index) {
        Stock foundStock = (Stock) stockList.getElement(index);
        System.out.println("\nStock Details ");
        System.out.println("---------------------");
        System.out.println("Stock ID            : " + foundStock.getStockID());
        System.out.println("Stock Name          : " + foundStock.getStockName());
        System.out.println("Stock Quantity      : " + foundStock.getStockQty());
        System.out.println("Stock Price (RM)    : " + foundStock.getCostPricePerItem());
        System.out.println("Selling Price (RM)  : " + foundStock.getSellingPricePerItem());
        System.out.println("Stock Expired Date  : " + foundStock.getOnlyDate(foundStock.getStockExpiredDate()));
        System.out.println("Stock Category      : " + foundStock.getStockCategory());
    }

    public void updateStock() {

        char continue_update_stock = 'n';
        int index = -1;
        if (!stockList.isEmpty()) {

            do {
                displayStockDetails();
                Scanner scan = new Scanner(System.in);
                do {

                    String stockID = " ";
                    System.out.println("\nPlease enter the stock id (e to exit): ");
                    stockID = scan.nextLine();
                    if (!stockID.toLowerCase().equals("e")) {
                        index = stockList.getIndex(new Stock(stockID));
                        if (index != -1) {
                            printStockFound(index);
                            int choice = 0;
                            do {
                                choice = displayUpdateMenu();
                                if (choice <= 0 || choice > 8) {
                                    System.out.println("Please input only numbers between 1 and 8.");
                                }
                            } while (choice <= 0 || choice > 8);
                            Stock tempStock = (Stock) stockList.getElement(index);
                            Stock tempStockModified = createTempStockObj(tempStock);
                            switch (choice) {
                                case 1:
                                    String newStockName = updateStockName();
                                    tempStockModified.setStockName(newStockName);
                                    break;
                                case 2:
                                    String newStockCategory = updateStockCategory();
                                    tempStockModified.setStockCategory(newStockCategory);
                                    break;
                                case 3:
                                    Calendar calendar = updateStockExpiredDate();
                                    tempStockModified.setStockExpiredDate(calendar);
                                    break;
                                case 4:
                                    double stockPrice = updateStockPrice();
                                    tempStockModified.setCostPricePerItem(stockPrice);
                                    break;
                                case 5:
                                    double sellingPrice = updateSellingPrice();
                                    tempStockModified.setSellingPricePerItem(sellingPrice);
                                    break;
                                case 6:
                                    int stockInQty = stockIn();
                                    tempStockModified.stockIn(stockInQty);
                                    break;
                                case 7:
                                    int stockOutQty = stockOut(tempStockModified.getStockQty());
                                    tempStockModified.stockOut(stockOutQty);
                                    break;
                                case 8:
                                    return;
                                default:
                                    break;

                            }

                            char update_confirmation = 'n';
                            printStockDetailsBeforeAfter(tempStock, tempStockModified);
                            do {

                                System.out.println("\nConfirmed to update (y = yes / n = no): ");
                                update_confirmation = scan.next().toLowerCase().charAt(0);
                                scan.nextLine();
                                if (!(update_confirmation == 'y' || update_confirmation == 'n')) {
                                    invalidInputMsg();
                                }

                            } while (!(update_confirmation == 'y' || update_confirmation == 'n'));

                            if (update_confirmation == 'y') {
                                stockList.update(index, tempStockModified);
                                System.out.println("\nStock " + tempStockModified.getStockID() + 
                                        " is successfully updated.");             
                            }
                            else{
                                String rpStr = tempStockModified.getStockID();
                                int rpQtyIn = reportList.getValue(rpStr).getStockInQty();
                                int rpQtyOut = reportList.getValue(rpStr).getStockInQty();
                                if(choice == 6){
                                    
                                    rpQtyIn -= rpCalculateStockIn(tempStock, tempStockModified);
                                    reportList.add(rpStr, new Report(tempStock.getStockName(), tempStock.getStockCategory(), rpQtyIn, rpQtyOut)); 
                                }
                                else if(choice == 7){
                                    
                                    rpQtyOut -= rpCalculateStockOut(tempStock, tempStockModified);
                                    reportList.add(rpStr, new Report(tempStock.getStockName(), tempStock.getStockCategory(), rpQtyIn, rpQtyOut));
                                }
                            }

                        } else {
                            System.out.println("Stock NOT FOUND! Please try again.");
                        }
                    } else {
                        return;
                    }

                } while (index == -1);

                do {

                    System.out.println("\nContinue to update another stock (y = yes / n = no): ");
                    continue_update_stock = scan.next().toLowerCase().charAt(0);

                    if (!(continue_update_stock == 'y' || continue_update_stock == 'n')) {
                        invalidInputMsg();
                    }
                } while (!(continue_update_stock == 'y' || continue_update_stock == 'n'));

            } while (continue_update_stock == 'y');
        } else {
            System.out.println("You have no any stock record in the system, please add some stock.");
        }

    }

    public void printStockDetailsBeforeAfter(Stock objBefore, Stock objAfter) {

        System.out.println("\n" + String.format("%-70s %-70s", "Before", "After"));
        System.out.println(String.format("%-70s %-70s", "------", "------"));
        System.out.println(String.format("%-70s %-70s", "Stock ID            : " + objBefore.getStockID(), "Stock ID            : " + objAfter.getStockID()));
        System.out.println(String.format("%-70s %-70s", "Stock Name          : " + objBefore.getStockName(), "Stock Name          : " + objAfter.getStockName()));
        System.out.println(String.format("%-70s %-70s", "Stock Quantity      : " + objBefore.getStockQty(), "Stock Quantity      : " + objAfter.getStockQty()));
        System.out.println(String.format("%-70s %-70s", "Stock Price (RM)    : " + objBefore.getCostPricePerItem(), "Stock Price (RM)    : " + objAfter.getCostPricePerItem()));
        System.out.println(String.format("%-70s %-70s", "Selling Price (RM)  : " + objBefore.getSellingPricePerItem(), "Selling Price (RM)  : " + objAfter.getSellingPricePerItem()));
        System.out.println(String.format("%-70s %-70s", "Stock Expired Date  : " + objBefore.getOnlyDate(objBefore.getStockExpiredDate()), "Stock Expired Date  : " + objAfter.getOnlyDate(objAfter.getStockExpiredDate())));
        System.out.println(String.format("%-70s %-70s", "Stock Category      : " + objBefore.getStockCategory(), "Stock Category      : " + objAfter.getStockCategory()));

        String rpStr = objBefore.getStockID();
        int rpQtyIn = 0;   
        int rpQtyOut = 0;

        if(objBefore.getStockQty() < objAfter.getStockQty())
            rpQtyIn += rpCalculateStockIn(objBefore, objAfter);
        else
            rpQtyOut += rpCalculateStockOut(objBefore, objAfter);
        
        reportList.add(rpStr, new Report(objAfter.getStockName(), objAfter.getStockCategory(), 
                                       reportList.getValue(rpStr).getStockInQty()+rpQtyIn, reportList.getValue(rpStr).getStockOutQty()+rpQtyOut));
    }

    public int rpCalculateStockIn (Stock objBefore, Stock objAfter){

        int rpStockInChange = objAfter.getStockQty() - objBefore.getStockQty();
        
        return rpStockInChange;
    }
    public int rpCalculateStockOut (Stock objBefore, Stock objAfter){
        
        int rpStockOutChange = objBefore.getStockQty() - objAfter.getStockQty();
        
        return rpStockOutChange;
    }
    
    public String updateStockName() {
        String newStockName = "";
        boolean input_validation = false;
        Scanner scan = new Scanner(System.in);
        while (!input_validation) {
            System.out.println("\nPlease enter the new stock name (Max 30 characters): ");
            newStockName = scan.nextLine();
            if (newStockName.length() > 0 && newStockName.length() < 30 && newStockName.isBlank() != true) {
                input_validation = true;
            } else {
                System.out.println("Stock name cannot be blank and not more than 30 characters");
            }
        }
        
        return newStockName.toUpperCase();

    }

    public Stock createTempStockObj(Stock obj) {
        Stock tempStockObj = new Stock();
        //String stockID, String stockName, int stockQty, double costPricePerItem, Calendar stockExpiredDate, String stockCategory
        tempStockObj.setStockID(obj.getStockID());
        tempStockObj.setStockName(obj.getStockName());
        tempStockObj.setStockQty(obj.getStockQty());
        tempStockObj.setCostPricePerItem(obj.getCostPricePerItem());
        tempStockObj.setStockExpiredDate(obj.getStockExpiredDate());
        tempStockObj.setStockCategory(obj.getStockCategory());
        tempStockObj.setSellingPricePerItem(obj.getSellingPricePerItem());

        return tempStockObj;
    }

    public String updateStockCategory() {
        String newStockCategory = "";
        boolean input_validation = true;
        Scanner scan = new Scanner(System.in);
        while (input_validation) {
            System.out.println("\nPlease enter the new stock category (food / drink / other): ");
            newStockCategory = scan.nextLine();
            if (newStockCategory.toLowerCase().equals("food") || newStockCategory.toLowerCase().equals("drink") || newStockCategory.toLowerCase().equals("other")) {
                input_validation = false;
            } else {
                System.out.println("\nOnly 'food' or 'drink' or 'other' is allowed for stock catergory");
            }
        }
        return newStockCategory.toUpperCase();
    }

    public Calendar updateStockExpiredDate() {
        Scanner scan = new Scanner(System.in);
        String expiredDate = "";
        do {
            System.out.println("\nEnter stock Expired Date (dd-mm-yyyy): ");
            expiredDate = scan.nextLine();

        } while (checkExpiredDateInput(expiredDate) != true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(expiredDate);
            calendar.setTime(date);
        } catch (ParseException p) {

        }
        return calendar;
    }

    public double updateStockPrice() {
        double price = 0.0;
        Scanner scan = new Scanner(System.in);

        do {
            try {
                System.out.println("\nNew stock price per item :");
                price = scan.nextDouble();
                if (price <= 0) {
                    System.out.println("\nNo zero or negative value is allowed.");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("\nPlease enter numbers only.");
            }

        } while (price <= 0);

        return price;
    }

    public double updateSellingPrice() {
        double sellingPrice = 0.0;
        Scanner scan = new Scanner(System.in);

        do {
            try {
                System.out.println("\nNew selling price per item :");
                sellingPrice = scan.nextDouble();
                if (sellingPrice <= 0) {
                    System.out.println("\nNo zero or negative value is allowed.");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("\nPlease enter numbers only.");
            }

        } while (sellingPrice <= 0);

        return sellingPrice;
    }

    public int stockIn() {
        Scanner scan = new Scanner(System.in);
        int stockInQty = -1;
        do {
            try {
                System.out.println("\nQuantity to stock in (min:1, max:100):");
                stockInQty = scan.nextInt();
                if (stockInQty <= 0 || stockInQty > 100) {
                    System.out.println("\nMinimum quantity is 1 and maximum quantity is 100. Please check again");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("\nPlease enter numbers only.");
            }

        } while (stockInQty <= 0 || stockInQty > 100); 
        
        return stockInQty;
    }

    public int stockOut(int quantityOnHand) {
        Scanner scan = new Scanner(System.in);
        int stockOutQty = -1;
        do {
            try {
                System.out.println("\nQuantity to stock out :");
                stockOutQty = scan.nextInt();
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("\nPlease enter numbers only.");
            }
            if (stockOutQty <= 0) {
                System.out.println("\nNo zero or negative value is allowed.");
            } else if (stockOutQty > quantityOnHand) {
                System.out.println("\nStock out quantity can not be higher than quantity on hand!");
            }
        } while (stockOutQty <= 0 || stockOutQty > quantityOnHand);
        
        return stockOutQty;
    }

    public void displayStockDetails() {
        System.out.println("\nAll Stock Details");
        System.out.println("=================");

        System.out.println("\n" + String.format("%-20s %-30s %-7s %-11s %-20s %-12s %-15s", "Stock ID", "Name", "Qty", "Price(RM)", "Selling Price(RM)", "Expired Date", "Stock Category"));
        System.out.println(String.format("%-20s %-30s %-7s %-11s %-20s %-12s %-15s", "====================", "==============================", "=======", "===========", "===================", "============", "==============="));
        if (!stockList.isEmpty()) {
            System.out.println(stockList);
            System.out.println("=========================================================================================================================");

        } else {
            System.out.println("No Records Found.");
            System.out.println("=========================================================================================================================");
        }
        System.out.println("Total " + stockList.getSize() + " stock record(s).");
        System.out.println("====================================");
    }

    public void searchStock() {
        int index = -1;
        char continue_search_stock = 'y';
        Scanner scan = new Scanner(System.in);
        do {
            do {
                index = -1;
                System.out.println("\nPlease enter the stock id (e to exit): ");
                String stockID = scan.nextLine();
                if (!stockID.toLowerCase().equals("e")) {
                    index = stockList.getIndex(new Stock(stockID));
                    Stock foundStock = (Stock) stockList.getElement(index);
                    if (index != -1) {
                        printStockFound(index);
                    } else {
                        System.out.println("\nThe stock does not exist in the system. Please enter the stock ID again.");
                    }
                } else {
                    return;
                }
            } while (index == -1);
            do {
                System.out.println("\nDo you want to continue search for stock details (y = yes / n = no): ");
                continue_search_stock = scan.next().toLowerCase().charAt(0);
                scan.nextLine();
                if (!(continue_search_stock == 'y' || continue_search_stock == 'n')) {
                    invalidInputMsg();
                }
            } while (!(continue_search_stock == 'y' || continue_search_stock == 'n'));
        } while (continue_search_stock == 'y');
    }

    public NoDuplicateLinkedList<Stock> readStockFromBinFile() {
        FileOutputStream fos;
        NoDuplicateLinkedList<Stock> stockList = new NoDuplicateLinkedList<>();
        try {
            // read back
            FileInputStream fis = new FileInputStream("stock.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            stockList = (NoDuplicateLinkedList) obj;

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return stockList;
    }

    public void writeToBinFile() {
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
    
    public MapInterface<String, Report>/**/ readReportFromBinFile() {
        FileOutputStream fos;
        MapInterface<String, Report> reportList = new HashTable<String, Report>();
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
