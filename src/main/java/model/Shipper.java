/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Macbook
 */
public class Shipper extends Account {


    private int totalDeliveries;

    public Shipper() {
    }

    public Shipper(int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountAccountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountAccountIsActive);
    }

    public Shipper(int totalDeliveries, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountAccountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountAccountIsActive);
        this.totalDeliveries = totalDeliveries;
    }

 

    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }

}
