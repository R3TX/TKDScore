package model.entity;

public class RoundStatisticsEntity {
    // primaryKey
    private int statisticsId;
    private RoundEntity round;
    private CompetitorEntity competitor;

    private int baseScore;
    private int gamJeomFouls;
    private int headKicks;

    public RoundStatisticsEntity() {}

    public RoundStatisticsEntity(RoundEntity round, CompetitorEntity competitor, int baseScore, int gamJeomFouls, int headKicks) {
        this.round = round;
        this.competitor = competitor;
        this.baseScore = baseScore;
        this.gamJeomFouls = gamJeomFouls;
        this.headKicks = headKicks;
    }

    // Getters
    public RoundEntity getRound() { return round; }
    public CompetitorEntity getCompetitor() { return competitor; }
    public int getBaseScore() { return baseScore; }
    public int getGamJeomFouls() { return gamJeomFouls; }
    public int getHeadKicks() { return headKicks; }

    public int getStatisticsId() {
        return statisticsId;
    }

    // Setters
    public void setRound(RoundEntity round) { this.round = round; }
    public void setCompetitor(CompetitorEntity competitor) { this.competitor = competitor; }
    public void setBaseScore(int baseScore) { this.baseScore = baseScore; }
    public void setGamJeomFouls(int gamJeomFouls) { this.gamJeomFouls = gamJeomFouls; }
    public void setHeadKicks(int headKicks) { this.headKicks = headKicks; }

    public void setStatisticsId(int statisticsId) {
        this.statisticsId = statisticsId;
    }
}