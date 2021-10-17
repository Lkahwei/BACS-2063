package entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Tan Kai Yuan
 */
public class Staff implements Serializable {

    private String staffID;
    private String staffHead = "S";
    private static int staffNo = 1000;
    private String staffName;
    private String staffType;
    private String staffContact;
    private Calendar dateJoined;
    private String loginPass;

    public Staff(String staffName, String staffType, String staffContact, Calendar dateJoined, String loginPass) {
        this.staffID = staffHead + ++staffNo;
        this.staffName = staffName;
        this.staffType = staffType;
        this.staffContact = staffContact;
        this.dateJoined = dateJoined;
        this.loginPass = loginPass;
    }

    public Staff() {
    }

    public Staff(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getStaffContact() {
        return staffContact;
    }

    public void setStaffContact(String staffContact) {
        this.staffContact = staffContact;
    }

    public Calendar getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Calendar dateJoined) {
        this.dateJoined = dateJoined;
    }

    public static void setStaffNo(int staffNo) {
        Staff.staffNo = staffNo;
    }

    public static int getStaffNo() {
        return staffNo;
    }

    public String getDate(Calendar dateJoined) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(dateJoined.getTime());
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    @Override
    public String toString() {
        return String.format("%-5s   %-20s   %-19s   %-12s   %-10s   ", staffID, staffName, staffType, staffContact, getDate(dateJoined));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final Staff other = (Staff) obj;
        if (!Objects.equals(this.staffID, other.staffID)) {
            return false;
        }
        return true;
    }
}
