public class TempCalculator {

        // Convert Fahrenheit to Celsius
        public double fahrenheitToCelsius(double fahrenheit) {
            return (fahrenheit - 32) * 5.0 / 9.0;
        }

        // Convert Celsius to Fahrenheit
        public double celsiusToFahrenheit(double celsius) {
            return (celsius * 9.0 / 5.0) + 32;
        }

        // Check if Celsius temperature is extreme
        public boolean isExtremeTemperature(double celsius) {
            return celsius < -40 || celsius > 50;
        }

        // Convert Kelvin to Celsius
        public double kelvinToCelsius(double kelvin) {
            return kelvin - 273.15;
        }

        // Convert Celsius to Kelvin
        public double celsiusToKelvin(double celsius) {
            return celsius + 273.15;
        }

        // Convert temperature between supported units
        public double convert(double value, String from, String to) {

            // Reject invalid numbers
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException(
                        "Invalid temperature value");
            }

            // Convert input temperature to Celsius
            double celsius;

            switch (from) {
                case "Celsius":
                    celsius = value;
                    break;
                case "Fahrenheit":
                    celsius = fahrenheitToCelsius(value);
                    break;
                case "Kelvin":
                    celsius = kelvinToCelsius(value);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unsupported input unit: " + from);
            }

            // Convert Celsius to the requested unit
            double result;

            switch (to) {
                case "Celsius":
                    result = celsius;
                    break;
                case "Fahrenheit":
                    result = celsiusToFahrenheit(celsius);
                    break;
                case "Kelvin":
                    result = celsiusToKelvin(celsius);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unsupported output unit: " + to);
            }

            // Reject invalid results
            if (!Double.isFinite(result)) {
                throw new IllegalArgumentException(
                        "Temperature is outside the supported range");
            }

            return result;
        }
}

