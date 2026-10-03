import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitTest {

    @Test
    void createTemperatureUnitWithoutId() {

        TemperatureUnit unit =
                new TemperatureUnit("Celsius", "°C");

        assertEquals("Celsius", unit.getName());
        assertEquals("°C", unit.getSymbol());
    }

    @Test
    void createTemperatureUnitWithId() {

        TemperatureUnit unit =
                new TemperatureUnit(
                        1,
                        "Celsius",
                        "°C"
                );

        assertEquals(1, unit.getId());
        assertEquals("Celsius", unit.getName());
        assertEquals("°C", unit.getSymbol());
    }

    @Test
    void temperatureUnitToString() {

        TemperatureUnit unit =
                new TemperatureUnit(
                        1,
                        "Fahrenheit",
                        "°F"
                );

        assertEquals(
                "Fahrenheit (°F)",
                unit.toString()
        );
    }
}