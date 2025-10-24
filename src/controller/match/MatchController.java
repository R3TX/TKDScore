package controller.match;

import model.entity.CompetitorEntity;
import model.entity.MatchEntity;
import model.entity.RoundEntity;
import model.entity.RoundStatisticsEntity;
import model.service.CompetitorService;
import model.service.MatchService;
import model.service.RoundService;
import model.service.RoundStatisticsService;

import java.util.Optional;

public class MatchController {
    private final MatchService matchService;
    private final RoundService roundService;
    private final RoundStatisticsService statsService;
    private final CompetitorService competitorService;

    private MatchEntity currentMatch;

    public MatchController(MatchService matchService, RoundService roundService, RoundStatisticsService statsService, CompetitorService competitorService) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.statsService = statsService;
        this.competitorService = competitorService;
    }

    // --- Match Flow Management ---

    /**
     * Starts a new match and the first round.
     * @param matchNumber The number of the new match.
     * @param redCompetitorId The ID of the red competitor.
     * @param blueCompetitorId The ID of the blue competitor.
     * @return The newly created MatchEntity.
     */
    public MatchEntity startNewMatch(int matchNumber, int redCompetitorId, int blueCompetitorId) {
        // 1. Create the Match
        MatchEntity match = matchService.createNewMatch(matchNumber, redCompetitorId, blueCompetitorId);
        // 2. Set the newly created match as the current active match
        this.currentMatch = match;
        // 3. Automatically start the first Round
        // This is a common business logic requirement: Match start = Round 1 start.
        startNewRound(match.getuId(), 1);

        return match;
    }

    /**
     * Provides access to the MatchEntity that is currently active.
     * @return The currently active MatchEntity.
     * @throws IllegalStateException if no match is currently active.
     */
    public MatchEntity getCurrentMatch() {
        if (currentMatch == null) {
            // Es buena práctica lanzar una excepción si se intenta acceder a un estado no inicializado.
            throw new IllegalStateException("No match is currently active. Call startNewMatch first.");
        }
        return currentMatch;
    }

    /**
     * Starts a new sequential round (e.g., Round 2 after Round 1 finishes).
     * @param matchId The ID of the current match.
     * @param roundNumber The number of the round (2 or 3).
     * @return The newly created RoundEntity.
     */
    public RoundEntity startNewRound(int matchId, int roundNumber) {
        return roundService.startNewRound(matchId, roundNumber);
    }

    /**
     * Finalizes the current match, setting the official winner.
     * @param matchId The ID of the match.
     * @param winnerId The ID of the winning competitor.
     * @return The updated MatchEntity.
     */
    public MatchEntity finishMatch(int matchId, int winnerId) {
        MatchEntity finishedMatch = matchService.concludeMatch(matchId, winnerId);
        this.currentMatch = null; // clean reference
        return finishedMatch;
    }

    // --- Scoring and Statistics Management ---

    /**
     * Registers a score event for a competitor in the current round.
     * @param roundId The current active round ID.
     * @param competitorId The ID of the scoring competitor.
     * @param points The base points scored (1, 2, or 3).
     * @param isHeadKick True if the score was a head kick.
     * @return The updated RoundStatisticsEntity.
     */
    public RoundStatisticsEntity registerScore(int roundId, int competitorId, int points, boolean isHeadKick) {
        return statsService.registerScore(roundId, competitorId, points, isHeadKick);
    }

    /**
     * Registers a Gam-Jeom foul for a competitor in the current round.
     * @param roundId The current active round ID.
     * @param competitorId The ID of the penalized competitor.
     * @return The updated RoundStatisticsEntity.
     */
    public RoundStatisticsEntity registerGamJeom(int roundId, int competitorId) {
        return statsService.registerGamJeom(roundId, competitorId);
    }

    /**
     * Finalizes the current round based on the scores accumulated in RoundStatistics.
     * NOTE: Actual score calculation (who wins the round) should be done here
     * or delegated back to a service layer method that analyzes the current stats.
     * @param roundId The ID of the round to finish.
     */
    public RoundEntity finishRound(int roundId) {
        MatchEntity match = getCurrentMatch();
        // Business Logic Example (Simplified):
        // 1. Fetch R.S. for Red and Blue.
        // 2. Compare total scores.
        // 3. Determine winnerId and final scores.

        int redId = match.getRedCompetitor().getuId();
        int blueId = match.getBlueCompetitor().getuId();

        RoundStatisticsEntity redStats = statsService.getOrCreateStats(roundId, redId);
        RoundStatisticsEntity blueStats = statsService.getOrCreateStats(roundId, blueId);

        int redScore = redStats.getTotalScore();
        int blueScore = blueStats.getTotalScore();

        Integer winnerId = null;
        if (redScore > blueScore) {
            winnerId = redId;
        } else if (blueScore > redScore) {
            winnerId = blueId;
        }

        return roundService.concludeRound(roundId, redScore, blueScore, winnerId);
    }

    /**
     * Retrieves the competitor's name based on their ID, primarily for display in the View.
     * This decouples the View (JFrameColumns) from the Data Access layer.
     * @param competitorId The ID of the competitor.
     * @return The competitor's name (String).
     * @throws RuntimeException if the competitor is not found.
     */
    public String getCompetitorName(int competitorId) {
        Optional<CompetitorEntity> competitor = competitorService.findById(competitorId);

        if (competitor.isEmpty()) {
            //in case there is no competitor
            throw new RuntimeException("Competitor with ID " + competitorId + " not found.");
        }

        return competitor.get().getName();
    }
}