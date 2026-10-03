public class TempRecord {

    private int id;
    private double inputValue;
    private int inputUnitId;
    private double outputValue;
    private int outputUnitId;
    private String createdAt;

    // Constructor for saving a new conversion
    public TempRecord(
            double inputValue,
            int inputUnitId,
            double outputValue,
            int outputUnitId) {

        this.inputValue = inputValue;
        this.inputUnitId = inputUnitId;
        this.outputValue = outputValue;
        this.outputUnitId = outputUnitId;
    }

    // Constructor for reading a conversion from database
    public TempRecord(
            int id,
            double inputValue,
            int inputUnitId,
            double outputValue,
            int outputUnitId,
            String createdAt) {

        this.id = id;
        this.inputValue = inputValue;
        this.inputUnitId = inputUnitId;
        this.outputValue = outputValue;
        this.outputUnitId = outputUnitId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public double getInputValue() {
        return inputValue;
    }

    public int getInputUnitId() {
        return inputUnitId;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public int getOutputUnitId() {
        return outputUnitId;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}