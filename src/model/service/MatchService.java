package model.service;
import model.entity.CompetitorEntity;
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
        CompetitorEntity redCompetitor = competitorDAO.findById(redCompetitorId)
                .orElseThrow(() -> new IllegalArgumentException("Red Competitor ID " + redCompetitorId + " is invalid."));

        CompetitorEntity blueCompetitor = competitorDAO.findById(blueCompetitorId)
                .orElseThrow(() -> new IllegalArgumentException("Blue Competitor ID " + blueCompetitorId + " is invalid."));

        // Logic: Create the new entity with initial state
        MatchEntity newMatch = new MatchEntity(
                0, // DAO will set the matchId
                redCompetitor,
                blueCompetitor,
                null, // No winner yet
                matchNumber
        );
        return matchDAO.save(newMatch);
    }

    /**
     * Updates a match between two competitors.
     * Applies business logic to ensure competitors exist.
     * @param match the match to update.
     * @return The newly created MatchEntity.
     * @throws IllegalArgumentException if a competitor ID is invalid.
     */
    public MatchEntity updateMatch(MatchEntity match) {
        // Business Logic: Ensure both competitors exist by checking the CompetitorDAO
           if(match.getRedCompetitor()== null) {

               throw new IllegalArgumentException("Red Competitor ID is invalid.");
           }
        if(match.getBlueCompetitor()== null) {

            throw new IllegalArgumentException("Blue Competitor ID is invalid.");
        }

        return matchDAO.save(match);
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
        CompetitorEntity winner = competitorDAO.findById(winnerId)
                .orElseThrow(() -> new IllegalArgumentException("Winner ID " + winnerId + " is invalid."));

        boolean isParticipant = match.getRedCompetitor().getuId() == winnerId ||
                match.getBlueCompetitor().getuId() == winnerId;

        if (!isParticipant) {
            throw new IllegalArgumentException("Winner ID " + winnerId + " is not a participant in this match.");
        }
        match.setMatchWinner(winner);

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