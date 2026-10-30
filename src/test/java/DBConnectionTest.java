
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    // Test MariaDB connection
    @Test
    void databaseConnectionWorks() throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }

    // Test database table initialization
    @Test
    void databaseInitializationWorks() {
        DBConnection.initializeDatabase();

        // Verify the database is accessible
        assertDoesNotThrow(() -> {
            try (Connection connection = DBConnection.getConnection()) {
                assertFalse(connection.isClosed());
            }
        });
    }
}
