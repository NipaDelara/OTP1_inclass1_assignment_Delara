import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {

    // Add a temperature unit to MariaDB
    public void addUnit(TemperatureUnit unit) {

        String sql = """
                INSERT IGNORE INTO temperature_unit (name, symbol)
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

    // Get all temperature units from MariaDB
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

    // Get localized temperature units from MariaDB.
    public List<TemperatureUnit> getLocalizedUnits(String language) {

        List<TemperatureUnit> units = new ArrayList<>();

        String sql = """
            SELECT u.id,
                   COALESCE(t.name, en.name, u.name) AS name,
                   u.symbol
            FROM temperature_unit u
            LEFT JOIN unit_translation t
                ON u.id = t.unit_id AND t.lang = ?
            LEFT JOIN unit_translation en
                ON u.id = en.unit_id AND en.lang = 'en'
            ORDER BY u.id
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, language);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    TemperatureUnit unit = new TemperatureUnit(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("symbol")
                    );

                    units.add(unit);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error reading localized units: " + e.getMessage()
            );
        }

        return units;
    }

}