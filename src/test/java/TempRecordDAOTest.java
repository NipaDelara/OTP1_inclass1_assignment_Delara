import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordDAOTest {

    private TempRecordDAO recordDAO;

    @BeforeEach
    void setUp() {

        DBConnection.initializeDatabase();

        TemperatureUnitDAO unitDAO =
                new TemperatureUnitDAO();

        unitDAO.addUnit(
                new TemperatureUnit(
                        "Celsius",
                        "°C"
                )
        );

        unitDAO.addUnit(
                new TemperatureUnit(
                        "Fahrenheit",
                        "°F"
                )
        );

        recordDAO = new TempRecordDAO();
    }

    @Test
    void addAndReadConversionRecord() {

        TempRecord record =
                new TempRecord(
                        25.0,
                        1,
                        77.0,
                        2
                );

        recordDAO.addRecord(record);

        List<TempRecord> records =
                recordDAO.getAllRecords();

        assertNotNull(records);
        assertFalse(records.isEmpty());

        boolean found =
                records.stream().anyMatch(
                        savedRecord ->
                                Math.abs(
                                        savedRecord.getInputValue()
                                                - 25.0
                                ) < 0.001
                                        &&
                                        Math.abs(
                                                savedRecord.getOutputValue()
                                                        - 77.0
                                        ) < 0.001
                );

        assertTrue(found);
    }
}