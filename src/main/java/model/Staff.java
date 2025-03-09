/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Staff extends Account {

    private String workShift;
    private int totalOrders;

    public Staff() {
    }

    public Staff(int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean isActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive);
    }

    public Staff(String workShift, int totalOrders, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean isActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive);
        this.workShift = workShift;
        this.totalOrders = totalOrders;
    }

    public String getWorkShift() {
        return workShift;
    }

    public void setWorkShift(String workShift) {
        this.workShift = workShift;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

}
