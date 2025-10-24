package model.entity;

public class RoundEntity {
    private int roundId;
    private MatchEntity match;
    private int roundNumber;
    private CompetitorEntity roundWinner; // Use Integer to allow NULL
    private int finalRedScore;
    private int finalBlueScore;

    public RoundEntity() {}

    public RoundEntity(int roundId, MatchEntity match, int roundNumber, CompetitorEntity roundWinner, int finalRedScore, int finalBlueScore) {
        this.roundId = roundId;
        this.match = match;
        this.roundNumber = roundNumber;
        this.roundWinner = roundWinner;
        this.finalRedScore = finalRedScore;
        this.finalBlueScore = finalBlueScore;
    }

    // Getters
    public int getRoundId() { return roundId; }
    public MatchEntity getMatch() { return match; }
    public int getRoundNumber() { return roundNumber; }
    public CompetitorEntity getRoundWinner() { return roundWinner; }
    public int getFinalRedScore() { return finalRedScore; }
    public int getFinalBlueScore() { return finalBlueScore; }

    // Setters
    public void setRoundId(int roundId) { this.roundId = roundId; }
    public void setMatch(MatchEntity match) { this.match = match; }
    public void setRoundNumber(int roundNumber) { this.roundNumber = roundNumber; }
    public void setRoundWinner(CompetitorEntity roundWinner) { this.roundWinner = roundWinner; }
    public void setFinalRedScore(int finalRedScore) { this.finalRedScore = finalRedScore; }
    public void setFinalBlueScore(int finalBlueScore) { this.finalBlueScore = finalBlueScore; }
}