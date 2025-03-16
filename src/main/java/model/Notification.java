/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class Notification {

    private int notificationID;
    private int senderID;
    private int receiverID;
    private String notificationDetails;
    private Date dateCreated;
    private boolean isDeleted;
    private String notificationTitle;
    private boolean isRead;

    public Notification(int notificationID, int senderID, int receiverID, String notificationDetails, Date dateCreated, boolean isDeleted, String notificationTitle, boolean isRead) {
        this.notificationID = notificationID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.notificationDetails = notificationDetails;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        this.notificationTitle = notificationTitle;
        this.isRead = isRead;
    }

    public Notification(int senderID, int receiverID, String notificationDetails, Date dateCreated, boolean isDeleted, String notificationTitle, boolean isRead) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.notificationDetails = notificationDetails;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        this.notificationTitle = notificationTitle;
        this.isRead = isRead;
    }

    
    public Notification() {
    }

    public Notification(int notificationID, String notificationTitle, String notificationDetails, Date dateCreated) {
        this.notificationID = notificationID;
        this.notificationDetails = notificationDetails;
        this.dateCreated = dateCreated;
        this.notificationTitle = notificationTitle;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(String notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Notification{" + "notificationID=" + notificationID + ", senderID=" + senderID + ", receiverID=" + receiverID + ", notificationDetails=" + notificationDetails + ", dateCreated=" + dateCreated + ", isDeleted=" + isDeleted + ", notificationTitle=" + notificationTitle + ", isRead=" + isRead + '}';
    }
    
}
