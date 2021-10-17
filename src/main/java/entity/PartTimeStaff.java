package entity;

import java.util.Calendar;

/**
 *
 * @author Tan Kai Yuan
 */
public class PartTimeStaff extends Staff {

    private double wages;
    private int workHour;

    public PartTimeStaff(String staffName, String staffType, String staffContact, Calendar dateJoined, String loginPass,
            double wages) {
        super(staffName, staffType, staffContact, dateJoined, loginPass);
        this.wages = wages;
    }

    public PartTimeStaff() {
    }

    public PartTimeStaff(String staffID) {
        super(staffID);
    }

    public double getWages() {
        return wages;
    }

    public void setWages(double wages) {
        this.wages = wages;
    }

    public int getWorkHour() {
        return workHour;
    }

    public void setWorkHour(int workHour) {
        this.workHour = workHour;
    }

    @Override
    public String toString() {
        return String.format("%-5s   %-20s   %-9s   %-12s   RM%,9.2f     %6s   %-12s   %-10s", 
                super.getStaffID(), super.getStaffName(), super.getStaffType(), " ", wages, " ",
                super.getStaffContact(), super.getDate(super.getDateJoined()));
    }
    
    public double calculateMonthlyPay() {
        return wages * workHour;
    }

}
