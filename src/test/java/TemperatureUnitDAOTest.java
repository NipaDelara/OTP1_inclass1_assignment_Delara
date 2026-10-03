import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitDAOTest {

    private TemperatureUnitDAO unitDAO;

    @BeforeEach
    void setUp() {

        DBConnection.initializeDatabase();

        unitDAO = new TemperatureUnitDAO();

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

        unitDAO.addUnit(
                new TemperatureUnit(
                        "Kelvin",
                        "K"
                )
        );
    }

    @Test
    void addAndReadTemperatureUnits() {

        List<TemperatureUnit> units =
                unitDAO.getAllUnits();

        assertNotNull(units);

        assertTrue(
                units.stream().anyMatch(
                        unit ->
                                unit.getName()
                                        .equals("Celsius")
                )
        );

        assertTrue(
                units.stream().anyMatch(
                        unit ->
                                unit.getName()
                                        .equals("Fahrenheit")
                )
        );

        assertTrue(
                units.stream().anyMatch(
                        unit ->
                                unit.getName()
                                        .equals("Kelvin")
                )
        );
    }
}