import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    // Default = local Windows MariaDB
    // Docker can override these values with environment variables
    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mariadb://localhost:3306/temperature_converter"
    );

    private static final String USER = System.getenv().getOrDefault(
            "DB_USER",
            "root"
    );

    private static final String PASSWORD = System.getenv().getOrDefault(
            "DB_PASSWORD",
            "12345"
    );

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {

        String createTemperatureUnitTable = """
                CREATE TABLE IF NOT EXISTS temperature_unit (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL UNIQUE,
                    symbol VARCHAR(20) NOT NULL
                )
                """;

        String createTempRecordTable = """
                CREATE TABLE IF NOT EXISTS temp_record (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    input_value DOUBLE NOT NULL,
                    input_unit_id INT NOT NULL,
                    output_value DOUBLE NOT NULL,
                    output_unit_id INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (input_unit_id)
                        REFERENCES temperature_unit(id),
                    FOREIGN KEY (output_unit_id)
                        REFERENCES temperature_unit(id)
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTemperatureUnitTable);
            statement.execute(createTempRecordTable);

            System.out.println(
                    "MariaDB database initialized successfully."
            );

        } catch (SQLException e) {
            System.err.println(
                    "Database initialization failed: " + e.getMessage()
            );
        }
    }
}