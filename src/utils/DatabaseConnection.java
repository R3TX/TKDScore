package utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {

    private static final String DB_PATH = "./data/marcador_tkd";
    private static final String JDBC_URL = "jdbc:h2:" + DB_PATH;
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Nombre del archivo principal que H2 crea
    private static final String DB_FILE_NAME = "marcador_tkd.mv.db";

    /**
     * Retrieves an active connection to the H2 database.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 Driver not found. Ensure the JAR is in the classpath.");
            throw new SQLException("H2 Driver not available.", e);
        }
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    /**
     * Checks if the main H2 database file already exists on disk.
     *
     * @return true if the file exists, false otherwise.
     */
    public static boolean isDatabaseCreated() {
        // La ruta del archivo es la carpeta donde se encuentra la aplicación + la ruta DB_PATH
        // y el nombre del archivo.
        File dbFile = new File("./data/" + DB_FILE_NAME);
        return dbFile.exists();
    }
    // -----------------------------------

    /**
     * Executes the necessary SQL statements to create the database schema (tables)
     * if they don't already exist (uses IF NOT EXISTS).
     */
    public static void initializeDatabase() {
        if (isDatabaseCreated()) return;
        System.out.println("Initializing H2 Database Schema...");

        // --- SQL Statements to Create Tables (English/Entity-based fields) ---
        String createCompetitorTable =
                "CREATE TABLE IF NOT EXISTS COMPETITOR_ENTITY (" +
                        "   COMPETITOR_ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "   NAME VARCHAR(255) NOT NULL," +
                        "   ASSIGNED_COLOR VARCHAR(10) NOT NULL" +
                        ");";

        String createMatchTable =
                "CREATE TABLE IF NOT EXISTS MATCH_ENTITY (" +
                        "   MATCH_ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "   MATCH_NUMBER INT NOT NULL," +
                        "   RED_COMPETITOR_ID INT NOT NULL," +
                        "   BLUE_COMPETITOR_ID INT NOT NULL," +
                        "   MATCH_WINNER_ID INT," + // Nullable
                        "   RED_WON_ROUNDS INT NOT NULL," +
                        "   BLUE_WON_ROUNDS INT NOT NULL," +
                        "   FOREIGN KEY (RED_COMPETITOR_ID) REFERENCES COMPETITOR_ENTITY(COMPETITOR_ID)," +
                        "   FOREIGN KEY (BLUE_COMPETITOR_ID) REFERENCES COMPETITOR_ENTITY(COMPETITOR_ID)," +
                        "   FOREIGN KEY (MATCH_WINNER_ID) REFERENCES COMPETITOR_ENTITY(COMPETITOR_ID)" +
                        ");";

        String createRoundTable =
                "CREATE TABLE IF NOT EXISTS ROUND_ENTITY (" +
                        "   ROUND_ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "   MATCH_ID INT NOT NULL," +
                        "   ROUND_NUMBER INT NOT NULL," +
                        "   ROUND_WINNER_ID INT," + // Nullable
                        "   FINAL_RED_SCORE INT NOT NULL," +
                        "   FINAL_BLUE_SCORE INT NOT NULL," +
                        "   FOREIGN KEY (MATCH_ID) REFERENCES MATCH_ENTITY(MATCH_ID)," +
                        "   FOREIGN KEY (ROUND_WINNER_ID) REFERENCES COMPETITOR_ENTITY(COMPETITOR_ID)" +
                        ");";

        String createRoundStatsTable =
                "CREATE TABLE IF NOT EXISTS ROUND_STATISTICS_ENTITY (" +
                        "   ROUND_ID INT NOT NULL," +
                        "   COMPETITOR_ID INT NOT NULL," +
                        "   BASE_SCORE INT NOT NULL," +
                        "   GAM_JEOM_FOULS INT NOT NULL," +
                        "   HEAD_KICKS INT NOT NULL," +
                        "   TOTAL_SCORE INT NOT NULL," +
                        "   PRIMARY KEY (ROUND_ID, COMPETITOR_ID)," +
                        "   FOREIGN KEY (ROUND_ID) REFERENCES ROUND_ENTITY(ROUND_ID)," +
                        "   FOREIGN KEY (COMPETITOR_ID) REFERENCES COMPETITOR_ENTITY(COMPETITOR_ID)" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute table creation statements
            stmt.execute(createCompetitorTable);
            stmt.execute(createMatchTable);
            stmt.execute(createRoundTable);
            stmt.execute(createRoundStatsTable);

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error during database initialization:");
            e.printStackTrace();
        }
    }
}