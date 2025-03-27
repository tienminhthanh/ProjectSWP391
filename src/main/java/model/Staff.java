/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Staff extends Account{


    private int totalOrders;

    public Staff() {
    }

    public Staff(int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountIsActive);
    }

  
    public Staff(int totalOrders, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountAccountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountAccountIsActive);
        this.totalOrders = totalOrders;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

   
    

}
