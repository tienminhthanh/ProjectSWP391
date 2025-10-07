package model.product_related;

import model.interfaces.ProductClassification;

/**
 *
 * @author anhkc
 */
public class OGCharacter implements ProductClassification{
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

    @Override
    public int getId() {
        return getCharacterID();
    }

    @Override
    public String getName() {
        return getCharacterName();
    }

    @Override
    public String getType() {
        return "merch";
    }

    @Override
    public String getCode() {
        return "character";
    }
}
