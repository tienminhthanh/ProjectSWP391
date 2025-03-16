package model;

/**
 *
 * @author anhkc
 */
public class Series {
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
}
