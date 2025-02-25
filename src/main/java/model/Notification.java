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
    private boolean ísRead;

    public Notification() {
    }

    public Notification(int notificationID, int senderID, int receiverID, String notificationDetails, Date dateCreated, boolean isDeleted, String notificationTitle, boolean ísRead) {
        this.notificationID = notificationID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.notificationDetails = notificationDetails;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        this.notificationTitle = notificationTitle;
        this.ísRead = ísRead;
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

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public boolean isÍsRead() {
        return ísRead;
    }

    public void setÍsRead(boolean ísRead) {
        this.ísRead = ísRead;
    }
}
