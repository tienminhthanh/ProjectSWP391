package model;

/**
 *
 * @author anhkc
 */
public class OGCharacter {
    private int characterID;
    private String characterName;

    // Constructors
    public OGCharacter() {}

    public OGCharacter(int characterID, String characterName) {
        this.characterID = characterID;
        this.characterName = characterName;
    }

    // Getters
    public int getCharacterID() { return characterID; }
    public String getCharacterName() { return characterName; }

    // Fluent Setters
    public OGCharacter setCharacterID(int characterID) {
        this.characterID = characterID;
        return this;
    }

    public OGCharacter setCharacterName(String characterName) {
        this.characterName = characterName;
        return this;
    }
}
