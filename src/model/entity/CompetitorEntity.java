package model.entity;

public class CompetitorEntity {
    private int uId;
    private String name;
    private String assignedColor; // E.g., "Red" or "Blue"

    public CompetitorEntity() {}

    public CompetitorEntity(int uId, String name, String assignedColor) {
        this.uId = uId;
        this.name = name;
        this.assignedColor = assignedColor;
    }

    // Getters
    public int getuId() { return uId; }
    public String getName() { return name; }
    public String getAssignedColor() { return assignedColor; }

    // Setters
    public void setuId(int uId) { this.uId = uId; }
    public void setName(String name) { this.name = name; }
    public void setAssignedColor(String assignedColor) { this.assignedColor = assignedColor; }
}
