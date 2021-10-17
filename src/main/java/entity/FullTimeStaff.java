package entity;

import java.util.Calendar;

/**
 *
 * @author Tan Kai Yuan
 */
public class FullTimeStaff extends Staff {

    private String position;
    private double basicSalary;

    public FullTimeStaff(String staffName, String staffType, String staffContact, Calendar dateJoined, String loginPass,
            String position, double basicSalary) {
        super(staffName, staffType, staffContact, dateJoined, loginPass);
        this.position = position;
        this.basicSalary = basicSalary;
    }

    public FullTimeStaff() {
    }

    public FullTimeStaff(String staffID) {
        super(staffID);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%-15s %-10.2f", position, basicSalary);
    }

}
