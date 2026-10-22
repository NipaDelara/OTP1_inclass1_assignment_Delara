
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocalizationConsole {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //Select language
        System.out.println("Select your language:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.println("4. Japanese");

        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();

        //Set locale
        Locale locale;

        switch (choice) {
            case "2" -> locale = Locale.forLanguageTag("fi-FI");
            case "3" -> locale = Locale.forLanguageTag("sv-SE");
            case "4" -> locale = Locale.forLanguageTag("ja-JP");
            default -> locale = Locale.US;
        }

        // Load translations
        ResourceBundle bundle =
                ResourceBundle.getBundle("MessagesBundle", locale);

        //Display translated messages
        System.out.println("\n" + bundle.getString("title"));
        System.out.println(bundle.getString("message"));

        //Temperature conversion
        TemperatureConverter converter = new TemperatureConverter();

        System.out.print(bundle.getString("temperature") + ": ");

        double celsius = Double.parseDouble(scanner.nextLine());

        double fahrenheit = converter.celsiusToFahrenheit(celsius);

        System.out.println(bundle.getString("result") + ":");
        System.out.println(celsius + " °C = " + fahrenheit + " °F");
    }
}
