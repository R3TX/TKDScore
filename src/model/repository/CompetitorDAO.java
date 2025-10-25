package model.repository;

import model.entity.CompetitorEntity;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class CompetitorDAO {

    private static final String TABLE_NAME = "COMPETITOR_ENTITY";
    private static final String INSERT_SQL =
            "INSERT INTO " + TABLE_NAME + " (NAME, ASSIGNED_COLOR) VALUES (?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM " + TABLE_NAME + " WHERE COMPETITOR_ID = ?";

    private static final String UPDATE_SQL =
            "UPDATE " + TABLE_NAME +
                    " SET NAME = ?, ASSIGNED_COLOR = ?" +
                    " WHERE COMPETITOR_ID = ? ";

    /**
     * Finds a CompetitorEntity by its unique ID.
     */
    public Optional<CompetitorEntity> findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCompetitor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Saves a new CompetitorEntity and retrieves the generated ID.
     * Assumes ID = 0 for new entities. Update logic should be separate.
     */
    public CompetitorEntity save(CompetitorEntity competitor) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, competitor.getName());
            stmt.setString(2, competitor.getAssignedColor());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    competitor.setuId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competitor;
    }

    public CompetitorEntity update(CompetitorEntity competitor) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            // 1. Asignar los valores del SET (Parámetros 1 a 4)
            stmt.setString(1, competitor.getName());
            stmt.setString(2, competitor.getAssignedColor());

            stmt.setInt(3, competitor.getuId());

            // 3. Ejecutar la actualización
            int rowsAffected = stmt.executeUpdate();
            System.out.println("updated competitor: " + competitor.getuId());

        } catch (SQLException e) {
            System.err.println("error trying to update competitor: " + competitor.getuId());
            e.printStackTrace();
        }
        return competitor;
    }

    private CompetitorEntity mapResultSetToCompetitor(ResultSet rs) throws SQLException {
        CompetitorEntity entity = new CompetitorEntity();
        entity.setuId(rs.getInt("COMPETITOR_ID"));
        entity.setName(rs.getString("NAME"));
        entity.setAssignedColor(rs.getString("ASSIGNED_COLOR"));
        return entity;
    }
}

