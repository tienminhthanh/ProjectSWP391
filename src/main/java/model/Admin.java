package model;

public class Admin extends Account{

    private int totalEvents, totalVouchers;

    public Admin() {
    }

    public Admin(int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountIsActive);
    }

    public Admin(int totalEvents, int totalVouchers, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountIsActive);
        this.totalEvents = totalEvents;
        this.totalVouchers = totalVouchers;
    }


  
    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }

    public int getTotalVouchers() {
        return totalVouchers;
    }

    public void setTotalVouchers(int totalVouchers) {
        this.totalVouchers = totalVouchers;
    }

    

}
