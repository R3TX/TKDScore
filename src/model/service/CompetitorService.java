package model.service;
import model.entity.CompetitorEntity;
import model.repository.CompetitorDAO;

import java.util.List;
import java.util.Optional;

public class CompetitorService {

    private final CompetitorDAO competitorDAO;

    // Constructor for dependency injection
    public CompetitorService(CompetitorDAO competitorDAO) {
        this.competitorDAO = competitorDAO;
    }

    /**
     * Finds a Competitor by their unique ID.
     * @param id The Competitor's ID.
     * @return An Optional containing the CompetitorEntity, or empty if not found.
     */
    public Optional<CompetitorEntity> findById(int id) {
        return competitorDAO.findById(id);
    }

    /**
     * Retrieves all competitors.
     * @return A list of all CompetitorEntity objects.
     */
    // Assuming a findAll method exists in CompetitorDAO
    // public List<CompetitorEntity> findAll() {
    //     return competitorDAO.findAll();
    // }

    /**
     * Saves a new Competitor or updates an existing one.
     * Includes basic business logic validation (e.g., name must not be empty).
     * @param competitor The CompetitorEntity to save.
     * @return The saved CompetitorEntity.
     * @throws IllegalArgumentException if validation fails.
     */
    public CompetitorEntity save(CompetitorEntity competitor) {
        if (competitor.getName() == null || competitor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Competitor name cannot be empty.");
        }
        if (competitor.getAssignedColor() == null || competitor.getAssignedColor().trim().isEmpty()) {
            throw new IllegalArgumentException("Competitor assigned color cannot be empty.");
        }
        return competitorDAO.save(competitor);
    }
}