package model.product_related;

import java.util.List;
import java.util.Objects;

import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class Genre implements IProductClassification {

    private int genreID;
    private String genreName;
    private List<Book> bookList;

    // Constructors
    public Genre() {
    }

    public Genre(int genreID, String genreName, List<Book> bookList) {
        this.genreID = genreID;
        this.genreName = genreName;
        this.bookList = bookList;
    }
    
    
    
    public Genre(int genreID, String genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
    }

    // Getters
    public int getGenreID() {
        return genreID;
    }

    public String getGenreName() {
        return genreName;
    }

    public List<Book> getBookList() {
        return bookList;
    }
    

    // Fluent Setters
    public Genre setGenreID(int genreID) {
        this.genreID = genreID;
        return this;
    }

    public Genre setGenreName(String genreName) {
        this.genreName = genreName;
        return this;
    }

    public Genre setBookList(List<Book> bookList) {
        this.bookList = bookList;
        return this;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Genre)) {
            return false;
        }
        Genre other = (Genre) obj;
        return this.genreID == other.genreID; // Compare by genreID
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreID);
    }

    @Override
    public int getId() {
        return getGenreID();
    }

    @Override
    public String getName() {
        return getGenreName();
    }

    @Override
    public String getType() {
        return "book";
    }

    @Override
    public String getCode() {
        return "genre";
    }

    @Override
    public String toString() {
        return "Genre{" + "genreID=" + genreID + ", genreName=" + genreName + ", bookList=" + bookList + '}';
    }
    
    
}
