package model.repository;

import model.entity.MatchEntity;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class MatchDAO {

    private final CompetitorDAO competitorDAO;

    private static final String TABLE_NAME = "MATCH_ENTITY";
    private static final String INSERT_SQL =
            "INSERT INTO " + TABLE_NAME +
                    " (MATCH_NUMBER, RED_COMPETITOR_ID, BLUE_COMPETITOR_ID, MATCH_WINNER_ID) VALUES (?, ?, ?, ?)";

    public MatchDAO(CompetitorDAO competitorDAO) {
        this.competitorDAO = competitorDAO;
    }

    /**
     * Saves a new MatchEntity. Handles the potentially NULL winner ID.
     * Assumes ID = 0 for new entities.
     */
    public MatchEntity save(MatchEntity match) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, match.getMatchNumber());
            stmt.setInt(2, match.getRedCompetitor().getuId());
            stmt.setInt(3, match.getBlueCompetitor().getuId());

            // Handle nullable MATCH_WINNER_ID
            if (match.getMatchWinner() != null) {
                stmt.setInt(4, match.getMatchWinner().getuId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, match.getRedWonRounds());
            stmt.setInt(6, match.getBlueWonRounds());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    match.setuId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }

    /**
     * Finds a MatchEntity by its unique ID.
     */
    public Optional<MatchEntity> findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE MATCH_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMatch(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private MatchEntity mapResultSetToMatch(ResultSet rs) throws SQLException {
        MatchEntity entity = new MatchEntity();
        entity.setuId(rs.getInt("MATCH_ID"));
        entity.setMatchNumber(rs.getInt("MATCH_NUMBER"));

        //get id for competitors
        int redId = rs.getInt("RED_COMPETITOR_ID");
        int blueId = rs.getInt("BLUE_COMPETITOR_ID");

        competitorDAO.findById(redId).ifPresent(entity::setRedCompetitor);
        competitorDAO.findById(blueId).ifPresent(entity::setBlueCompetitor);

        // Handle nullable MATCH_WINNER_ID
        int winnerId = rs.getInt("MATCH_WINNER_ID");
        if (!rs.wasNull()) {
            competitorDAO.findById(winnerId).ifPresent(entity::setMatchWinner);
        } else {
            entity.setMatchWinner(null);
        }
        return entity;
    }
}