import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TemperatureFXMLController implements Initializable {

    // Database and calculator objects
    private final TempCalculator calculator = new TempCalculator();
    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
    private final TempRecordDAO recordDAO = new TempRecordDAO();

    // FXML components
    @FXML private Label titleLabel;
    @FXML private Label languageLabel;
    @FXML private Label temperatureLabel;
    @FXML private Label fromLabel;
    @FXML private Label toLabel;
    @FXML private Label resultLabel;
    @FXML private Label historyLabel;

    @FXML private TextField temperatureField;
    @FXML private ComboBox<String> languageBox;
    @FXML private ComboBox<TemperatureUnit> fromUnitBox;
    @FXML private ComboBox<TemperatureUnit> toUnitBox;
    @FXML private Button convertButton;
    @FXML private TextArea historyArea;

    private ResourceBundle bundle;

    // Initialize the application
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize MariaDB tables
        DBConnection.initializeDatabase();

        // Insert default temperature units
        initializeUnits();

        // Load units and saved conversions
        loadUnits();
        loadHistory();

        // Set up languages
        languageBox.getItems().setAll(
                "English",
                "Finnish",
                "Swedish",
                "Japanese",
                "Arabic"
        );

        languageBox.setValue("English");

        // Change language when selected
        languageBox.setOnAction(event -> changeLanguage());

        // Load default English translations
        changeLanguage();

        // Convert and save button
        convertButton.setOnAction(event -> convertTemperature());
    }

    // Add default temperature units
    private void initializeUnits() {
        unitDAO.addUnit(new TemperatureUnit("Celsius", "°C"));
        unitDAO.addUnit(new TemperatureUnit("Fahrenheit", "°F"));
        unitDAO.addUnit(new TemperatureUnit("Kelvin", "K"));
    }

    // Load units from MariaDB
    private void loadUnits() {

        List<TemperatureUnit> units = unitDAO.getAllUnits();

        fromUnitBox.getItems().setAll(units);
        toUnitBox.getItems().setAll(units);

        // Default selections
        for (TemperatureUnit unit : units) {
            if ("Celsius".equals(unit.getName())) {
                fromUnitBox.setValue(unit);
            }
            if ("Fahrenheit".equals(unit.getName())) {
                toUnitBox.setValue(unit);
            }
        }
    }

    // Change application language
    @FXML
    private void changeLanguage() {

        String language = languageBox.getValue();
        Locale locale;

        switch (language) {
            case "Finnish":
                locale = Locale.forLanguageTag("fi-FI");
                break;
            case "Swedish":
                locale = Locale.forLanguageTag("sv-SE");
                break;
            case "Japanese":
                locale = Locale.forLanguageTag("ja-JP");
                break;
            case "Arabic":
                locale = Locale.forLanguageTag("ar");
                break;
            default:
                locale = Locale.forLanguageTag("en-US");
        }

        // Load translation file
        bundle = ResourceBundle.getBundle("MessagesBundle", locale);

        // Update labels
        titleLabel.setText(bundle.getString("app.title"));
        languageLabel.setText(bundle.getString("label.language"));
        temperatureLabel.setText(bundle.getString("label.temperature"));
        temperatureField.setPromptText(
                bundle.getString("prompt.temperature")
        );
        fromLabel.setText(bundle.getString("label.from"));
        toLabel.setText(bundle.getString("label.to"));
        convertButton.setText(bundle.getString("button.convert"));
        historyLabel.setText(bundle.getString("label.history"));

        // Reset result label when language changes
        resultLabel.setText(bundle.getString("label.result"));
    }

    // Convert temperature and save result
    @FXML
    private void convertTemperature() {

        try {
            double input = Double.parseDouble(
                    temperatureField.getText().trim()
            );

            // Validate numeric input
            if (!Double.isFinite(input)) {
                showError("Please enter a finite temperature.");
                return;
            }

            TemperatureUnit from = fromUnitBox.getValue();
            TemperatureUnit to = toUnitBox.getValue();

            if (from == null || to == null) {
                showError("Please select temperature units.");
                return;
            }

            // Convert temperature
            double result = convert(
                    input,
                    from.getName(),
                    to.getName()
            );

            if (!Double.isFinite(result)) {
                showError("Temperature is outside the supported range.");
                return;
            }

            // Display converted result
            resultLabel.setText(
                    String.format(
                            Locale.US,
                            "%s: %.2f %s",
                            bundle.getString("label.result"),
                            result,
                            to.getSymbol()
                    )
            );

            // Create conversion record
            TempRecord record = new TempRecord(
                    input,
                    from.getId(),
                    result,
                    to.getId()
            );

            // Save record to MariaDB
            recordDAO.addRecord(record);

            // Refresh conversion history
            loadHistory();

        } catch (NumberFormatException e) {
            showError("Please enter a valid temperature.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // Handle all temperature conversions
    private double convert(double value, String from, String to) {

        // Use TempCalculator for all conversions
        return calculator.convert(value, from, to);
    }

    // Get unit symbol using MariaDB unit ID
    private String getUnitSymbol(int unitId) {

        // Look up the symbol from database units
        List<TemperatureUnit> units = unitDAO.getAllUnits();

        for (TemperatureUnit unit : units) {
            if (unit.getId() == unitId) {
                return unit.getSymbol();
            }
        }

        return "?";
    }

    // Load conversion history from MariaDB
    private void loadHistory() {

        historyArea.clear();

        List<TempRecord> records = recordDAO.getAllRecords();

        if (records.isEmpty()) {
            historyArea.setText("No conversions saved yet.");
            return;
        }

        // Display readable unit symbols instead of IDs
        for (TempRecord record : records) {

            String inputSymbol = getUnitSymbol(
                    record.getInputUnitId()
            );

            String outputSymbol = getUnitSymbol(
                    record.getOutputUnitId()
            );

            String historyText = String.format(
                    Locale.US,
                    "%.2f %s → %.2f %s | %s%n",
                    record.getInputValue(),
                    inputSymbol,
                    record.getOutputValue(),
                    outputSymbol,
                    record.getCreatedAt()
            );

            historyArea.appendText(historyText);
        }

        // Scroll to newest conversion
        historyArea.positionCaret(historyArea.getLength());
    }

    // Display error message
    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
