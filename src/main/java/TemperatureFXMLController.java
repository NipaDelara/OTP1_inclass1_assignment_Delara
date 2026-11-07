import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;

import java.net.URL;
import java.text.NumberFormat;
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

    // Current language and translations
    private ResourceBundle bundle;
    private Locale currentLocale = Locale.US;
    private String currentLanguage = "en";

    // Initialize application
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize existing MariaDB tables
        DBConnection.initializeDatabase();

        // Insert default units if missing
        initializeUnits();

        // Configure language selector
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

        // Load English UI and database translations
        changeLanguage();

        // Load conversion history
        loadHistory();

        // Configure convert button
        convertButton.setOnAction(
                event -> convertTemperature()
        );
    }

    // Insert default temperature units
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

    // Load translated units from MariaDB
    private void loadUnits() {

        TemperatureUnit previousFrom =
                fromUnitBox.getValue();

        TemperatureUnit previousTo =
                toUnitBox.getValue();

        // Preserve selected IDs
        int fromId = previousFrom != null
                ? previousFrom.getId() : -1;

        int toId = previousTo != null
                ? previousTo.getId() : -1;

        List<TemperatureUnit> units =
                unitDAO.getLocalizedUnits(currentLanguage);

        fromUnitBox.getItems().setAll(units);
        toUnitBox.getItems().setAll(units);

        // Restore previous selections
        for (TemperatureUnit unit : units) {

            if (unit.getId() == fromId ||
                    (fromId == -1 &&
                            "°C".equals(unit.getSymbol()))) {

                fromUnitBox.setValue(unit);
            }

            if (unit.getId() == toId ||
                    (toId == -1 &&
                            "°F".equals(unit.getSymbol()))) {

                toUnitBox.setValue(unit);
            }
        }
    }

    // Change UI and database language
    @FXML
    private void changeLanguage() {

        String language = languageBox.getValue();

        if (language == null) {
            language = "English";
        }

        switch (language) {

            case "Finnish":
                currentLocale =
                        Locale.forLanguageTag("fi-FI");
                break;

            case "Swedish":
                currentLocale =
                        Locale.forLanguageTag("sv-SE");
                break;

            case "Japanese":
                currentLocale =
                        Locale.forLanguageTag("ja-JP");
                break;

            case "Arabic":
                currentLocale =
                        Locale.forLanguageTag("ar");
                break;

            default:
                currentLocale =
                        Locale.forLanguageTag("en-US");
        }

        // Database language code
        currentLanguage = currentLocale.getLanguage();

        // Static UI translations from properties files
        bundle = ResourceBundle.getBundle(
                "MessagesBundle",
                currentLocale
        );

        titleLabel.setText(
                bundle.getString("app.title")
        );

        languageLabel.setText(
                bundle.getString("label.language")
        );

        temperatureLabel.setText(
                bundle.getString("label.temperature")
        );

        temperatureField.setPromptText(
                bundle.getString("prompt.temperature")
        );

        fromLabel.setText(
                bundle.getString("label.from")
        );

        toLabel.setText(
                bundle.getString("label.to")
        );

        convertButton.setText(
                bundle.getString("button.convert")
        );

        historyLabel.setText(
                bundle.getString("label.history")
        );

        resultLabel.setText(
                bundle.getString("label.result")
        );

        // Localized names from MariaDB
        loadUnits();

        // Arabic uses right-to-left orientation
        applyOrientation();

        // Refresh history formatting
        loadHistory();
    }

    // Apply RTL for Arabic, LTR for other languages
    private void applyOrientation() {

        NodeOrientation orientation =
                "ar".equals(currentLanguage)
                        ? NodeOrientation.RIGHT_TO_LEFT
                        : NodeOrientation.LEFT_TO_RIGHT;

        // Use an existing control's scene root
        // when the FXML has been attached to a scene.
        if (titleLabel.getScene() != null) {
            titleLabel.getScene()
                    .getRoot()
                    .setNodeOrientation(orientation);
        } else {
            titleLabel.setNodeOrientation(orientation);
            languageLabel.setNodeOrientation(orientation);
            temperatureLabel.setNodeOrientation(orientation);
            fromLabel.setNodeOrientation(orientation);
            toLabel.setNodeOrientation(orientation);
            resultLabel.setNodeOrientation(orientation);
            historyLabel.setNodeOrientation(orientation);
            temperatureField.setNodeOrientation(orientation);
            languageBox.setNodeOrientation(orientation);
            fromUnitBox.setNodeOrientation(orientation);
            toUnitBox.setNodeOrientation(orientation);
            convertButton.setNodeOrientation(orientation);
            historyArea.setNodeOrientation(orientation);
        }
    }

    // Convert temperature and save result
    @FXML
    private void convertTemperature() {

        try {

            double input = Double.parseDouble(
                    temperatureField.getText().trim()
            );

            if (!Double.isFinite(input)) {
                showError(
                        "Please enter a finite temperature."
                );
                return;
            }

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

            // Use original English names for calculator
            String fromName =
                    getOriginalUnitName(from.getId());

            String toName =
                    getOriginalUnitName(to.getId());

            double result = calculator.convert(
                    input,
                    fromName,
                    toName
            );

            if (!Double.isFinite(result)) {
                showError(
                        "Temperature is outside the supported range."
                );
                return;
            }

            // Localized number formatting
            NumberFormat numberFormat =
                    NumberFormat.getNumberInstance(
                            currentLocale
                    );

            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);

            resultLabel.setText(
                    bundle.getString("label.result")
                            + ": "
                            + numberFormat.format(result)
                            + " "
                            + to.getSymbol()
            );

            // Save conversion to MariaDB
            TempRecord record = new TempRecord(
                    input,
                    from.getId(),
                    result,
                    to.getId()
            );

            recordDAO.addRecord(record);

            // Refresh history
            loadHistory();

        } catch (NumberFormatException e) {

            showError(
                    "Please enter a valid temperature."
            );

        } catch (IllegalArgumentException e) {

            showError(e.getMessage());
        }
    }

    // Find original unit name for calculations
    private String getOriginalUnitName(int unitId) {

        List<TemperatureUnit> units =
                unitDAO.getAllUnits();

        for (TemperatureUnit unit : units) {

            if (unit.getId() == unitId) {
                return unit.getName();
            }
        }

        throw new IllegalArgumentException(
                "Unknown temperature unit ID: " + unitId
        );
    }

    // Get unit symbol from database
    private String getUnitSymbol(int unitId) {

        List<TemperatureUnit> units =
                unitDAO.getAllUnits();

        for (TemperatureUnit unit : units) {

            if (unit.getId() == unitId) {
                return unit.getSymbol();
            }
        }

        return "?";
    }

    // Display the latest five conversions
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

        NumberFormat numberFormat =
                NumberFormat.getNumberInstance(
                        currentLocale
                );

        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        // Display last five records returned by DAO
        int count = Math.min(5, records.size());

        for (int i = 0; i < count; i++) {

            TempRecord record = records.get(i);

            String inputSymbol = getUnitSymbol(
                    record.getInputUnitId()
            );

            String outputSymbol = getUnitSymbol(
                    record.getOutputUnitId()
            );

            String historyText =
                    numberFormat.format(
                            record.getInputValue()
                    )
                            + " " + inputSymbol
                            + " → "
                            + numberFormat.format(
                            record.getOutputValue()
                    )
                            + " " + outputSymbol
                            + " | "
                            + record.getCreatedAt()
                            + System.lineSeparator();

            historyArea.appendText(historyText);
        }

        historyArea.positionCaret(
                historyArea.getLength()
        );
    }

    // Display error dialog
    private void showError(String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
