/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Event {
    private int eventID;
    private String eventName;
    private LocalDate dateCreated;
    private int duration;
    private String banner;
    private String description;
    private int adminID;
    private boolean isActive;
    private LocalDate dateStarted;
    private List<EventProduct> eventProductList;

    public Event() {
    }

    public Event(int eventID, String eventName, LocalDate dateCreated, int duration, String banner, String description, int adminID, boolean isActive, LocalDate dateStarted, List<EventProduct> eventProductList) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.banner = banner;
        this.description = description;
        this.adminID = adminID;
        this.isActive = isActive;
        this.dateStarted = dateStarted;
        this.eventProductList = eventProductList;
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

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
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

    public LocalDate getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
    }

    public List<EventProduct> getEventProductList() {
        return eventProductList;
    }

    public void setEventProductList(List<EventProduct> eventProductList) {
        this.eventProductList = eventProductList;
    }

   
    
   


}
