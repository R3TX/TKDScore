package model.entity;

public class MatchEntity {
    private int matchId;
    private int matchNumber;
    private int redCompetitorId;
    private int blueCompetitorId;
    private Integer matchWinnerId; // Use Integer to allow NULL (if not finished)

    public MatchEntity() {}

    public MatchEntity(int matchId, int redCompetitorId, int blueCompetitorId, Integer matchWinnerId, int matchNumber) {
        this.matchId = matchId;
        this.redCompetitorId = redCompetitorId;
        this.blueCompetitorId = blueCompetitorId;
        this.matchWinnerId = matchWinnerId;
        this.matchNumber = matchNumber;
    }

    // Getters
    public int getMatchId() { return matchId; }
    public int getRedCompetitorId() { return redCompetitorId; }
    public int getBlueCompetitorId() { return blueCompetitorId; }
    public Integer getMatchWinnerId() { return matchWinnerId; }

    public int getMatchNumber() {
        return matchNumber;
    }

    // Setters
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public void setRedCompetitorId(int redCompetitorId) { this.redCompetitorId = redCompetitorId; }
    public void setBlueCompetitorId(int blueCompetitorId) { this.blueCompetitorId = blueCompetitorId; }
    public void setMatchWinnerId(Integer matchWinnerId) { this.matchWinnerId = matchWinnerId; }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }
}