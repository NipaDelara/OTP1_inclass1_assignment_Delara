import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TemperatureConverterTest {

    private final TemperatureConverter converter =
            new TemperatureConverter();

    // Test Celsius to Fahrenheit
    @Test
    void testCelsiusToFahrenheit() {
        assertEquals(77.0,
                converter.celsiusToFahrenheit(25), 0.001);
    }

    // Test Fahrenheit to Celsius
    @Test
    void testFahrenheitToCelsius() {
        assertEquals(25.0,
                converter.fahrenheitToCelsius(77), 0.001);
    }

    // Test Kelvin to Celsius
    @Test
    void testKelvinToCelsius() {
        assertEquals(26.85,
                converter.kelvinToCelsius(300), 0.001);
    }

    // Test extreme temperature
    @Test
    void testExtremeTemperature() {
        assertTrue(converter.isExtremeTemperature(60));
    }

    // Test normal temperature
    @Test
    void testNormalTemperature() {
        assertFalse(converter.isExtremeTemperature(25));
    }

    // Test freezing point
    @Test
    void testFreezingPoint() {
        assertEquals(32.0,
                converter.celsiusToFahrenheit(0), 0.001);
    }

    // Test negative temperature
    @Test
    void testNegativeTemperature() {
        assertEquals(-40.0,
                converter.celsiusToFahrenheit(-40), 0.001);
    }

    // Test boiling point
    @Test
    void testBoilingPoint() {
        assertEquals(100.0,
                converter.fahrenheitToCelsius(212), 0.001);
    }
}
