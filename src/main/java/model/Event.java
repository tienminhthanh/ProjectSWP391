/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Event {

    private int eventID;
    private String eventName;
    private String eventDateCreated;
    private int eventDuration;
    private String banner;
    private String description;
    private int adminID;
    private boolean eventIsActive;
    private String eventDateStarted;
    private boolean expiry;

    public Event() {
    }

    public Event(String eventName, String eventDateCreated, int eventDuration, String banner, String description, int adminID, boolean eventIsActive, String eventDateStarted, boolean expiry) {
        this.eventName = eventName;
        this.eventDateCreated = eventDateCreated;
        this.eventDuration = eventDuration;
        this.banner = banner;
        this.description = description;
        this.adminID = adminID;
        this.eventIsActive = eventIsActive;
        this.eventDateStarted = eventDateStarted;
        this.expiry = expiry;
    }

    public Event(int eventID, String eventName, String eventDateCreated, int eventDuration, String banner, String description, int adminID, boolean eventIsActive, String eventDateStarted, boolean expiry) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDateCreated = eventDateCreated;
        this.eventDuration = eventDuration;
        this.banner = banner;
        this.description = description;
        this.adminID = adminID;
        this.eventIsActive = eventIsActive;
        this.eventDateStarted = eventDateStarted;
        this.expiry = expiry;
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

    public String getEventDateCreated() {
        return eventDateCreated;
    }

    public void setEventDateCreated(String eventDateCreated) {
        this.eventDateCreated = eventDateCreated;
    }

    public int getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
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

    public boolean isEventIsActive() {
        return eventIsActive;
    }

    public void setEventIsActive(boolean eventIsActive) {
        this.eventIsActive = eventIsActive;
    }

    public String getEventDateStarted() {
        return eventDateStarted;
    }

    public void setEventDateStarted(String eventDateStarted) {
        this.eventDateStarted = eventDateStarted;
    }

    public boolean isExpiry() {
        return expiry;
    }

    public void setExpiry(boolean expiry) {
        this.expiry = expiry;
    }

}
