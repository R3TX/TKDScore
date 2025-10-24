package model.repository;

import model.entity.RoundStatisticsEntity;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RoundStatisticsDAO {

    private final RoundDAO roundDAO;
    private final CompetitorDAO competitorDAO;

    private static final String TABLE_NAME = "ROUND_STATISTICS_ENTITY";
    private static final String INSERT_SQL =
            "INSERT INTO " + TABLE_NAME +
                    " (ROUND_ID, COMPETITOR_ID, BASE_SCORE, GAM_JEOM_FOULS, HEAD_KICKS, TOTAL_SCORE) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE " + TABLE_NAME +
                    " SET BASE_SCORE = ?, GAM_JEOM_FOULS = ?, HEAD_KICKS = ?, TOTAL_SCORE = ?" +
                    " WHERE ROUND_ID = ? AND COMPETITOR_ID = ?";
    private static final String SELECT_BY_COMPOSITE_ID_SQL =
            "SELECT * FROM " + TABLE_NAME + " WHERE ROUND_ID = ? AND COMPETITOR_ID = ?";

    public RoundStatisticsDAO(RoundDAO roundDAO, CompetitorDAO competitorDAO) {
        this.roundDAO = roundDAO;
        this.competitorDAO = competitorDAO;
    }

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

            stmt.setInt(1, stats.getRound().getRoundId());
            stmt.setInt(2, stats.getCompetitor().getuId());
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

    public RoundStatisticsEntity updateRoundStatistics(RoundStatisticsEntity stats // Nuevo parámetro dinámico
    ) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            // 1. Asignar los valores del SET (Parámetros 1 a 4)
            stmt.setInt(1, stats.getBaseScore());
            stmt.setInt(2, stats.getGamJeomFouls());
            stmt.setInt(3, stats.getHeadKicks());
            stmt.setInt(4, stats.getTotalScore());

            // 2. Asignar los valores dinámicos del WHERE (Parámetros 5 y 6)
            stmt.setInt(5, stats.getRound().getRoundId());
            stmt.setInt(6, stats.getCompetitor().getuId());

            // 3. Ejecutar la actualización
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Filas actualizadas: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error al actualizar estadísticas de ronda.");
            e.printStackTrace();
        }
        return stats;
    }

    private RoundStatisticsEntity mapResultSetToStats(ResultSet rs) throws SQLException {
        RoundStatisticsEntity entity = new RoundStatisticsEntity();
        int roundId = rs.getInt("ROUND_ID");
        int competitorId = rs.getInt("COMPETITOR_ID");

        // Asignar RoundEntity
        roundDAO.findById(roundId).ifPresent(entity::setRound);
        // Asignar CompetitorEntity
        competitorDAO.findById(competitorId).ifPresent(entity::setCompetitor);

        entity.setBaseScore(rs.getInt("BASE_SCORE"));
        entity.setGamJeomFouls(rs.getInt("GAM_JEOM_FOULS"));
        entity.setHeadKicks(rs.getInt("HEAD_KICKS"));
        entity.setTotalScore(rs.getInt("TOTAL_SCORE"));
        return entity;
    }
}