package model.service;
import model.entity.MatchEntity;
import model.repository.CompetitorDAO;
import model.repository.MatchDAO;

import java.util.Optional;

public class MatchService {

    private final MatchDAO matchDAO;
    private final CompetitorDAO competitorDAO;

    public MatchService(MatchDAO matchDAO, CompetitorDAO competitorDAO) {
        this.matchDAO = matchDAO;
        this.competitorDAO = competitorDAO;
    }

    // Constructor de la entidad MatchEntity ajustado para reflejar el orden lógico:
    // public MatchEntity(int matchId, int matchNumber, int redCompetitorId, int blueCompetitorId, Integer matchWinnerId)

    /**
     * Creates a new match between two competitors.
     * Applies business logic to ensure competitors exist.
     * @param matchNumber The sequential number of the match.
     * @param redCompetitorId The ID of the red competitor.
     * @param blueCompetitorId The ID of the blue competitor.
     * @return The newly created MatchEntity.
     * @throws IllegalArgumentException if a competitor ID is invalid.
     */
    public MatchEntity createNewMatch(int matchNumber, int redCompetitorId, int blueCompetitorId) {
        // Business Logic: Ensure both competitors exist by checking the CompetitorDAO
        if (competitorDAO.findById(redCompetitorId).isEmpty() || competitorDAO.findById(blueCompetitorId).isEmpty()) {
            throw new IllegalArgumentException("One or both competitor IDs are invalid.");
        }

        // Logic: Create the new entity with initial state
        MatchEntity newMatch = new MatchEntity(
                0, // DAO will set the matchId
                redCompetitorId,
                blueCompetitorId,
                null, // No winner yet
                matchNumber
        );
        return matchDAO.save(newMatch);
    }

    /**
     * Records the final winner of the match and updates the entity.
     * @param matchId The ID of the match to update.
     * @param winnerId The ID of the winning competitor.
     * @return The updated MatchEntity.
     * @throws RuntimeException if the match is not found.
     * @throws IllegalArgumentException if the winner is not a participant.
     */
    public MatchEntity concludeMatch(int matchId, int winnerId) {
        MatchEntity match = matchDAO.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        // Business Logic: Check if the winner is one of the participants
        if (match.getRedCompetitorId() != winnerId && match.getBlueCompetitorId() != winnerId) {
            throw new IllegalArgumentException("Winner ID is not a participant in this match.");
        }

        match.setMatchWinnerId(winnerId);
        // Assuming matchDAO.save() handles the update if the ID exists
        return matchDAO.save(match);
    }

    /**
     * Retrieves the current match details by ID.
     * @param matchId The ID of the match.
     * @return An Optional containing the MatchEntity.
     */
    public Optional<MatchEntity> findById(int matchId) {
        return matchDAO.findById(matchId);
    }
}