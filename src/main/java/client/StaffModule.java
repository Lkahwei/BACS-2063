package client;

import adt.*;
import entity.*;
import java.util.Calendar;
import java.util.Scanner;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Tan Kai Yuan
 */

public class StaffModule {

    //list
    //Login Module
    private ListInterface<Staff> staffList = new ArrList<>();

    public StaffModule() {
    }

    public void loginMain() throws FileNotFoundException {
        staffList = readStaffFromBinFile();
        readStaffFromBinFile2();
        staffCounterAdjuster();
        boolean logout = false;
        do {
            logout = false;
            if (login()) { // if is supervisor
                int supervisorChoice;
                do {
                    supervisorChoice = supervisorMenu();
                    switch (supervisorChoice) {
                        case 1:
                            staffMain();// staff management module;
                            break;
                        case 2:
                            StockModule stockModule = new StockModule();
                            stockModule.stockMain();
                            break;
                        case 3:
                            PaymentModule paymentModule = new PaymentModule();
                            paymentModule.paymentMain();
                            break;
                        case 4:
                            ReportModule reportModule = new ReportModule();
                            reportModule.reportMain();
                            break;
                        case 5:
                            logout = true;
                            System.out.println("\nLogged out successfully");
                            break;
                    }
                } while (supervisorChoice != 5);

            } else {
                int staffChoice;
                do {
                    staffChoice = normalStaffMenu();
                    switch (staffChoice) {
                        case 1:
                            StockModule stockModule = new StockModule();
                            stockModule.stockMain();
                            break;
                        case 2:
                            PaymentModule paymentModule = new PaymentModule();
                            paymentModule.paymentMain();
                            break;
                        case 3:
                            logout = true;
                            System.out.println("\nLogged out successfully");
                            break;

                    }
                } while (staffChoice != 3);

            }

        } while (logout);
    }

    public void staffMain() {

        int staffMainMenuSelection = 0;
        do {
            staffMainMenuSelection = staffModuleMainMenu();
            switch (staffMainMenuSelection) {
                case 1:
                    int staffManagementMenuSelection = 0;
                    do {
                        switch (staffManagementMenuSelection = staffManagementMenu()) {
                            case 1:
                                addNewStaff();
                                break;
                            case 2:
                                searchStaff();
                                break;
                            case 3:
                                modifyStaff();
                                break;
                            case 4:
                                removeStaff();
                                break;
                            case 5:
                                displayAllStaff();
                                break;
                            case 6:
                                System.out.println("Exit Staff Management Menu");
                                break;
                        }
                    } while (staffManagementMenuSelection != 6);

                    break;
                case 2:
                    int StaffPaymentMenuSelection = 0;
                    do {
                        switch (StaffPaymentMenuSelection = staffPaymentMenu()) {
                            case 1:
                                calculateMonthlyPayment();
                                break;
                            case 2:
                                displayAllMonthlyPayment();
                                break;
                            case 3:
                                clearAllMonthlyPayment();
                                break;
                            case 4:
                                System.out.println("Exit Staff Payment Menu");
                                break;
                        }
                    } while (StaffPaymentMenuSelection != 4);
                    break;
                case 3:
                    System.out.println("Exit Staff Module Menu");
                    writeToBinFile();
                    break;

            }

        } while (staffMainMenuSelection != 3);
        System.out.println("End StaffModule");

    }

