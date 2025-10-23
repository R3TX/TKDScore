package model.repository;

import model.entity.RoundStatisticsEntity;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RoundStatisticsDAO {

    private static final String TABLE_NAME = "ROUND_STATISTICS_ENTITY";
    private static final String INSERT_SQL =
            "INSERT INTO " + TABLE_NAME +
                    " (ROUND_ID, COMPETITOR_ID, BASE_SCORE, GAM_JEOM_FOULS, HEAD_KICKS, TOTAL_SCORE) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_COMPOSITE_ID_SQL =
            "SELECT * FROM " + TABLE_NAME + " WHERE ROUND_ID = ? AND COMPETITOR_ID = ?";

    /**
     * Finds RoundStatisticsEntity using the composite key (Round ID and Competitor ID).
     */
    public Optional<RoundStatisticsEntity> findByCompositeId(int roundId, int competitorId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_COMPOSITE_ID_SQL)) {

            stmt.setInt(1, roundId);
            stmt.setInt(2, competitorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStats(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Saves a new RoundStatisticsEntity. Used for initial creation or subsequent updates.
     * Note: This implementation performs an INSERT. A robust system would use
     * an INSERT OR UPDATE (UPSERT) or check for existence first.
     */
    public RoundStatisticsEntity save(RoundStatisticsEntity stats) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setInt(1, stats.getRoundId());
            stmt.setInt(2, stats.getCompetitorId());
            stmt.setInt(3, stats.getBaseScore());
            stmt.setInt(4, stats.getGamJeomFouls());
            stmt.setInt(5, stats.getHeadKicks());
            stmt.setInt(6, stats.getTotalScore());

            // Execute
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Check if the error is due to a primary key violation (record already exists)
            // If so, you'd execute an UPDATE here instead.
            e.printStackTrace();
        }
        return stats;
    }

    private RoundStatisticsEntity mapResultSetToStats(ResultSet rs) throws SQLException {
        RoundStatisticsEntity entity = new RoundStatisticsEntity();
        entity.setRoundId(rs.getInt("ROUND_ID"));
        entity.setCompetitorId(rs.getInt("COMPETITOR_ID"));
        entity.setBaseScore(rs.getInt("BASE_SCORE"));
        entity.setGamJeomFouls(rs.getInt("GAM_JEOM_FOULS"));
        entity.setHeadKicks(rs.getInt("HEAD_KICKS"));
        entity.setTotalScore(rs.getInt("TOTAL_SCORE"));
        return entity;
    }
}