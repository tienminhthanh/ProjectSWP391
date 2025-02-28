package model;

public class Admin {

    private int adminID, totalEvents, totalVoucher;

    public Admin(int adminID, int totalEvents, int totalVoucher) {
        this.adminID = adminID;
        this.totalEvents = totalEvents;
        this.totalVoucher = totalVoucher;
    }

    public Admin() {
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }

    public int getTotalVoucher() {
        return totalVoucher;
    }

    public void setTotalVoucher(int totalVoucher) {
        this.totalVoucher = totalVoucher;
    }

}
