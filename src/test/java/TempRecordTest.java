import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordTest {

    @Test
    void createNewTempRecord() {

        TempRecord record =
                new TempRecord(
                        25.0,
                        1,
                        77.0,
                        2
                );

        assertEquals(
                25.0,
                record.getInputValue(),
                0.001
        );

        assertEquals(1, record.getInputUnitId());

        assertEquals(
                77.0,
                record.getOutputValue(),
                0.001
        );

        assertEquals(2, record.getOutputUnitId());
    }

    @Test
    void createTempRecordFromDatabase() {

        TempRecord record =
                new TempRecord(
                        10,
                        25.0,
                        1,
                        77.0,
                        2,
                        "2026-10-03 10:00:00"
                );

        assertEquals(10, record.getId());
        assertEquals(25.0, record.getInputValue(), 0.001);
        assertEquals(1, record.getInputUnitId());
        assertEquals(77.0, record.getOutputValue(), 0.001);
        assertEquals(2, record.getOutputUnitId());

        assertEquals(
                "2026-10-03 10:00:00",
                record.getCreatedAt()
        );
    }
}