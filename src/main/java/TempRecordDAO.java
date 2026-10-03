import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TempRecordDAO {

    // Save conversion to database
    public void addRecord(TempRecord record) {

        String sql = """
                INSERT INTO temp_record
                (input_value, input_unit_id, output_value, output_unit_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDouble(1, record.getInputValue());
            statement.setInt(2, record.getInputUnitId());
            statement.setDouble(3, record.getOutputValue());
            statement.setInt(4, record.getOutputUnitId());

            statement.executeUpdate();

            System.out.println("Conversion saved successfully.");

        } catch (SQLException e) {
            System.err.println(
                    "Error saving conversion: " + e.getMessage()
            );
        }
    }

    // Read all conversion records
    public List<TempRecord> getAllRecords() {

        List<TempRecord> records = new ArrayList<>();

        String sql = """
                SELECT id,
                       input_value,
                       input_unit_id,
                       output_value,
                       output_unit_id,
                       created_at
                FROM temp_record
                ORDER BY id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                TempRecord record = new TempRecord(
                        resultSet.getInt("id"),
                        resultSet.getDouble("input_value"),
                        resultSet.getInt("input_unit_id"),
                        resultSet.getDouble("output_value"),
                        resultSet.getInt("output_unit_id"),
                        resultSet.getString("created_at")
                );

                records.add(record);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error reading conversions: " + e.getMessage()
            );
        }

        return records;
    }
}
