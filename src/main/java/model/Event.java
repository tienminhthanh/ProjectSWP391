/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author ADMIN
 */
public class Event {

    private int eventID;
    private String eventName;
    private String dateCreated;
    private int duration;
    private String banner;
    private String description;
    private int adminID;
    private boolean isActive;
    private String dateStarted;
    private boolean expiry;

    public Event(int eventID, String eventName, String dateCreated, int duration, String banner, String description, int adminID, boolean isActive, String dateStarted, boolean expiry) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.banner = banner;
        this.description = description;
        this.adminID = adminID;
        this.isActive = isActive;
        this.dateStarted = dateStarted;
        this.expiry = expiry;
    }

    public Event(String eventName, String dateCreated, int duration, String banner, String description, int adminID, boolean isActive, String dateStarted, boolean expiry) {
        this.eventName = eventName;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.banner = banner;
        this.description = description;
        this.adminID = adminID;
        this.isActive = isActive;
        this.dateStarted = dateStarted;
        this.expiry = expiry;
    }

    public Event() {
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public boolean isExpiry() {
        return expiry;
    }

    public void setExpiry(boolean expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "Event{" + "eventID=" + eventID + ", eventName=" + eventName + ", dateCreated=" + dateCreated + ", duration=" + duration + ", banner=" + banner + ", description=" + description + ", adminID=" + adminID + ", isActive=" + isActive + ", dateStarted=" + dateStarted + ", expiry=" + expiry + '}';
    }

}
