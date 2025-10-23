package model;

import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class Series implements IProductClassification{
    private int seriesID;
    private String seriesName;

    // Constructors
    public Series() {}

    public Series(int seriesID, String seriesName) {
        this.seriesID = seriesID;
        this.seriesName = seriesName;
    }

    // Getters
    public int getSeriesID() { return seriesID; }
    public String getSeriesName() { return seriesName; }

    // Fluent Setters
    public Series setSeriesID(int seriesID) {
        this.seriesID = seriesID;
        return this;
    }

    public Series setSeriesName(String seriesName) {
        this.seriesName = seriesName;
        return this;
    }

    @Override
    public int getId() {
        return getSeriesID();
    }

    @Override
    public String getName() {
        return getSeriesName();
    }

    @Override
    public String getType() {
        return "merch";
    }

    @Override
    public String getCode() {
        return "series";
    }
}
