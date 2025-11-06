package model;

import java.util.List;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class Series implements IProductClassification {

    private int seriesID;
    private String seriesName;
    private List<Merchandise> merchList;

    // Constructors
    public Series() {
    }

    public Series(int seriesID, String seriesName, List<Merchandise> merchList) {
        this.seriesID = seriesID;
        this.seriesName = seriesName;
        this.merchList = merchList;
    }

    public Series(int seriesID, String seriesName) {
        this(seriesID, seriesName, null);
    }

    // Getters
    public int getSeriesID() {
        return seriesID;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public List<Merchandise> getMerchList() {
        return merchList;
    }

    // Fluent Setters
    public Series setSeriesID(int seriesID) {
        this.seriesID = seriesID;
        return this;
    }

    public Series setSeriesName(String seriesName) {
        this.seriesName = seriesName;
        return this;
    }

    public Series setMerchList(List<Merchandise> merchList) {
        this.merchList = merchList;
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
