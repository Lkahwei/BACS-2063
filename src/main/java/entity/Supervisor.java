package entity;

import java.util.Calendar;

/**
 *
 * @author Tan Kai Yuan
 */
public class Supervisor extends FullTimeStaff {

    private double allowance;
    private static int countSupervisor = 0;

    public Supervisor(String staffName, String staffType, String staffContact, Calendar dateJoined, String loginPass,
            String position, double basicSalary) {
        super(staffName, staffType, staffContact, dateJoined, loginPass, position, basicSalary);
        countSupervisor++;
    }


    public Supervisor() {
    }

    public Supervisor(String staffID) {
        super(staffID);
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public static int getCountSupervisor() {
        return countSupervisor;
    }

    public static void setCountSupervisor(int countSupervisor) {
        Supervisor.countSupervisor = countSupervisor;
    }

    @Override
    public String toString() {
        return String.format("%-5s   %-20s   %-9s   %-12s   RM%,9.2f     %6s   %-12s   %-10s",
                super.getStaffID(), super.getStaffName(), super.getStaffType(), super.getPosition(),
                super.getBasicSalary(), " ", super.getStaffContact(), super.getDate(super.getDateJoined()));
    }
    
    public double calculateMonthlyPay() {
        return super.getBasicSalary() + allowance;
    }

}
