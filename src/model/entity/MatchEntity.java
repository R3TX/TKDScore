package model.entity;

public class MatchEntity {
    private int uId;
    private int matchNumber;
    private CompetitorEntity redCompetitor;
    private CompetitorEntity blueCompetitor;
    private CompetitorEntity matchWinner; // Use Integer to allow NULL (if not finished)

    public MatchEntity() {}

    public MatchEntity(int uId, CompetitorEntity redCompetitor, CompetitorEntity blueCompetitor, CompetitorEntity matchWinner, int matchNumber) {
        this.uId = uId;
        this.redCompetitor = redCompetitor;
        this.blueCompetitor = blueCompetitor;
        this.matchWinner = matchWinner;
        this.matchNumber = matchNumber;
    }

    // Getters
    public int getuId() { return uId; }
    public CompetitorEntity getRedCompetitor() { return redCompetitor; }
    public CompetitorEntity getBlueCompetitor() { return blueCompetitor; }
    public CompetitorEntity getMatchWinner() { return matchWinner; }

    public int getMatchNumber() {
        return matchNumber;
    }

    // Setters
    public void setuId(int uId) { this.uId = uId; }
    public void setRedCompetitor(CompetitorEntity redCompetitor) { this.redCompetitor = redCompetitor; }
    public void setBlueCompetitor(CompetitorEntity blueCompetitor) { this.blueCompetitor = blueCompetitor; }
    public void setMatchWinner(CompetitorEntity matchWinner) { this.matchWinner = matchWinner; }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }
}