package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.service.DecryptionService;
import by.cleverdeath.vita.service.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CrossTest {
    EncryptionService encryptionService;
    DecryptionService decryptionService;
    String expected;

    @BeforeAll
    void steUp() {
        encryptionService = new EncryptionServiceImpl();
        decryptionService = new DecryptionServiceImpl();
        expected = "zaraza"; // :*
    }

    @Test
    void testHedge() {
        String encrypted = encryptionService.encryptWithHedge(expected, 4).get();
        String actual = decryptionService.decryptWithHedge(encrypted, 4).get().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testKeyPhrase() {
        String encrypted = encryptionService.encryptWithKeyPhrase(expected, "KEYPHRASE").get();
        String actual = decryptionService.decryptWithKeyPhrase(encrypted, "KEYPHRASE").get().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGrid() {
        Integer gridDimension = 4;
        List<GridPosition> positions = List.of(
                new GridPosition(0, 0),
                new GridPosition(1, 3),
                new GridPosition(3, 1),
                new GridPosition(2, 2));
        String encrypted = encryptionService.encryptWithGrid(expected, gridDimension, positions).get();
        String actual = decryptionService.decryptWithGrid(encrypted, gridDimension, positions).get().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCesar() {
        String encrypted = encryptionService.encryptCesar(expected, 4).get();
        String actual = decryptionService.decryptCesar(encrypted, 4).get().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSubstitution() {
        String encrypted = encryptionService.encryptWithSubstitution(expected, 7).get();
        String actual = decryptionService.decryptWithSubstitution(encrypted, 15).get().trim();
        Assertions.assertEquals(expected, actual);
    }
}
