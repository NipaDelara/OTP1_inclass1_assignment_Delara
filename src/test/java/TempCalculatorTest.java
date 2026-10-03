import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TempCalculatorTest {

    private final TempCalculator calculator = new TempCalculator();

    @Test
    void fahrenheitToCelsius() {
        assertEquals(
                25.0,
                calculator.fahrenheitToCelsius(77),
                0.001
        );
    }

    @Test
    void celsiusToFahrenheit() {
        assertEquals(
                77.0,
                calculator.celsiusToFahrenheit(25),
                0.001
        );
    }

    @Test
    void kelvinToCelsius() {
        assertEquals(
                26.85,
                calculator.kelvinToCelsius(300),
                0.001
        );
    }

    @Test
    void highTemperatureIsExtreme() {
        assertTrue(
                calculator.isExtremeTemperature(60)
        );
    }

    @Test
    void lowTemperatureIsExtreme() {
        assertTrue(
                calculator.isExtremeTemperature(-50)
        );
    }

    @Test
    void normalTemperatureIsNotExtreme() {
        assertFalse(
                calculator.isExtremeTemperature(25)
        );
    }
}