    private void staffCounterAdjuster() {
        for (int i = 0; i < staffList.getSize(); i++) {
            if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.Supervisor")) {
                Supervisor.setCountSupervisor(Supervisor.getCountSupervisor() + 1);
            } else if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.NormalStaff")) {
                NormalStaff.setCountNormalStaff(NormalStaff.getCountNormalStaff() + 1);
            }
        }
    }

    public int supervisorMenu() {
        int selection = -1;
        Scanner scanner = new Scanner(System.in);
        boolean validity = false;
        System.out.println("\n\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Supervisor Menu                |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Staff Module                |");
        System.out.println("| 2. Stock Management            |");
        System.out.println("| 3. Order and Payment           |");
        System.out.println("| 4. Reporting                   |");
        System.out.println("| 5. Log Out                     |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 5) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please input integer between [1-5].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-5].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    public int normalStaffMenu() {
        int selection = -1;
        Scanner scanner = new Scanner(System.in);
        boolean validity = false;
        System.out.println("\n\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Staff Menu                     |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Stock Management            |");
        System.out.println("| 2. Order and Payment           |");
        System.out.println("| 3. Log Out                     |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 3) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please input integer between [1-3].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-3].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    public boolean login() {
        String tempStaffID, tempLoginPass;
        int positionNum = 0;
        int countLogin = 0;
        boolean getstaff = false;
        boolean isSupervisor = false;
        System.out.println("\n");
        System.out.println("------------------------ Staff Login -------------------------");
        do {
            tempStaffID = getLoginID();
            positionNum = loginGetPositionNum(tempStaffID);
            if (positionNum > 0) {
                if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.Supervisor")) {
                    Supervisor tempStaffObj = createTempStaffObj((Supervisor) staffList.getEntry(positionNum));
                    tempLoginPass = getLoginPassword();
                    if (tempLoginPass.equals(tempStaffObj.getLoginPass())) {
                        System.out.println("\nLogged In Successfully.");
                        System.out.println("\nWelcome Supervisor " + tempStaffObj.getStaffName() + ".");
                        getstaff = true;
                        isSupervisor = true;
                    } else {
                        System.out.println("\nWrong password is entered. Please try again.");
                        countLogin++;
                        System.out.println("\nYou have " + (3 - countLogin) + " attempts left.");
                    }

                } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.NormalStaff")) {
                    NormalStaff tempStaffObj = createTempStaffObj((NormalStaff) staffList.getEntry(positionNum));
                    tempLoginPass = getLoginPassword();
                    if (tempLoginPass.equals(tempStaffObj.getLoginPass())) {
                        System.out.println("\nLogged In Successfully.");
                        System.out.println("\nWelcome Normal Staff " + tempStaffObj.getStaffName() + ".");
                        getstaff = true;
                    } else {
                        System.out.println("\nWrong password is entered. Please try again.");
                        countLogin++;
                        System.out.println("\nYou have " + (3 - countLogin) + " attempts left.");
                    }
                } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.PartTimeStaff")) {
                    PartTimeStaff tempStaffObj = createTempStaffObj((PartTimeStaff) staffList.getEntry(positionNum));
                    tempLoginPass = getLoginPassword();
                    if (tempLoginPass.equals(tempStaffObj.getLoginPass())) {
                        System.out.println("\nLogged In Successfully.");
                        System.out.println("\nWelcome Part Time Staff " + tempStaffObj.getStaffName() + ".");
                        getstaff = true;
                    } else {
                        System.out.println("\nWrong password is entered. Please try again.");
                        countLogin++;
                        System.out.println("\nYou have " + (3 - countLogin) + " attempts left.");
                    }
                }
            } else {
                System.out.println("\nID NOT found. Please try again.");
                countLogin++;
                System.out.println("\nYou have " + (3 - countLogin) + " attempts left.");
            }
            if (countLogin == 3) {
                System.out.println("\nSystem down.");
                System.exit(0);
            }
        } while (!getstaff);
        return isSupervisor;
    }

    public String getLoginID() {
        Scanner scanner = new Scanner(System.in);
        String tempStaffID;
        System.out.println("\nStaff ID: ");
        tempStaffID = scanner.nextLine();
        return tempStaffID;
    }

    public String getLoginPassword() {
        Scanner scanner = new Scanner(System.in);
        String tempLoginPass;
        System.out.println("\nPassword: ");
        tempLoginPass = scanner.nextLine();
        return tempLoginPass;
    }

    public int loginGetPositionNum(String tempStaffID) {
        int positionNum = 0;
        if (staffList.contains(new Staff(tempStaffID))) {
            positionNum = staffList.getPosition(new Staff(tempStaffID));
        }
        return positionNum;
    }

    private ArrList<Staff> readStaffFromBinFile() {
        ArrList<Staff> staffList = new ArrList<>();
        try {
            // read back
            FileInputStream fis = new FileInputStream("staff.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            staffList = (ArrList) obj;

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return staffList;
    }

    private void readStaffFromBinFile2() {
        //FileOutputStream fos;
        int StaffNo = 0;
        try {
            // read back
            FileInputStream fis2 = new FileInputStream("staffNo.bin");
            DataInputStream ois2 = new DataInputStream(fis2);
            int staffNo = ois2.readInt();
            Staff.setStaffNo(staffNo);
        } catch (Exception e) {
            //    e.printStackTrace();
        }

    }

    public void writeToBinFile() {
        FileOutputStream fos;
        DataOutputStream fos2;
        try {
            fos = new FileOutputStream("staff.bin");
            fos2 = new DataOutputStream(new FileOutputStream("staffNo.bin"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            DataOutputStream oos2 = new DataOutputStream(fos2);
            oos.writeObject(staffList);
            System.out.println(Staff.getStaffNo());
            oos2.writeInt(Staff.getStaffNo());

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    // Staff Module
    public int staffModuleMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        boolean validity = false;
        System.out.println("\n\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Staff Module                   |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Staff Management            |");
        System.out.println("| 2. Staff Payment               |");
        System.out.println("| 3. Exit                        |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 3) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please input integer between [1-4].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-4].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    // Staff Management
    public int staffManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        boolean validity = false;
        System.out.println("\n\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Staff Management               |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Add New Staff               |");
        System.out.println("| 2. Search Existing Staff       |");
        System.out.println("| 3. Modify Existing Staff       |");
        System.out.println("| 4. Remove Existing Staff       |");
        System.out.println("| 5. Display All Staff Details   |");
        System.out.println("| 6. Exit                        |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 6) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please select integer between [1-6].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-6].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    public void addNewStaff() {
        boolean contAdd = false;
        do {
            String newStaffName, newStaffContact, newLoginPass;
            String newStaffType = "";
            String newPosition = "";
            double newBasicSalary = 0;
            double newWages = 0;
            Calendar newDateJoined = Calendar.getInstance();

            Scanner scanner = new Scanner(System.in);
            int givenPosition = 0;
            int staffTypeSelection = 0;
            int staffPositionSelection = 0;
            boolean validity;
            System.out.println("\n\n");
            System.out.println("----------------------- Add New Staff ------------------------");
            System.out.println("\nNew Staff ID: " + "S" + (Staff.getStaffNo() + 1));
            do {
                validity = false;
                System.out.println("\nPlease input Staff's Name (only allow [A-Z],[ ] and [/] OR e to exit): ");
                newStaffName = scanner.nextLine();
                newStaffName = newStaffName.toLowerCase();
                if (newStaffName.equals("e")) {
                    return;
                }
                validity = validateName(newStaffName);
                if (validity) {
                    newStaffName = newStaffName.toUpperCase();
                }
            } while (!validity);
            System.out.println("\n");
            System.out.println("+--------------------------------+");
            System.out.println("| Staff Type Available           |");
            System.out.println("+--------------------------------+");
            System.out.println("| 1. Full Time Staff             |");
            System.out.println("| 2. Part Time Staff             |");
            System.out.println("+--------------------------------+");
            do {
                validity = false;
                try {
                    System.out.println("\nPlease select Staff's Type: ");
                    staffTypeSelection = scanner.nextInt();

                    if (staffTypeSelection > 0 && staffTypeSelection <= 2) {
                        validity = true;
                    } else {
                        System.out.println("Invalid input. Please select integer [1] or [2].");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please input integer [1] or [2].");
                    scanner.nextLine();
                }
            } while (!validity);
            switch (staffTypeSelection) {
                case 1:
                    newStaffType = "Full Time";
                    break;
                case 2:
                    newStaffType = "Part Time";
                    break;
            }

            if (newStaffType.equals("Full Time")) {
                System.out.println("\n");
                System.out.println("+--------------------------------+");
                System.out.println("| Position Available             |");
                System.out.println("+--------------------------------+");
                System.out.println("| 1. Normal Staff                |");
                System.out.println("| 2. Supervisor                  |");
                System.out.println("+--------------------------------+");
                do {
                    validity = false;
                    try {
                        System.out.println("\nPlease select Staff's Position: ");
                        staffPositionSelection = scanner.nextInt();

                        if (staffPositionSelection > 0 && staffPositionSelection <= 2) {
                            validity = true;
                        } else {
                            System.out.println("Invalid input. Please select integer [1] or [2].");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please input integer [1] or [2].");
                        scanner.nextLine();
                    }
                } while (!validity);
                switch (staffPositionSelection) {
                    case 1:
                        newPosition = "Normal Staff";
                        givenPosition = 1 + Supervisor.getCountSupervisor() + NormalStaff.getCountNormalStaff();
                        break;
                    case 2:
                        newPosition = "Supervisor";
                        givenPosition = 1 + Supervisor.getCountSupervisor();
                        break;
                }
            }
            if (newStaffType.equals("Full Time")) {
                do {
                    validity = false;
                    try {
                        System.out.println("\nPlease input Staff's Basic Salary (min 1200.00): ");
                        newBasicSalary = scanner.nextDouble();
                        scanner.nextLine();
                        if (newBasicSalary >= 1200.00) {
                            validity = true;
                        } else {
                            System.out.println("Minimum Basic Salary is RM1200. Please input again.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please input number only.");
                        scanner.nextLine();
                    }
                } while (!validity);
            } else {
                do {
                    validity = false;
                    try {
                        System.out.println("\nPlease input Staff's Wages Per Hour: ");
                        newWages = scanner.nextDouble();
                        scanner.nextLine();
                        if (newWages > 0) {
                            validity = true;
                        } else {
                            System.out.println("Invalid number, please input positive number only.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please input number only.");
                        scanner.nextLine();
                    }
                } while (!validity);
            }
            do {
                validity = false;
                System.out.println("\nPlease input Staff's Contact Number (01X-XXXXXXX / 011-XXXXXXXX): ");
                newStaffContact = scanner.nextLine();
                validity = validateStaffContact(newStaffContact);
            } while (!validity);

            do {
                validity = false;
                System.out.println("\nPlease create Login Password for this ID (min 8 characters): ");
                newLoginPass = scanner.nextLine();
                if (newLoginPass.length() >= 8) {
                    validity = true;
                } else {
                    System.out.println("Minimum 8 characters for Login Password. Please input again.");
                }
            } while (!validity);

            String confirmAdd = "";
            System.out.println("\nConfirm add this staff? (y = yes / others = no):");
            confirmAdd = scanner.nextLine();
            confirmAdd = confirmAdd.toLowerCase();
            if (confirmAdd.equals("y") || confirmAdd.equals("yes")) {
                switch (staffPositionSelection) {
                    case 1:
                        if (staffList.add(givenPosition, new NormalStaff(newStaffName, newStaffType, newStaffContact, newDateJoined, newLoginPass, newPosition, newBasicSalary))) {
                            System.out.println("\nNew Normal Staff is added successfully.");
                            printNormalStaff(givenPosition);
                        }
                        break;
                    case 2:
                        if (staffList.add(givenPosition, new Supervisor(newStaffName, newStaffType, newStaffContact, newDateJoined, newLoginPass, newPosition, newBasicSalary))){
                            System.out.println("\nNew Supervisor is added successfully.");
                            printSupervisor(givenPosition);
                        };  
                        break;
                }

                if (staffTypeSelection == 2) {
                    if (staffList.add(new PartTimeStaff(newStaffName, newStaffType, newStaffContact, newDateJoined, newLoginPass, newWages))){
                        System.out.println("\nNew Part Time Staff is added successfully.");
                        printPartTimeStaff(staffList.getSize());
                    };
                }
            }
            String confirmCont = "";
            System.out.println("\nAdd another new staff? (y = yes / others = no): ");
            confirmCont = scanner.nextLine();
            confirmCont = confirmCont.toLowerCase();
            if (confirmCont.equals("y") || confirmCont.equals("yes")) {
                contAdd = true;
            } else {
                contAdd = false;
            }
        } while (contAdd);

    }

    public void searchStaff() {
        boolean getStaff = false;
        boolean contSearch = false;
        System.out.println("\n\n");
        System.out.println("------------------- Search Existing Staff --------------------");
        if (!staffList.isEmpty()) {
            do {
                int positionNum = 0;
                do {
                    String tempStaffID = findStaff();
                    tempStaffID = tempStaffID.toLowerCase();
                    if (!tempStaffID.equals("e")) {
                        if (tempStaffID.length() == 5) {
                            tempStaffID = tempStaffID.toUpperCase();
                            if (tempStaffID.charAt(0) == 'S') {
                                positionNum = getPositionNum(tempStaffID);
                                printStaff(tempStaffID, positionNum);
                                getStaff = true;
                                Scanner scanner = new Scanner(System.in);
                                String confirmCont = "";
                                System.out.println("\nSearch another existing staff? (y = yes / others = no): ");
                                confirmCont = scanner.nextLine();
                                confirmCont = confirmCont.toLowerCase();
                                if (confirmCont.equals("y") || confirmCont.equals("yes")) {
                                    contSearch = true;
                                } else {
                                    contSearch = false;
                                }
                            } else {
                                System.out.println("Incorrent Staff ID format. Please input again.");
                            }
                        } else {
                            System.out.println("Incorrent Staff ID format. Please input again.");
                        }
                    } else {
                        contSearch = false;
                        getStaff = true;
                    }
                } while (contSearch);
            } while (!getStaff);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nThe Staff List is empty. No Staff can be searched.\n");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        }

    }

    public void modifyStaff() {
        boolean getStaff = false;
        boolean contModify = false;
        boolean contModifyDetails = false;
        System.out.println("\n\n");
        System.out.println("------------------- Modify Existing Staff --------------------");
        if (!staffList.isEmpty()) {
            do {
                int positionNum = 0;
                do {
                    String tempStaffID = findStaff();
                    tempStaffID = tempStaffID.toLowerCase();
                    if (!tempStaffID.equals("e")) {
                        if (tempStaffID.length() == 5) {
                            tempStaffID = tempStaffID.toUpperCase();
                            if (tempStaffID.charAt(0) == 'S') {
                                positionNum = getPositionNum(tempStaffID);
                                printStaff(tempStaffID, positionNum);
                                getStaff = true;
                                if (positionNum > 0) {
                                    do {
                                        if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.Supervisor")) {
                                            Supervisor tempStaffObj = createTempStaffObj((Supervisor) staffList.getEntry(positionNum));
                                            int selection = fullTimeStaffModifyMenu();
                                            switch (selection) {
                                                case 1:
                                                    modifyName(tempStaffObj);
                                                    break;
                                                case 2:
                                                    modifyBasicSalary(tempStaffObj);
                                                    break;
                                                case 3:
                                                    modifyContactNum(tempStaffObj);
                                                    break;
                                            }
                                            if (staffList.replace(positionNum, tempStaffObj)){
                                                System.out.println("\nModify Staff " + tempStaffObj.getStaffID() + " successfully.");
                                            };

                                        } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.NormalStaff")) {
                                            NormalStaff tempStaffObj = createTempStaffObj((NormalStaff) staffList.getEntry(positionNum));
                                            int selection = fullTimeStaffModifyMenu();
                                            switch (selection) {
                                                case 1:
                                                    modifyName(tempStaffObj);
                                                    break;
                                                case 2:
                                                    modifyBasicSalary(tempStaffObj);
                                                    break;
                                                case 3:
                                                    modifyContactNum(tempStaffObj);
                                                    break;
                                            }
                                            if (staffList.replace(positionNum, tempStaffObj)){
                                                System.out.println("\nModify Staff " + tempStaffObj.getStaffID() + " successfully.");
                                            };
                                        } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.PartTimeStaff")) {
                                            PartTimeStaff tempStaffObj = createTempStaffObj((PartTimeStaff) staffList.getEntry(positionNum));
                                            int selection = partTimeStaffModifyMenu();
                                            switch (selection) {
                                                case 1:
                                                    modifyName(tempStaffObj);
                                                    break;
                                                case 2:
                                                    modifyWages(tempStaffObj);
                                                    break;
                                                case 3:
                                                    modifyContactNum(tempStaffObj);
                                                    break;
                                            }
                                            if (staffList.replace(positionNum, tempStaffObj)){
                                                System.out.println("\nModify Staff " + tempStaffObj.getStaffID() + " successfully.");
                                            };
                                        }
                                        Scanner scanner = new Scanner(System.in);
                                        String confirmCont = "";
                                        System.out.println("\nModify another Details of this Staff? (y = yes / others = no): ");
                                        confirmCont = scanner.nextLine();
                                        confirmCont = confirmCont.toLowerCase();
                                        if (confirmCont.equals("y") || confirmCont.equals("yes")) {
                                            contModifyDetails = true;
                                        } else {
                                            contModifyDetails = false;
                                            printStaff(tempStaffID, positionNum);
                                            System.out.println("\nPress any key to continue...");
                                            scanner.nextLine();
                                        }
                                    } while (contModifyDetails);
                                }
                                Scanner scanner = new Scanner(System.in);
                                String confirmCont = "";
                                System.out.println("\nModify another existing Staff? (y = yes / others = no): ");
                                confirmCont = scanner.nextLine();
                                confirmCont = confirmCont.toLowerCase();
                                if (confirmCont.equals("y") || confirmCont.equals("yes")) {
                                    contModify = true;
                                } else {
                                    contModify = false;
                                }
                            } else {
                                System.out.println("Incorrent Staff ID format. Please input again.");
                            }
                        } else {
                            System.out.println("Incorrent Staff ID format. Please input again.");
                        }
                    } else {
                        getStaff = true;
                        contModify = false;
                    }
                } while (contModify);
            } while (!getStaff);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nThe Staff List is empty. No Staff can be modified.\n");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        }
    }

    public void removeStaff() {
        boolean getStaff = false;
        boolean contRemove = false;
        System.out.println("\n\n");
        System.out.println("------------------- Remove Existing Staff --------------------");
        if (!staffList.isEmpty()) {
            do {
                int positionNum = 0;
                do {
                    String tempStaffID = findStaff();
                    tempStaffID = tempStaffID.toLowerCase();
                    if (!tempStaffID.equals("e")) {
                        if (tempStaffID.length() == 5) {
                            tempStaffID = tempStaffID.toUpperCase();
                            if (tempStaffID.charAt(0) == 'S') {
                                positionNum = getPositionNum(tempStaffID);
                                printStaff(tempStaffID, positionNum);
                                boolean removeSuccessful = false;
                                getStaff = true;
                                if (positionNum > 0) {
                                    String confirmation = confirmRemove();
                                    if (confirmation.equals("y") || confirmation.equals("yes")) {
                                        if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.Supervisor")) {
                                            Supervisor.setCountSupervisor(Supervisor.getCountSupervisor() - 1);
                                        } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.NormalStaff")) {
                                            NormalStaff.setCountNormalStaff(NormalStaff.getCountNormalStaff() - 1);
                                        }
                                        removeSuccessful = staffList.remove(new Staff(tempStaffID));
                                        //removeSuccessful = true;
                                    }
                                }
                                if (removeSuccessful) {
                                    System.out.println("\nStaff " + tempStaffID + " is removed successfully.");
                                } else {
                                    System.out.println("\nRemove failed.");
                                }
                                Scanner scanner = new Scanner(System.in);
                                String confirmCont = "";
                                System.out.println("\nRemove another existing staff? (y = yes / others = no): ");
                                confirmCont = scanner.nextLine();
                                confirmCont = confirmCont.toLowerCase();
                                if (confirmCont.equals("y") || confirmCont.equals("yes")) {
                                    contRemove = true;
                                } else {
                                    contRemove = false;
                                }
                            } else {
                                System.out.println("Incorrent Staff ID format. Please input again.");
                            }
                        } else {
                            System.out.println("Incorrent Staff ID format. Please input again.");
                        }
                    } else {
                        contRemove = false;
                        getStaff = true;
                    }
                } while (contRemove);
            } while (!getStaff);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nThe Staff List is empty. No Staff can be removed.\n");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        }
    }

    public int fullTimeStaffModifyMenu() {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        boolean validity = false;
        System.out.println("\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Full Time Staff Modify Menu    |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Staff Name                  |");
        System.out.println("| 2. Basic Salary                |");
        System.out.println("| 3. Contact Number              |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 3) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please select integer between [1-3].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-3].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    public int partTimeStaffModifyMenu() {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        boolean validity = false;
        System.out.println("\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Part Time Staff Modify Menu    |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Staff Name                  |");
        System.out.println("| 2. Wages Per Hour              |");
        System.out.println("| 3. Contact Number              |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 3) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please select integer between [1-3].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-3].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    public void modifyName(Supervisor tempStaffObj) {
        boolean validity = false;
        String tempStaffName;
        do {
            tempStaffName = askName();
            validity = validateName(tempStaffName);
        } while (!validity);
        tempStaffName = tempStaffName.toUpperCase();
        tempStaffObj.setStaffName(tempStaffName);
    }

    public void modifyName(NormalStaff tempStaffObj) {
        boolean validity = false;
        String tempStaffName;
        do {
            tempStaffName = askName();
            validity = validateName(tempStaffName);
        } while (!validity);
        tempStaffName = tempStaffName.toUpperCase();
        tempStaffObj.setStaffName(tempStaffName);
    }

    public void modifyName(PartTimeStaff tempStaffObj) {
        boolean validity = false;
        String tempStaffName;
        do {
            tempStaffName = askName();
            validity = validateName(tempStaffName);
        } while (!validity);
        tempStaffName = tempStaffName.toUpperCase();
        tempStaffObj.setStaffName(tempStaffName);
    }

    public String askName() {
        Scanner scanner = new Scanner(System.in);
        String tempStaffName;
        System.out.println("\nPlease input Staff's New Name ( only allow [A-Z],[ ] and [/] ): ");
        tempStaffName = scanner.nextLine();
        return tempStaffName;
    }

    public void modifyBasicSalary(Supervisor tempStaffObj) {
        boolean validity = false;
        double tempBasicSalary;
        do {
            try {
                tempBasicSalary = askBasicSalary();
                if (tempBasicSalary >= 1200.00) {
                    validity = true;
                    tempStaffObj.setBasicSalary(tempBasicSalary);
                } else {
                    System.out.println("Minimum Basic Salary is RM1200. Please input again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input number only.");
            }
        } while (!validity);
    }

    public void modifyBasicSalary(NormalStaff tempStaffObj) {
        boolean validity = false;
        double tempBasicSalary;
        do {
            try {
                tempBasicSalary = askBasicSalary();
                if (tempBasicSalary >= 1200.00) {
                    validity = true;
                    tempStaffObj.setBasicSalary(tempBasicSalary);
                    tempStaffObj.setOtRate(tempBasicSalary);
                } else {
                    System.out.println("Minimum Basic Salary is RM1200. Please input again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input number only.");
            }
        } while (!validity);
    }

    public double askBasicSalary() {
        Scanner scanner = new Scanner(System.in);
        double tempBasicSalary;
        System.out.println("\nPlease input Staff's New Basic Salary (min 1200.00): ");
        tempBasicSalary = scanner.nextDouble();
        scanner.nextLine();
        return tempBasicSalary;
    }

    public void modifyWages(PartTimeStaff tempStaffObj) {
        boolean validity = false;
        double tempWages;
        do {
            validity = false;
            try {
                tempWages = askWages();
                if (tempWages > 0) {
                    validity = true;
                } else {
                    System.out.println("Invalid number. Please input positive number only.");
                }
                tempStaffObj.setWages(tempWages);
            } catch (Exception e) {
                System.out.println("Invalid input. Please input number only.");
            }
        } while (!validity);
    }

    public double askWages() {
        Scanner scanner = new Scanner(System.in);
        double tempWages;
        System.out.println("\nPlease input Staff's New Wages Per Hour: ");
        tempWages = scanner.nextDouble();
        return tempWages;
    }

    public void modifyContactNum(Supervisor tempStaffObj) {
        boolean validity = false;
        String tempContactNum;
        do {
            tempContactNum = askContactNum();
            validity = validateStaffContact(tempContactNum);
        } while (!validity);
        tempStaffObj.setStaffContact(tempContactNum);
    }

    public void modifyContactNum(NormalStaff tempStaffObj) {
        String tempContactNum = askContactNum();
        tempStaffObj.setStaffContact(tempContactNum);
    }

    public void modifyContactNum(PartTimeStaff tempStaffObj) {
        String tempContactNum = askContactNum();
        tempStaffObj.setStaffContact(tempContactNum);
    }

    public String askContactNum() {
        Scanner scanner = new Scanner(System.in);
        String tempContactNum;
        System.out.println("\nPlease input Staff's New Contact Number (01X-XXXXXXX / 011-XXXXXXXX): ");
        tempContactNum = scanner.nextLine();
        return tempContactNum;
    }

    public String confirmRemove() {
        Scanner scanner = new Scanner(System.in);
        String confirmation = "";
        System.out.println("\nConfirm remove? (y = yes / others = no): ");
        confirmation = scanner.nextLine();
        confirmation = confirmation.toLowerCase();
        return confirmation;
    }

    public void displayAllStaff() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n");
        System.out.println("+---------------------------------------------------- Staff List ---------------------------------------------------+");
        System.out.println(String.format("| %62s%s%44s |", " ", "Wages /", " "));
        System.out.println(String.format("| %-2s  %-5s   %-20s   %-9s   %-12s   %-12s  %-6s    %-12s   %-10s |", "NO", "ID", "Name", "Type", "Position", "Basic Salary", "OT Rate", "Contact", "Date Joined"));
        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
        if (!staffList.isEmpty()) {
            for (int i = 0; i < staffList.getSize(); i++) {
                System.out.println(String.format("| %-2s  " + staffList.getEntry(i + 1) + "  |", (i + 1)));
            }
            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
            System.out.println("\nTotal " + staffList.getSize() + " Staff are displayed.\n");
        } else {
            System.out.println("\nThe Staff List is empty. No Staff can be displayed.\n");
        }
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    public void printStaff(String tempStaffID, int positionNum) {
        if (staffList.contains(new Staff(tempStaffID))) {
            if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.Supervisor")) {
                printSupervisor(positionNum);
            } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.NormalStaff")) {
                printNormalStaff(positionNum);
            } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.PartTimeStaff")) {
                printPartTimeStaff(positionNum);
            }
        }
    }

    public void printSupervisor(int positionNum) {
        Supervisor tempStaffObj = (Supervisor) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("------------------------ Staff Record ------------------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println("Staff Type      : " + tempStaffObj.getStaffType());
        System.out.println("Staff Position  : " + tempStaffObj.getPosition());
        System.out.println(String.format("%s%,9.2f", "Basic Salary    : RM", tempStaffObj.getBasicSalary()));
        System.out.println("Contact Number  : " + tempStaffObj.getStaffContact());
        System.out.println("Date Joined     : " + tempStaffObj.getDate(tempStaffObj.getDateJoined()));
    }

    public void printNormalStaff(int positionNum) {
        NormalStaff tempStaffObj = (NormalStaff) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("------------------------ Staff Record ------------------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println("Staff Type      : " + tempStaffObj.getStaffType());
        System.out.println("Staff Position  : " + tempStaffObj.getPosition());
        System.out.println(String.format("%s%,9.2f", "Basic Salary    : RM", tempStaffObj.getBasicSalary()));
        System.out.println(String.format("%s%,9.2f", "OT Rate Per Hour: RM", tempStaffObj.getOtRate()));
        System.out.println("Contact Number  : " + tempStaffObj.getStaffContact());
        System.out.println("Date Joined     : " + tempStaffObj.getDate(tempStaffObj.getDateJoined()));
    }

    public void printPartTimeStaff(int positionNum) {
        PartTimeStaff tempStaffObj = (PartTimeStaff) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("------------------------ Staff Record ------------------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println("Staff Type      : " + tempStaffObj.getStaffType());
        System.out.println(String.format("%s%,9.2f", "Wages Per Hour  : RM", tempStaffObj.getWages()));
        System.out.println("Contact Number  : " + tempStaffObj.getStaffContact());
        System.out.println("Date Joined     : " + tempStaffObj.getDate(tempStaffObj.getDateJoined()));
    }

    public boolean validateBlank(String input) {
        boolean validity = true;
        if (input.isBlank()) {
            System.out.println("No empty field is allowed. Please input again.");
            validity = false;
        }
        return validity;
    }

    public boolean validateName(String name) {
        boolean validity = false;
        validity = validateBlank(name);
        if (!name.matches("[a-zA-Z/ ]+") && validity == true) {
            System.out.println("Incorrent Name format. Please input again.");
            validity = false;
        }
        return validity;
    }

    public boolean validateStaffContact(String staffContact) {
        boolean validity = false;
        validity = validateBlank(staffContact);
        if (validity) {
            if (staffContact.length() >= 11 && staffContact.length() <= 12) {
                if (staffContact.charAt(2) == '1') {
                    validity = (staffContact.length() == 12 && staffContact.matches("[0]{1}[1]{1}[1][-]\\d{8}"));
                } else {
                    validity = (staffContact.length() == 11 && staffContact.matches("[0]{1}[1]{1}[^1][-]\\d{7}"));
                }
                if (!validity) {
                    System.out.println("Incorrent Contact Number format. Please input again.");
                }
            } else {
                System.out.println("Incorrent Contact Number format. Please input again.");
                validity = false;
            }
        }
        return validity;
    }

    // Staff Payment
    public int staffPaymentMenu() {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        boolean validity = false;
        System.out.println("\n\n");
        System.out.println("+--------------------------------+");
        System.out.println("| Staff Payment                  |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Calculate Monthly Payment   |");
        System.out.println("| 2. Display All Monthly Payment |");
        System.out.println("| 3. Clear All Monthly Payment   |");
        System.out.println("| 4. Exit                        |");
        System.out.println("+--------------------------------+");
        System.out.println("\nEnter selection: ");
        do {
            try {
                selection = scanner.nextInt();

                if (selection > 0 && selection <= 4) {
                    validity = true;
                } else {
                    System.out.println("Invalid input. Please select integer between [1-4].");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer between [1-4].");
                scanner.nextLine();
            }
        } while (!validity);
        return selection;
    }

    public void calculateMonthlyPayment() {
        boolean getStaff = false;
        boolean contCalculate = false;
        System.out.println("\n\n");
        System.out.println("----------------- Calculate Monthly Payment ------------------");
        if (!staffList.isEmpty()) {
            do {
                do {
                    int positionNum = 0;
                    String tempStaffID = findStaff();
                    tempStaffID = tempStaffID.toLowerCase();
                    if (!tempStaffID.equals("e")) {
                        if (tempStaffID.length() == 5) {
                            tempStaffID = tempStaffID.toUpperCase();
                            if (tempStaffID.charAt(0) == 'S') {
                                positionNum = getPositionNum(tempStaffID);
                                printStaffCalculation(tempStaffID, positionNum);
                                getStaff = true;
                                if (positionNum > 0) {
                                    if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.Supervisor")) {
                                        Supervisor tempStaffObj = createTempStaffObj((Supervisor) staffList.getEntry(positionNum));
                                        insertAllowance(tempStaffObj);
                                        staffList.replace(positionNum, tempStaffObj);
                                        printSupervisorMonthlyPay(positionNum);
                                    } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.NormalStaff")) {
                                        NormalStaff tempStaffObj = createTempStaffObj((NormalStaff) staffList.getEntry(positionNum));
                                        insertOtHour(tempStaffObj);
                                        staffList.replace(positionNum, tempStaffObj);
                                        printNormalStaffMonthlyPay(positionNum);
                                    } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.PartTimeStaff")) {
                                        PartTimeStaff tempStaffObj = createTempStaffObj((PartTimeStaff) staffList.getEntry(positionNum));
                                        insertWorkHour(tempStaffObj);
                                        staffList.replace(positionNum, tempStaffObj);
                                        printPartTimeStaffMonthlyPay(positionNum);
                                    }
                                }
                                Scanner scanner = new Scanner(System.in);
                                String confirmCont = "";
                                System.out.println("\nCalculate another Staff Monthly Payment? (y = yes / others = no): ");
                                confirmCont = scanner.nextLine();
                                confirmCont = confirmCont.toLowerCase();
                                if (confirmCont.equals("y") || confirmCont.equals("yes")) {
                                    contCalculate = true;
                                } else {
                                    contCalculate = false;
                                }
                            } else {
                                System.out.println("Incorrent Staff ID format. Please input again.");
                            }
                        } else {
                            System.out.println("Incorrent Staff ID format. Please input again.");
                        }
                    } else {
                        contCalculate = false;
                        getStaff = true;
                    }
                } while (contCalculate);
            } while (!getStaff);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nThe Staff List is empty. No Monthly Payment can be calculated.\n");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        }
    }

    public void displayAllMonthlyPayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n");
        System.out.println("+---------------------------------------------------- Staff Monthly Payment List ------------------------------------------------+");
        System.out.println(String.format("| %62s%s%17s%s%6s%s%15s |", " ", "Wages / ", "", "Work / ", " ", "Allowance /", " "));
        System.out.println(String.format("| %-2s  %-5s   %-20s   %-9s   %-12s   %-11s  %-8s   %-6s      %-11s   %-11s |", "NO", "ID", "Name", "Type", "Position", "Basic Salary", "OT Rate", "OT Hour", "OT Salary", "Total Salary"));
        System.out.println("+--------------------------------------------------------------------------------------------------------------------------------+");
        if (!staffList.isEmpty()) {
            for (int i = 0; i < staffList.getSize(); i++) {
                if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.Supervisor")) {
                    Supervisor tempStaffObj = createTempStaffObj((Supervisor) staffList.getEntry(i + 1));
                    System.out.println(String.format("| %-2s  %-5s   %-20s   %-9s   %-12s   RM%,9.2f     %6s     %4s       RM%,9.2f   RM%,9.2f  |",
                            i + 1, tempStaffObj.getStaffID(), tempStaffObj.getStaffName(), tempStaffObj.getStaffType(),
                            tempStaffObj.getPosition(), tempStaffObj.getBasicSalary(), "", "",
                            tempStaffObj.getAllowance(), tempStaffObj.calculateMonthlyPay()));
                } else if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.NormalStaff")) {
                    NormalStaff tempStaffObj = createTempStaffObj((NormalStaff) staffList.getEntry(i + 1));
                    System.out.println(String.format("| %-2s  %-5s   %-20s   %-9s   %-12s   RM%,9.2f   RM%,6.2f     %-4d       RM%,9.2f   RM%,9.2f  |",
                            i + 1, tempStaffObj.getStaffID(), tempStaffObj.getStaffName(), tempStaffObj.getStaffType(),
                            tempStaffObj.getPosition(), tempStaffObj.getBasicSalary(), tempStaffObj.getOtRate(), tempStaffObj.getOtHour(),
                            tempStaffObj.calculateOtPay(), tempStaffObj.calculateMonthlyPay()));
                } else if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.PartTimeStaff")) {
                    PartTimeStaff tempStaffObj = createTempStaffObj((PartTimeStaff) staffList.getEntry(i + 1));
                    System.out.println(String.format("| %-2s  %-5s   %-20s   %-9s   %12s   RM%,9.2f     %6s     %-4d         %9s   RM%,9.2f  |",
                            i + 1, tempStaffObj.getStaffID(), tempStaffObj.getStaffName(), tempStaffObj.getStaffType(),
                            "", tempStaffObj.getWages(), "", tempStaffObj.getWorkHour(),
                            "", tempStaffObj.calculateMonthlyPay()));
                }
            }
            System.out.println("+--------------------------------------------------------------------------------------------------------------------------------+");
            System.out.println("\nTotal " + staffList.getSize() + " Staff Monthly Payment are displayed.\n");
        } else {
            System.out.println("\nThe Staff List is empty. No Monthly Payment can be displayed.\n");
        }
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    public void clearAllMonthlyPayment() {
        Scanner scanner = new Scanner(System.in);
        String confirmClear = "";
        System.out.println("\n\n");
        System.out.println("----------------- Clear All Monthly Payment ------------------");
        if (!staffList.isEmpty()) {
            System.out.println("\nConfirm Clear All Monthly Payment? (y = yes / others = no): ");
            confirmClear = scanner.nextLine();
            confirmClear = confirmClear.toLowerCase();
            if (confirmClear.equals("y") || confirmClear.equals("yes")) {
                for (int i = 0; i < staffList.getSize(); i++) {
                    if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.Supervisor")) {
                        Supervisor tempStaffObj = createTempStaffObj((Supervisor) staffList.getEntry(i + 1));
                        clearAllowance(tempStaffObj);
                        staffList.replace(i + 1, tempStaffObj);
                    } else if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.NormalStaff")) {
                        NormalStaff tempStaffObj = createTempStaffObj((NormalStaff) staffList.getEntry(i + 1));
                        clearOTHour(tempStaffObj);
                        staffList.replace(i + 1, tempStaffObj);
                    } else if (staffList.getEntry(i + 1).getClass().toString().equals("class entity.PartTimeStaff")) {
                        PartTimeStaff tempStaffObj = createTempStaffObj((PartTimeStaff) staffList.getEntry(i + 1));
                        clearWorkHour(tempStaffObj);
                        staffList.replace(i + 1, tempStaffObj);
                    }
                }
                System.out.println("\nAll Monthly Payment are cleared.");
            } else {
                System.out.println("\nClear Failed.");
            }
        } else {
            System.out.println("\nThe Staff List is empty. No Monthly Payment can be cleared.\n");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        }
    }

    public void printStaffCalculation(String tempStaffID, int positionNum) {
        if (staffList.contains(new Staff(tempStaffID))) {
            if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.Supervisor")) {
                printSupervisorCalculation(positionNum);
            } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.NormalStaff")) {
                printNormalStaffCalculation(positionNum);
            } else if (staffList.getEntry(positionNum).getClass().toString().equals("class entity.PartTimeStaff")) {
                printPartTimeStaffCalculation(positionNum);
            }
        }
    }

    public void printSupervisorCalculation(int positionNum) {
        Supervisor tempStaffObj = (Supervisor) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("--------------- Supervisor Payment Calculation ---------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println(String.format("%s%,9.2f", "Basic Salary    : RM", tempStaffObj.getBasicSalary()));
    }

    public void printSupervisorMonthlyPay(int positionNum) {
        Supervisor tempStaffObj = (Supervisor) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("------------------- Supervisor Monthly Pay -------------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println(String.format("%s%,9.2f", "Basic Salary    : RM", tempStaffObj.getBasicSalary()));
        System.out.println(String.format("%s%,9.2f", "Allowance       : RM", tempStaffObj.getAllowance()));
        System.out.println(String.format("\n%s%,9.2f", "Total Salary    : RM", tempStaffObj.calculateMonthlyPay()));
    }

    public void printNormalStaffCalculation(int positionNum) {
        NormalStaff tempStaffObj = (NormalStaff) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("-------------- Normal Staff Payment Calculation --------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println(String.format("%s%,9.2f", "Basic Salary    : RM", tempStaffObj.getBasicSalary()));
        System.out.println(String.format("%s%,9.2f", "OT Rate Per Hour: RM", tempStaffObj.getOtRate()));
    }

    public void printNormalStaffMonthlyPay(int positionNum) {
        NormalStaff tempStaffObj = (NormalStaff) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("------------------ Normal Staff Monthly Pay ------------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println(String.format("%s%,9.2f", "Basic Salary    : RM", tempStaffObj.getBasicSalary()));
        System.out.println(String.format("%s%,9.2f", "OT Rate Per Hour: RM", tempStaffObj.getOtRate()));
        System.out.println(String.format("%s%9d", "OT Hour Count   :   ", tempStaffObj.getOtHour()));
        System.out.println(String.format("%s%,9.2f", "Total Salary    : RM", tempStaffObj.calculateOtPay()));
        System.out.println(String.format("\n%s%,9.2f", "Total Salary    : RM", tempStaffObj.calculateMonthlyPay()));

    }

    public void printPartTimeStaffCalculation(int positionNum) {
        PartTimeStaff tempStaffObj = (PartTimeStaff) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("------------ Part Time Staff Payment Calculation -------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println(String.format("%s%,9.2f", "Wages Per Hour  : RM", tempStaffObj.getWages()));
    }

    public void printPartTimeStaffMonthlyPay(int positionNum) {
        PartTimeStaff tempStaffObj = (PartTimeStaff) staffList.getEntry(positionNum);
        System.out.println("\n");
        System.out.println("---------------- Part Time Staff Monthly Pay -----------------");
        System.out.println("Staff ID        : " + tempStaffObj.getStaffID());
        System.out.println("Staff Name      : " + tempStaffObj.getStaffName());
        System.out.println(String.format("%s%,9.2f", "Wages Per Hour  : RM", tempStaffObj.getWages()));
        System.out.println(String.format("%s%9d", "Work Hour Count :   ", tempStaffObj.getWorkHour()));
        System.out.println(String.format("\n%s%,9.2f", "Total Salary    : RM", tempStaffObj.calculateMonthlyPay()));
    }

    public void insertAllowance(Supervisor tempStaffObj) {
        boolean validity;
        double tempAllowance;
        do {
            validity = false;
            try {
                tempAllowance = askAllowance();
                if (tempAllowance > 0) {
                    validity = true;
                } else {
                    System.out.println("Invalid number. Please input positive number only.");
                }

                tempStaffObj.setAllowance(tempAllowance);
            } catch (Exception e) {
                System.out.println("Invalid input. Please input number only.");
            }
        } while (!validity);
    }

    public double askAllowance() {
        Scanner scanner = new Scanner(System.in);
        double tempAllowance;
        System.out.println("\nPlease input the Supervisor Allowance: ");
        tempAllowance = scanner.nextDouble();
        return tempAllowance;
    }

    public void insertOtHour(NormalStaff tempStaffObj) {
        boolean validity;
        int tempOtHour;
        do {
            validity = false;
            try {
                tempOtHour = askOTHour();
                if (tempOtHour > 0) {
                    validity = true;
                } else {
                    System.out.println("Invalid number. Please input positive number only.");
                }
                tempStaffObj.setOtHour(tempOtHour);
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer only.");
            }
        } while (!validity);
    }

    public int askOTHour() {
        Scanner scanner = new Scanner(System.in);
        int tempOtHour;
        System.out.println("\nPlease input the Normal Staff Overtime Hour: ");
        tempOtHour = scanner.nextInt();
        return tempOtHour;
    }

    public void insertWorkHour(PartTimeStaff tempStaffObj) {
        boolean validity;
        int temoWorkHour;
        do {
            validity = false;
            try {
                temoWorkHour = askWorkHour();
                if (temoWorkHour > 0) {
                    validity = true;
                } else {
                    System.out.println("Invalid number. Please input positive number only.");
                }
                tempStaffObj.setWorkHour(temoWorkHour);
            } catch (Exception e) {
                System.out.println("Invalid input. Please input integer only.");
            }
        } while (!validity);
    }

    public int askWorkHour() {
        Scanner scanner = new Scanner(System.in);
        int tempWorkHour;
        System.out.println("\nPlease input the Part Time Staff Work Hour: ");
        tempWorkHour = scanner.nextInt();
        return tempWorkHour;
    }

    public void clearAllowance(Supervisor tempStaffObj) {
        tempStaffObj.setAllowance(0);
    }

    public void clearOTHour(NormalStaff tempStaffObj) {
        tempStaffObj.setOtHour(0);
    }

    public void clearWorkHour(PartTimeStaff tempStaffObj) {
        tempStaffObj.setWorkHour(0);
    }

    //
    public String findStaff() {
        Scanner scanner = new Scanner(System.in);
        String tempStaffID;
        System.out.println("\nPlease input an existing Staff's ID (SXXXX OR e to exit): ");
        tempStaffID = scanner.nextLine();
        return tempStaffID;
    }

    public int getPositionNum(String tempStaffID) {
        int positionNum = 0;
        if (staffList.contains(new Staff(tempStaffID))) {
            positionNum = staffList.getPosition(new Staff(tempStaffID));
        } else {
            System.out.println("\nStaff " + tempStaffID + " NOT found.");
        }
        return positionNum;
    }

    public Supervisor createTempStaffObj(Supervisor obj) {
        Supervisor tempStaffObj = new Supervisor();

        tempStaffObj.setStaffID(obj.getStaffID());
        tempStaffObj.setStaffName(obj.getStaffName());
        tempStaffObj.setStaffType(obj.getStaffType());
        tempStaffObj.setStaffContact(obj.getStaffContact());
        tempStaffObj.setDateJoined(obj.getDateJoined());
        tempStaffObj.setLoginPass(obj.getLoginPass());
        tempStaffObj.setPosition(obj.getPosition());
        tempStaffObj.setBasicSalary(obj.getBasicSalary());
        tempStaffObj.setAllowance(obj.getAllowance());

        return tempStaffObj;
    }

    public NormalStaff createTempStaffObj(NormalStaff obj) {
        NormalStaff tempStaffObj = new NormalStaff();

        tempStaffObj.setStaffID(obj.getStaffID());
        tempStaffObj.setStaffName(obj.getStaffName());
        tempStaffObj.setStaffType(obj.getStaffType());
        tempStaffObj.setStaffContact(obj.getStaffContact());
        tempStaffObj.setDateJoined(obj.getDateJoined());
        tempStaffObj.setLoginPass(obj.getLoginPass());
        tempStaffObj.setPosition(obj.getPosition());
        tempStaffObj.setBasicSalary(obj.getBasicSalary());
        tempStaffObj.setOtRate(obj.getBasicSalary());
        tempStaffObj.setOtHour(obj.getOtHour());

        return tempStaffObj;
    }

    public PartTimeStaff createTempStaffObj(PartTimeStaff obj) {
        PartTimeStaff tempStaffObj = new PartTimeStaff();

        tempStaffObj.setStaffID(obj.getStaffID());
        tempStaffObj.setStaffName(obj.getStaffName());
        tempStaffObj.setStaffType(obj.getStaffType());
        tempStaffObj.setStaffContact(obj.getStaffContact());
        tempStaffObj.setDateJoined(obj.getDateJoined());
        tempStaffObj.setLoginPass(obj.getLoginPass());
        tempStaffObj.setWages(obj.getWages());
        tempStaffObj.setWorkHour(obj.getWorkHour());

        return tempStaffObj;
    }

}
