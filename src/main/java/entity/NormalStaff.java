package entity;

import java.util.Calendar;

/**
 *
 * @author Tan Kai Yuan
 */
public class NormalStaff extends FullTimeStaff {

    private int otHour;
    private double otRate;
    private static int countNormalStaff = 0;

    public NormalStaff(String staffName, String staffType, String staffContact, Calendar dateJoined, String loginPass,
            String position, double basicSalary) {
        super(staffName, staffType, staffContact, dateJoined, loginPass, position, basicSalary);
        this.otRate = basicSalary / 30 / 8 * 1.5;
        countNormalStaff++;
    }

    public NormalStaff(int otHour) {
        this.otHour = otHour;
    }

    public NormalStaff() {
    }

    public NormalStaff(String staffID) {
        super(staffID);
    }

    public int getOtHour() {
        return otHour;
    }

    public void setOtHour(int otHour) {
        this.otHour = otHour;
    }

    public double getOtRate() {
        return otRate;
    }

    public void setOtRate(double basicSalary) {
        this.otRate = basicSalary / 30 / 8 * 1.5;
    }

    public static int getCountNormalStaff() {
        return countNormalStaff;
    }

    public static void setCountNormalStaff(int countNormalStaff) {
        NormalStaff.countNormalStaff = countNormalStaff;
    }

    @Override
    public String toString() {
        return String.format("%-5s   %-20s   %-9s   %-12s   RM%,9.2f   RM%6.2f   %-12s   %-10s", 
                super.getStaffID(), super.getStaffName(), super.getStaffType(), super.getPosition(),
                super.getBasicSalary(), otRate, super.getStaffContact(), super.getDate(super.getDateJoined()));
    }
    
    public double calculateOtPay(){
        return otRate * otHour;
    }
    
    public double calculateMonthlyPay() {
        return super.getBasicSalary() + calculateOtPay();
    }

}
