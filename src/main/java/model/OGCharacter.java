/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhkc
 */
public class OGCharacter {
    private int characterID;
    private String characterName;

    // Constructor
    public OGCharacter() {}

    public OGCharacter(int characterID, String characterName) {
        this.characterID = characterID;
        this.characterName = characterName;
    }

    // Getters and Setters
    public int getCharacterID() { return characterID; }
    public void setCharacterID(int characterID) { this.characterID = characterID; }

    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
}

