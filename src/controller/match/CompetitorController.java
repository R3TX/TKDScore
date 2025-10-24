package controller.match;
import model.entity.CompetitorEntity;
import model.service.CompetitorService;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for handling UI requests related to Competitor management.
 */
public class CompetitorController {

    private final CompetitorService competitorService;

    public CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    /**
     * Handles the request to create a new competitor.
     * @param name The competitor's name.
     * @param color The competitor's assigned color (e.g., "Rojo", "Azul").
     * @return The newly created and saved CompetitorEntity.
     */
    public CompetitorEntity createNewCompetitor(String name, String color) {
        try {
            CompetitorEntity competitor = new CompetitorEntity();
            competitor.setName(name);
            competitor.setAssignedColor(color);
            return competitorService.save(competitor);
        } catch (IllegalArgumentException e) {
            // Log the error and potentially show a user-friendly message in the UI
            System.err.println("Validation Error: " + e.getMessage());
            throw e; // Re-throw for UI to handle
        }
    }

    /**
     * Handles the request to retrieve all registered competitors.
     * @return A list of all CompetitorEntity objects.
     */
    public List<CompetitorEntity> getAllCompetitors() {
        // Assuming CompetitorService has a findAll method implemented
        // return competitorService.findAll();
        return List.of(); // Placeholder
    }

    /**
     * Finds a competitor by ID.
     */
    public Optional<CompetitorEntity> getCompetitorById(int id) {
        return competitorService.findById(id);
    }
}