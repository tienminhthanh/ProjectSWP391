/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhkc
 */
public class Series {
    private int seriesID;
    private String seriesName;

    // Constructor
    public Series() {}

    public Series(int seriesID, String seriesName) {
        this.seriesID = seriesID;
        this.seriesName = seriesName;
    }

    // Getters and Setters
    public int getSeriesID() { return seriesID; }
    public void setSeriesID(int seriesID) { this.seriesID = seriesID; }

    public String getSeriesName() { return seriesName; }
    public void setSeriesName(String seriesName) { this.seriesName = seriesName; }
}

