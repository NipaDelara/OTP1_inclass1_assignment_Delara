public class TemperatureConverter {

    private final TempCalculator calculator = new TempCalculator();

    // Keep old method for existing tests
    public double fahrenheitToCelsius(double fahrenheit) {
        return calculator.fahrenheitToCelsius(fahrenheit);
    }

    // Keep old method for existing tests
    public double celsiusToFahrenheit(double celsius) {
        return calculator.celsiusToFahrenheit(celsius);
    }

    // Keep old method for existing tests
    public boolean isExtremeTemperature(double celsius) {
        return calculator.isExtremeTemperature(celsius);
    }

    // Keep old method for existing tests
    public double kelvinToCelsius(double kelvin) {
        return calculator.kelvinToCelsius(kelvin);
    }

    public static void main(String[] args) {

        // Create database and tables
        DBConnection.initializeDatabase();

        // Create Temperature Unit DAO
        TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();

        // Add temperature units
        unitDAO.addUnit(
                new TemperatureUnit("Celsius", "°C")
        );

        unitDAO.addUnit(
                new TemperatureUnit("Fahrenheit", "°F")
        );

        unitDAO.addUnit(
                new TemperatureUnit("Kelvin", "K")
        );

        // Display units
        System.out.println(
                "\nTemperature units in database:"
        );

        for (TemperatureUnit unit : unitDAO.getAllUnits()) {

            System.out.println(
                    unit.getId()
                            + " - "
                            + unit.getName()
                            + " "
                            + unit.getSymbol()
            );
        }

        // Calculator
        TempCalculator calculator =
                new TempCalculator();

        double celsius = 25;

        System.out.println(
                "\nTemperature Converter"
        );

        System.out.println(
                "---------------------"
        );

        System.out.println(
                "25 C = "
                        + calculator.celsiusToFahrenheit(celsius)
                        + " F"
        );

        System.out.println(
                "77 F = "
                        + calculator.fahrenheitToCelsius(77)
                        + " C"
        );

        System.out.println(
                "300 K = "
                        + calculator.kelvinToCelsius(300)
                        + " C"
        );

        System.out.println(
                "Is 60 C extreme? "
                        + calculator.isExtremeTemperature(60)
        );

        // Create Record DAO
        TempRecordDAO recordDAO =
                new TempRecordDAO();

        // Calculate conversion
        double fahrenheit =
                calculator.celsiusToFahrenheit(celsius);

        // Create conversion record
        TempRecord record =
                new TempRecord(
                        celsius,
                        1,
                        fahrenheit,
                        2
                );

        // Save record
        recordDAO.addRecord(record);

        // Display saved records
        System.out.println(
                "\nSaved conversion records:"
        );

        for (TempRecord savedRecord :
                recordDAO.getAllRecords()) {

            System.out.println(
                    "ID: "
                            + savedRecord.getId()
                            + " | Input: "
                            + savedRecord.getInputValue()
                            + " | Input Unit ID: "
                            + savedRecord.getInputUnitId()
                            + " | Output: "
                            + savedRecord.getOutputValue()
                            + " | Output Unit ID: "
                            + savedRecord.getOutputUnitId()
                            + " | Date: "
                            + savedRecord.getCreatedAt()
            );
        }
    }
}