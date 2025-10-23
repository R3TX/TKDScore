package model.service;
import model.entity.RoundEntity;
import model.repository.MatchDAO;
import model.repository.RoundDAO;

import java.util.Optional;

public class RoundService {

    private final RoundDAO roundDAO;
    private final MatchDAO matchDAO;

    public RoundService(RoundDAO roundDAO, MatchDAO matchDAO) {
        this.roundDAO = roundDAO;
        this.matchDAO = matchDAO;
    }

    /**
     * Starts a new round for a given match.
     * @param matchId The ID of the parent match.
     * @param roundNumber The sequence number of the round (1, 2, or 3).
     * @return The newly created RoundEntity.
     * @throws IllegalStateException if the match does not exist.
     */
    public RoundEntity startNewRound(int matchId, int roundNumber) {
        // Business Logic: Ensure the match exists
        matchDAO.findById(matchId)
                .orElseThrow(() -> new IllegalStateException("Cannot start round. Match not found with ID: " + matchId));

        // Logic: Verify round number validity (1, 2, or 3)
        if (roundNumber < 1 || roundNumber > 3) {
            throw new IllegalArgumentException("Round number must be 1, 2, or 3.");
        }

        // Logic: Create new entity
        RoundEntity newRound = new RoundEntity(
                0, // DAO will set the ID
                matchId,
                roundNumber,
                null,
                0,
                0
        );
        return roundDAO.save(newRound);
    }

    /**
     * Concludes a round, setting the final score and winner based on accumulated stats.
     * @param roundId The ID of the round to conclude.
     * @param redScore Final score for red.
     * @param blueScore Final score for blue.
     * @param roundWinnerId The ID of the round winner.
     * @return The updated RoundEntity.
     */
    public RoundEntity concludeRound(int roundId, int redScore, int blueScore, Integer roundWinnerId) {
        RoundEntity round = roundDAO.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round not found with ID: " + roundId));

        // Business Logic: Update scores and winner
        round.setFinalRedScore(redScore);
        round.setFinalBlueScore(blueScore);
        round.setRoundWinnerId(roundWinnerId);

        // Assuming roundDAO.save() handles the update
        return roundDAO.save(round);
    }

    /**
     * Retrieves a round by its ID.
     */
    public Optional<RoundEntity> findById(int roundId) {
        return roundDAO.findById(roundId);
    }
}