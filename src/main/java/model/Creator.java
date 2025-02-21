/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhkc
 */
public class Creator {
    private int creatorID; // Maps to INT
    private String creatorName; // Maps to NVARCHAR(200)
    private String creatorRole; // Maps to VARCHAR(20)

    // Constructor
    public Creator() {}

    public Creator(int creatorID, String creatorName, String creatorRole) {
        this.creatorID = creatorID;
        this.creatorName = creatorName;
        this.creatorRole = creatorRole;
    }

    // Getters and Setters
    public int getCreatorID() { return creatorID; }
    public void setCreatorID(int creatorID) { this.creatorID = creatorID; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public String getCreatorRole() { return creatorRole; }
    public void setCreatorRole(String creatorRole) { this.creatorRole = creatorRole; }
}

