public class TemperatureUnit {

    private int id;
    private String name;
    private String symbol;

    // Constructor without ID for new database records
    public TemperatureUnit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    // Constructor with ID for records read from database
    public TemperatureUnit(int id, String name, String symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    // Get ID
    public int getId() {
        return id;
    }

    // Get unit name
    public String getName() {
        return name;
    }

    // Get unit symbol
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}