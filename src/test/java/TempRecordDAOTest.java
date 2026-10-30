
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordDAOTest {

    private TempRecordDAO recordDAO;
    private TemperatureUnitDAO unitDAO;

    // Step 1: Initialize the database before testing
    @BeforeEach
    void setUp() {
        DBConnection.initializeDatabase();

        unitDAO = new TemperatureUnitDAO();
        recordDAO = new TempRecordDAO();

        // Add Celsius and Fahrenheit only if missing
        List<TemperatureUnit> units = unitDAO.getAllUnits();

        assertNotNull(units, "Cannot read temperature units");

        boolean hasCelsius = units.stream()
                .anyMatch(unit -> "Celsius".equals(unit.getName()));

        boolean hasFahrenheit = units.stream()
                .anyMatch(unit -> "Fahrenheit".equals(unit.getName()));

        if (!hasCelsius) {
            unitDAO.addUnit(new TemperatureUnit("Celsius", "°C"));
        }

        if (!hasFahrenheit) {
            unitDAO.addUnit(new TemperatureUnit("Fahrenheit", "°F"));
        }
    }

    // Step 2: Test saving and reading conversion records
    @Test
    void addAndReadConversionRecord() {

        // Read all temperature units
        List<TemperatureUnit> units = unitDAO.getAllUnits();

        assertNotNull(units, "Temperature units list is null");

        // Find Celsius database ID
        int celsiusId = units.stream()
                .filter(unit -> "Celsius".equals(unit.getName()))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Celsius not found"))
                .getId();

        // Find Fahrenheit database ID
        int fahrenheitId = units.stream()
                .filter(unit -> "Fahrenheit".equals(unit.getName()))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Fahrenheit not found"))
                .getId();

        // Create a Celsius to Fahrenheit conversion
        TempRecord record = new TempRecord(
                25.0,
                celsiusId,
                77.0,
                fahrenheitId
        );

        // Save conversion to MariaDB
        recordDAO.addRecord(record);

        // Read saved conversion history
        List<TempRecord> records = recordDAO.getAllRecords();

        assertNotNull(records, "Conversion history is null");
        assertFalse(records.isEmpty(), "No conversion records found");

        // Check the saved values and temperature units
        boolean found = records.stream().anyMatch(savedRecord ->
                Math.abs(savedRecord.getInputValue() - 25.0) < 0.001
                        &&
                        Math.abs(savedRecord.getOutputValue() - 77.0) < 0.001
                        &&
                        savedRecord.getInputUnitId() == celsiusId
                        &&
                        savedRecord.getOutputUnitId() == fahrenheitId
        );

        // Verify conversion was saved successfully
        assertTrue(found, "Conversion record was not saved correctly");
    }
}
