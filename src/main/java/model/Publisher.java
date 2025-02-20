/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhkc
 */
public class Publisher {
    private int publisherID;
    private String publisherName;

    // Constructor
    public Publisher() {}

    public Publisher(int publisherID, String publisherName) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

    // Getters and Setters
    public int getPublisherID() { return publisherID; }
    public void setPublisherID(int publisherID) { this.publisherID = publisherID; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
}

