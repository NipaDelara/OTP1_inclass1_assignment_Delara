import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {

    // Add a temperature unit to the database
    public void addUnit(TemperatureUnit unit) {

        String sql = """
                INSERT OR IGNORE INTO temperature_unit (name, symbol)
                VALUES (?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, unit.getName());
            statement.setString(2, unit.getSymbol());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(
                    "Error adding temperature unit: "
                            + e.getMessage()
            );
        }
    }

    // Get all temperature units from the database
    public List<TemperatureUnit> getAllUnits() {

        List<TemperatureUnit> units = new ArrayList<>();

        String sql = """
                SELECT id, name, symbol
                FROM temperature_unit
                ORDER BY id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                TemperatureUnit unit =
                        new TemperatureUnit(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("symbol")
                        );

                units.add(unit);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error reading temperature units: "
                            + e.getMessage()
            );
        }

        return units;
    }
}