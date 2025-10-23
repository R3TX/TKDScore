package model.entity;

public class CompetitorEntity {
    private int competitorId;
    private String name;
    private String assignedColor; // E.g., "Red" or "Blue"

    public CompetitorEntity() {}

    public CompetitorEntity(int competitorId, String name, String assignedColor) {
        this.competitorId = competitorId;
        this.name = name;
        this.assignedColor = assignedColor;
    }

    // Getters
    public int getCompetitorId() { return competitorId; }
    public String getName() { return name; }
    public String getAssignedColor() { return assignedColor; }

    // Setters
    public void setCompetitorId(int competitorId) { this.competitorId = competitorId; }
    public void setName(String name) { this.name = name; }
    public void setAssignedColor(String assignedColor) { this.assignedColor = assignedColor; }
}
