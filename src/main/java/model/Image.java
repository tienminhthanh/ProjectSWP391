/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhkc
 */
public class Image {
    private int imageID; // Maps to INT
    private String imageURL; // Maps to NVARCHAR(1024)

    // Empty
    public Image() {}
    
    /**
     * Full
     * 
     * @param imageID
     * @param imageURL 
     */
    public Image(int imageID, String imageURL) {
        this.imageID = imageID;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public int getImageID() { return imageID; }
    public void setImageID(int imageID) { this.imageID = imageID; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
