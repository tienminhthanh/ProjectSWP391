package model;

/**
 *
 * @author anhkc
 */
public class Genre {
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
}
