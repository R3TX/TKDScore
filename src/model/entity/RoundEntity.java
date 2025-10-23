package model.entity;

public class RoundEntity {
    private int roundId;
    private int matchId;
    private int roundNumber;
    private Integer roundWinnerId; // Use Integer to allow NULL
    private int finalRedScore;
    private int finalBlueScore;

    public RoundEntity() {}

    public RoundEntity(int roundId, int matchId, int roundNumber, Integer roundWinnerId, int finalRedScore, int finalBlueScore) {
        this.roundId = roundId;
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.roundWinnerId = roundWinnerId;
        this.finalRedScore = finalRedScore;
        this.finalBlueScore = finalBlueScore;
    }

    // Getters
    public int getRoundId() { return roundId; }
    public int getMatchId() { return matchId; }
    public int getRoundNumber() { return roundNumber; }
    public Integer getRoundWinnerId() { return roundWinnerId; }
    public int getFinalRedScore() { return finalRedScore; }
    public int getFinalBlueScore() { return finalBlueScore; }

    // Setters
    public void setRoundId(int roundId) { this.roundId = roundId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public void setRoundNumber(int roundNumber) { this.roundNumber = roundNumber; }
    public void setRoundWinnerId(Integer roundWinnerId) { this.roundWinnerId = roundWinnerId; }
    public void setFinalRedScore(int finalRedScore) { this.finalRedScore = finalRedScore; }
    public void setFinalBlueScore(int finalBlueScore) { this.finalBlueScore = finalBlueScore; }
}