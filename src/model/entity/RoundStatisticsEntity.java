package model.entity;

public class RoundStatisticsEntity {
    // Composite Primary Key (roundId, competitorId)
    private int roundId;
    private int competitorId;

    private int baseScore;
    private int gamJeomFouls;
    private int headKicks;
    private int totalScore;

    public RoundStatisticsEntity() {}

    public RoundStatisticsEntity(int roundId, int competitorId, int baseScore, int gamJeomFouls, int headKicks, int totalScore) {
        this.roundId = roundId;
        this.competitorId = competitorId;
        this.baseScore = baseScore;
        this.gamJeomFouls = gamJeomFouls;
        this.headKicks = headKicks;
        this.totalScore = totalScore;
    }

    // Getters
    public int getRoundId() { return roundId; }
    public int getCompetitorId() { return competitorId; }
    public int getBaseScore() { return baseScore; }
    public int getGamJeomFouls() { return gamJeomFouls; }
    public int getHeadKicks() { return headKicks; }
    public int getTotalScore() { return totalScore; }

    // Setters
    public void setRoundId(int roundId) { this.roundId = roundId; }
    public void setCompetitorId(int competitorId) { this.competitorId = competitorId; }
    public void setBaseScore(int baseScore) { this.baseScore = baseScore; }
    public void setGamJeomFouls(int gamJeomFouls) { this.gamJeomFouls = gamJeomFouls; }
    public void setHeadKicks(int headKicks) { this.headKicks = headKicks; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
}