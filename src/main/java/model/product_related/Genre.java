package model.product_related;

import java.util.Objects;

import model.interfaces.ProductClassification;

/**
 *
 * @author anhkc
 */
public class Genre implements ProductClassification{
    private int genreID;
    private String genreName;

    // Constructors
    public Genre() {}

    public Genre(int genreID, String genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
    }

    // Getters
    public int getGenreID() { return genreID; }
    public String getGenreName() { return genreName; }

    // Fluent Setters
    public Genre setGenreID(int genreID) {
        this.genreID = genreID;
        return this;
    }

    public Genre setGenreName(String genreName) {
        this.genreName = genreName;
        return this;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Genre)) return false;
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
}
