package model.service;

import model.entity.CompetitorEntity;
import model.entity.RoundEntity;
import model.entity.RoundStatisticsEntity;
import model.repository.CompetitorDAO;
import model.repository.RoundDAO;
import model.repository.RoundStatisticsDAO;

import java.util.Optional;

public class RoundStatisticsService {
    private final RoundDAO roundDAO;
    private final CompetitorDAO competitorDAO;

    private final RoundStatisticsDAO statsDAO;

    public RoundStatisticsService(RoundDAO roundDAO, CompetitorDAO competitorDAO, RoundStatisticsDAO statsDAO) {
        this.roundDAO = roundDAO;
        this.competitorDAO = competitorDAO;
        this.statsDAO = statsDAO;
    }

    /**
     * Retrieves the stats for a competitor in a round, creating a new entity if none exists.
     */
    public RoundStatisticsEntity getOrCreateStats(int statisticsId, int roundId, int competitorId) {

        //Search for statics
        RoundStatisticsEntity roundStatisticsEntity = getStatsById(statisticsId);
        if(null != roundStatisticsEntity){
            return roundStatisticsEntity;
        }

        return createStats(roundId,competitorId);
    }

    public RoundStatisticsEntity getStatsById(int statisticsId){
        // Attempt to find existing stats
        Optional<RoundStatisticsEntity> statsOpt = statsDAO.findById(statisticsId);

        return statsOpt.orElse(null);
    }
    public RoundStatisticsEntity createStats(int roundId, int competitorId){
        RoundEntity round = roundDAO.findById(roundId)
                .orElseThrow(() -> new IllegalArgumentException("Round ID " + roundId + " is invalid."));

        CompetitorEntity competitor = competitorDAO.findById(competitorId)
                .orElseThrow(() -> new IllegalArgumentException("Competitor ID " + competitorId + " is invalid."));

        // If not found, create a new entry in the database (initialize with 0s)
        RoundStatisticsEntity newStats = new RoundStatisticsEntity(round, competitor, 0, 0, 0);
        return statsDAO.save(newStats);
    }

    /**
     * Registers a score event (e.g., a kick) and updates the total score.
     */
    public void registerScore(RoundStatisticsEntity stats, int points, boolean isHeadKick) {

        // Business Logic: Update stats
        stats.setBaseScore(stats.getBaseScore() + points);
        if (isHeadKick) {
            stats.setHeadKicks(stats.getHeadKicks() + 1);
        }

        // Update the database (assuming statsDAO.save handles update/insert)
        statsDAO.updateRoundStatistics(stats);
    }

    /**
     * Registers a score event (e.g., a kick) and updates the total score.
     */
    public void decreaseScore(RoundStatisticsEntity stats) {
        // Business Logic: Update stats
        if (stats.getBaseScore() - 1 >= 0) {
            stats.setBaseScore(stats.getBaseScore() - 1);
            statsDAO.updateRoundStatistics(stats);
        }
    }

    public void setManualScore(RoundStatisticsEntity stats, int points) {
        stats.setBaseScore(points);
        // Update the database
        statsDAO.updateRoundStatistics(stats);
    }

    /**
     * Registers a foul (Gam-Jeom) and updates the total score (deducts 1 point).
     */
    public void registerGamJeom(RoundStatisticsEntity stats) {
        // Business Logic: Gam-Jeom increases foul count
        stats.setGamJeomFouls(stats.getGamJeomFouls() + 1);

        // Update the database
        statsDAO.updateRoundStatistics(stats);
    }

    /**
     * Registers a foul (Gam-Jeom) and updates the total score (deducts 1 point).
     */
    public void decreaseGamJeom(RoundStatisticsEntity stats, RoundStatisticsEntity point) {
        // Business Logic: Gam-Jeom increases foul count

        if (stats.getGamJeomFouls() - 1 >= 0) {
            stats.setGamJeomFouls(stats.getGamJeomFouls() - 1);
            decreaseScore(point);
            statsDAO.updateRoundStatistics(stats);
        }
    }
}