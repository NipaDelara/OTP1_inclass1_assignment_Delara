import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL =
            "jdbc:sqlite:temperature_converter.db";

    // Open database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Create database tables
    public static void initializeDatabase() {

        String createTemperatureUnitTable = """
                CREATE TABLE IF NOT EXISTS temperature_unit (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    symbol TEXT NOT NULL
                );
                """;

        String createTempRecordTable = """
                CREATE TABLE IF NOT EXISTS temp_record (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    input_value REAL NOT NULL,
                    input_unit_id INTEGER NOT NULL,
                    output_value REAL NOT NULL,
                    output_unit_id INTEGER NOT NULL,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (input_unit_id)
                        REFERENCES temperature_unit(id),

                    FOREIGN KEY (output_unit_id)
                        REFERENCES temperature_unit(id)
                );
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Enable SQLite foreign keys
            statement.execute("PRAGMA foreign_keys = ON");

            statement.execute(createTemperatureUnitTable);
            statement.execute(createTempRecordTable);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println(
                    "Database initialization failed: "
                            + e.getMessage()
            );
        }
    }
}