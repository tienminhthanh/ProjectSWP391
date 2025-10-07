package model.product_related;

import model.interfaces.ProductClassification;
/**
 *
 * @author anhkc
 */
public class Publisher implements ProductClassification{
    private int publisherID;
    private String publisherName;

    // Constructors
    public Publisher() {}

    public Publisher(int publisherID, String publisherName) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

    // Getters
    public int getPublisherID() { return publisherID; }
    public String getPublisherName() { return publisherName; }

    // Fluent Setters
    public Publisher setPublisherID(int publisherID) {
        this.publisherID = publisherID;
        return this;
    }

    public Publisher setPublisherName(String publisherName) {
        this.publisherName = publisherName;
        return this;
    }

    @Override
    public int getId() {
        return getPublisherID();
    }

    @Override
    public String getName() {
        return getPublisherName();
    }

    @Override
    public String getType() {
        return "book";
    }
}
