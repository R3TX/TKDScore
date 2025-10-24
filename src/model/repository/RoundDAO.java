package model.repository;

import model.entity.RoundEntity;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class RoundDAO {

    private final MatchDAO matchDAO;
    private final CompetitorDAO competitorDAO;

    private static final String TABLE_NAME = "ROUND_ENTITY";
    private static final String INSERT_SQL =
            "INSERT INTO " + TABLE_NAME +
                    " (MATCH_ID, ROUND_NUMBER, ROUND_WINNER_ID, FINAL_RED_SCORE, FINAL_BLUE_SCORE) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE " + TABLE_NAME +
                    " SET MATCH_ID = ?, ROUND_NUMBER = ?, ROUND_WINNER_ID = ?, FINAL_RED_SCORE = ?, FINAL_BLUE_SCORE = ?" +
                    " WHERE ROUND_ID = ? ";

    public RoundDAO(MatchDAO matchDAO, CompetitorDAO competitorDAO) {
        this.matchDAO = matchDAO;
        this.competitorDAO = competitorDAO;
    }

    /**
     * Saves a new RoundEntity. Handles the potentially NULL winner ID.
     */
    public RoundEntity update(RoundEntity round) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            // 1. Asignar los valores del SET (Parámetros 1 a 4)
            stmt.setInt(1, round.getMatch().getuId());
            stmt.setInt(2, round.getRoundNumber());
            if (round.getRoundWinner() != null) {
                stmt.setInt(3, round.getRoundWinner().getuId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, round.getFinalRedScore());
            stmt.setInt(5, round.getFinalBlueScore());
            stmt.setInt(6, round.getRoundId());


            // 3. Ejecutar la actualización
            int rowsAffected = stmt.executeUpdate();
            System.out.println("updated round: "+round.getRoundId());

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return round;
    }

    /**
     * Saves a new RoundEntity. Handles the potentially NULL winner ID.
     */
    public RoundEntity save(RoundEntity round) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, round.getMatch().getuId());
            stmt.setInt(2, round.getRoundNumber());

            // Handle nullable ROUND_WINNER_ID
            if (round.getRoundWinner() != null) {
                stmt.setInt(3, round.getRoundWinner().getuId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setInt(4, round.getFinalRedScore());
            stmt.setInt(5, round.getFinalBlueScore());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    round.setRoundId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return round;
    }

    /**
     * Finds a RoundEntity by its unique ID.
     */
    public Optional<RoundEntity> findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ROUND_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRound(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private RoundEntity mapResultSetToRound(ResultSet rs) throws SQLException {
        RoundEntity entity = new RoundEntity();
        entity.setRoundId(rs.getInt("ROUND_ID"));
        int matchId = rs.getInt("MATCH_ID");
        matchDAO.findById(matchId).ifPresent(entity::setMatch);
        entity.setRoundNumber(rs.getInt("ROUND_NUMBER"));

        // Handle nullable ROUND_WINNER_ID
        int winnerId = rs.getInt("ROUND_WINNER_ID");
        if (!rs.wasNull()) {
            competitorDAO.findById(winnerId).ifPresent(entity::setRoundWinner);
        } else {
            entity.setRoundWinner(null);
        }

        entity.setFinalRedScore(rs.getInt("FINAL_RED_SCORE"));
        entity.setFinalBlueScore(rs.getInt("FINAL_BLUE_SCORE"));
        return entity;
    }
}