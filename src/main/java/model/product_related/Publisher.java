package model.product_related;

import java.util.List;
import model.interfaces.IProductClassification;
/**
 *
 * @author anhkc
 */
public class Publisher implements IProductClassification{
    private int publisherID;
    private String publisherName;
    private List<Book> bookList;

    // Constructors
    public Publisher() {}

    public Publisher(int publisherID, String publisherName, List<Book> bookList) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
        this.bookList = bookList;
    }
    
    

    public Publisher(int publisherID, String publisherName) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

    // Getters
    public int getPublisherID() { return publisherID; }
    public String getPublisherName() { return publisherName; }

    public List<Book> getBookList() {
        return bookList;
    }
    

    // Fluent Setters
    public Publisher setPublisherID(int publisherID) {
        this.publisherID = publisherID;
        return this;
    }

    public Publisher setPublisherName(String publisherName) {
        this.publisherName = publisherName;
        return this;
    }

    public Publisher setBookList(List<Book> bookList) {
        this.bookList = bookList;
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

    @Override
    public String getCode() {
        return "publisher";
    }
}
