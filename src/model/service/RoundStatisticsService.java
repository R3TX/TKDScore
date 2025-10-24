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
    public RoundStatisticsEntity getOrCreateStats(int roundId, int competitorId) {
        // Attempt to find existing stats
        Optional<RoundStatisticsEntity> statsOpt = statsDAO.findByCompositeId(roundId, competitorId);

        if (statsOpt.isPresent()) {
            return statsOpt.get();
        }
        RoundEntity round = roundDAO.findById(roundId)
                .orElseThrow(() -> new IllegalArgumentException("Round ID " + roundId + " is invalid."));

        CompetitorEntity competitor = competitorDAO.findById(competitorId)
                .orElseThrow(() -> new IllegalArgumentException("Competitor ID " + competitorId + " is invalid."));

        // If not found, create a new entry in the database (initialize with 0s)
        RoundStatisticsEntity newStats = new RoundStatisticsEntity(round, competitor, 0, 0, 0, 0);
        return statsDAO.save(newStats);
    }

    /**
     * Registers a score event (e.g., a kick) and updates the total score.
     */
    public RoundStatisticsEntity registerScore(int roundId, int competitorId, int points, boolean isHeadKick) {
        RoundStatisticsEntity stats = getOrCreateStats(roundId, competitorId);

        // Business Logic: Update stats
        stats.setBaseScore(stats.getBaseScore() + points);
        if (isHeadKick) {
            stats.setHeadKicks(stats.getHeadKicks() + 1);
        }

        // Logic: Recalculate total score (BaseScore - GamJeomFouls)
        stats.setTotalScore(stats.getBaseScore() - stats.getGamJeomFouls());

        // Update the database (assuming statsDAO.save handles update/insert)
        return statsDAO.save(stats);
    }

    /**
     * Registers a foul (Gam-Jeom) and updates the total score (deducts 1 point).
     */
    public RoundStatisticsEntity registerGamJeom(int roundId, int competitorId) {
        RoundStatisticsEntity stats = getOrCreateStats(roundId, competitorId);

        // Business Logic: Gam-Jeom increases foul count
        stats.setGamJeomFouls(stats.getGamJeomFouls() + 1);

        // Logic: Recalculate total score (Gam-Jeom deductions are applied)
        stats.setTotalScore(stats.getBaseScore() - stats.getGamJeomFouls());

        // Update the database
        return statsDAO.save(stats);
    }
}