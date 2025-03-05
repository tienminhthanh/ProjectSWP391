/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhkc
 */
public class Genre {
    private int genreID;
    private String genreName;

    // Constructor
    public Genre() {}

    public Genre(int genreID, String genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
    }

    // Getters and Setters
    public int getGenreID() { return genreID; }
    public void setGenreID(int genreID) { this.genreID = genreID; }

    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }
}

