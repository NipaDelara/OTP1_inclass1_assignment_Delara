
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TemperatureConverterGUI extends Application {

    // Start JavaFX application
    @Override
    public void start(Stage stage) throws Exception {

        // Load the GUI layout from FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/temperature.fxml")
        );

        // Create scene using FXML
        Scene scene = new Scene(loader.load());

        // Use Japanese-compatible font
        scene.getRoot().setStyle(
                "-fx-font-family: 'Noto Sans CJK JP';"
        );

        // Set application window title
        stage.setTitle("Temperature Converter");

        // Set the scene
        stage.setScene(scene);

        // Display the GUI window
        stage.show();
    }

    // Main method to launch JavaFX
    public static void main(String[] args) {
        launch(args);
    }
}
