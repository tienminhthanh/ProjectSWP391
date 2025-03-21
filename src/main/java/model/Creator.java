package model;

import java.util.Objects;

/**
 *
 * @author anhkc
 */
public class Creator {
    private int creatorID; // Maps to INT
    private String creatorName; // Maps to NVARCHAR(200)
    private String creatorRole; // Maps to VARCHAR(20)
    private String generalCategory;

    // Constructor
    public Creator() {}

    public Creator(int creatorID, String creatorName, String creatorRole) {
        this.creatorID = creatorID;
        this.creatorName = creatorName;
        this.creatorRole = creatorRole;
    }

    public Creator(int creatorID, String creatorName, String creatorRole, String generalCategory) {
        this.creatorID = creatorID;
        this.creatorName = creatorName;
        this.creatorRole = creatorRole;
        this.generalCategory = generalCategory;
    }

    // Getters
    public int getCreatorID() { return creatorID; }
    public String getCreatorName() { return creatorName; }
    public String getCreatorRole() { return creatorRole; }
    public String getGeneralCategory() { return generalCategory; }

    // Fluent Setters
    public Creator setCreatorID(int creatorID) { 
        this.creatorID = creatorID; 
        return this; 
    }

    public Creator setCreatorName(String creatorName) { 
        this.creatorName = creatorName; 
        return this; 
    }

    public Creator setCreatorRole(String creatorRole) { 
        this.creatorRole = creatorRole; 
        return this; 
    }

    public Creator setGeneralCategory(String generalCategory) { 
        this.generalCategory = generalCategory; 
        return this; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Creator)) return false;
        Creator other = (Creator) obj;
        return this.creatorID == other.creatorID; // Compare by creatorID
    }

    @Override
    public int hashCode() {
        return Objects.hash(creatorID);
    }
}
