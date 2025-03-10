package model;

public class Admin extends Account{

    private int totalEvents, totalVoucher;

    public Admin() {
    }

    public Admin(int totalEvents, int totalVoucher) {
        this.totalEvents = totalEvents;
        this.totalVoucher = totalVoucher;
    }

    public Admin(int totalEvents, int totalVoucher, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean isActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive);
        this.totalEvents = totalEvents;
        this.totalVoucher = totalVoucher;
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
