import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TemperatureConverterGUI extends Application {

    private final TempCalculator calculator = new TempCalculator();
    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
    private final TempRecordDAO recordDAO = new TempRecordDAO();

    private TextField temperatureField;
    private ComboBox<TemperatureUnit> fromUnitBox;
    private ComboBox<TemperatureUnit> toUnitBox;
    private Label resultLabel;
    private TextArea historyArea;

    @Override
    public void start(Stage stage) {

        // Create database and tables
        DBConnection.initializeDatabase();

        // Add default temperature units
        initializeUnits();

        // Application title
        Label titleLabel = new Label("Temperature Converter");
        titleLabel.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        // Temperature input
        Label inputLabel = new Label("Temperature:");

        temperatureField = new TextField();
        temperatureField.setPromptText("Enter temperature");

        // From unit
        Label fromLabel = new Label("From:");
        fromUnitBox = new ComboBox<>();

        // To unit
        Label toLabel = new Label("To:");
        toUnitBox = new ComboBox<>();

        // Load units from database
        loadUnits();

        // Convert button
        Button convertButton = new Button("Convert & Save");
        convertButton.setOnAction(
                event -> convertTemperature()
        );

        // Result
        resultLabel = new Label("Result:");
        resultLabel.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        // Conversion history
        Label historyLabel = new Label("Conversion History");
        historyLabel.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;"
        );

        historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setPrefHeight(180);

        // Read saved records from database
        loadHistory();

        // Layout
        VBox root = new VBox(
                10,
                titleLabel,
                inputLabel,
                temperatureField,
                fromLabel,
                fromUnitBox,
                toLabel,
                toUnitBox,
                convertButton,
                resultLabel,
                historyLabel,
                historyArea
        );

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Window
        Scene scene = new Scene(root, 450, 600);

        stage.setTitle("Temperature Converter");
        stage.setScene(scene);
        stage.show();
    }

    // Add Celsius, Fahrenheit and Kelvin to database
    private void initializeUnits() {

        unitDAO.addUnit(
                new TemperatureUnit("Celsius", "°C")
        );

        unitDAO.addUnit(
                new TemperatureUnit("Fahrenheit", "°F")
        );

        unitDAO.addUnit(
                new TemperatureUnit("Kelvin", "K")
        );
    }

    // Load temperature units from database
    private void loadUnits() {

        List<TemperatureUnit> units =
                unitDAO.getAllUnits();

        fromUnitBox.getItems().addAll(units);
        toUnitBox.getItems().addAll(units);

        if (!units.isEmpty()) {

            // Default: Celsius
            fromUnitBox.getSelectionModel().selectFirst();

            // Default: Fahrenheit
            if (units.size() > 1) {
                toUnitBox.getSelectionModel().select(1);
            }
        }
    }

    // Convert temperature and save result
    private void convertTemperature() {

        try {

            double input = Double.parseDouble(
                    temperatureField.getText()
            );

            TemperatureUnit from =
                    fromUnitBox.getValue();

            TemperatureUnit to =
                    toUnitBox.getValue();

            if (from == null || to == null) {
                showError(
                        "Please select temperature units."
                );
                return;
            }

            double result = convert(
                    input,
                    from.getName(),
                    to.getName()
            );

            // Display result
            resultLabel.setText(
                    String.format(
                            "Result: %.2f %s",
                            result,
                            to.getSymbol()
                    )
            );

            // Create database record
            TempRecord record = new TempRecord(
                    input,
                    from.getId(),
                    result,
                    to.getId()
            );

            // Save conversion
            recordDAO.addRecord(record);

            // Refresh history
            loadHistory();

        } catch (NumberFormatException e) {

            showError(
                    "Please enter a valid temperature."
            );
        }
    }

    // Handle all temperature conversions
    private double convert(
            double value,
            String from,
            String to) {

        // Same unit
        if (from.equals(to)) {
            return value;
        }

        // Celsius -> Fahrenheit
        if (from.equals("Celsius")
                && to.equals("Fahrenheit")) {

            return calculator.celsiusToFahrenheit(value);
        }

        // Fahrenheit -> Celsius
        if (from.equals("Fahrenheit")
                && to.equals("Celsius")) {

            return calculator.fahrenheitToCelsius(value);
        }

        // Kelvin -> Celsius
        if (from.equals("Kelvin")
                && to.equals("Celsius")) {

            return calculator.kelvinToCelsius(value);
        }

        // Celsius -> Kelvin
        if (from.equals("Celsius")
                && to.equals("Kelvin")) {

            return value + 273.15;
        }

        // Fahrenheit -> Kelvin
        if (from.equals("Fahrenheit")
                && to.equals("Kelvin")) {

            double celsius =
                    calculator.fahrenheitToCelsius(value);

            return celsius + 273.15;
        }

        // Kelvin -> Fahrenheit
        if (from.equals("Kelvin")
                && to.equals("Fahrenheit")) {

            double celsius =
                    calculator.kelvinToCelsius(value);

            return calculator.celsiusToFahrenheit(
                    celsius
            );
        }

        throw new IllegalArgumentException(
                "Unsupported conversion"
        );
    }

    // Load conversion history from database
    private void loadHistory() {

        historyArea.clear();

        List<TempRecord> records =
                recordDAO.getAllRecords();

        if (records.isEmpty()) {

            historyArea.setText(
                    "No conversions saved yet."
            );

            return;
        }

        for (TempRecord record : records) {

            historyArea.appendText(
                    String.format(
                            "%.2f [Unit %d] -> %.2f [Unit %d] | %s%n",
                            record.getInputValue(),
                            record.getInputUnitId(),
                            record.getOutputValue(),
                            record.getOutputUnitId(),
                            record.getCreatedAt()
                    )
            );
        }
    }

    // Display error message
    private void showError(String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}