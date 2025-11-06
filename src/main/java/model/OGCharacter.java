package model;

import java.util.List;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class OGCharacter implements IProductClassification{
    private int characterID;
    private String characterName;
    private List<Merchandise> merchList;

    

    // Constructors
    public OGCharacter() {}

    public OGCharacter(int characterID, String characterName, List<Merchandise> merchList) {
        this.characterID = characterID;
        this.characterName = characterName;
        this.merchList = merchList;
    }

    public OGCharacter(int characterID, String characterName) {
        this(characterID,characterName,null);
    }

   

    // Getters
    public int getCharacterID() { return characterID; }
    public String getCharacterName() { return characterName; }
    public List<Merchandise> getMerchList() {
        return merchList;
    }

   

    // Fluent Setters
    public OGCharacter setCharacterID(int characterID) {
        this.characterID = characterID;
        return this;
    }

    public OGCharacter setCharacterName(String characterName) {
        this.characterName = characterName;
        return this;
    }
     public OGCharacter setMerchList(List<Merchandise> merchList) {
        this.merchList = merchList;
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
