package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entyties.GridPosition;
import by.cleverdeath.vita.service.DecryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DecryptionServiceImplTest {
    DecryptionService decryptionService;

    @BeforeAll
    void setUp() {
        decryptionService = new DecryptionServiceImpl();
    }

    @Test
    void decryptWithHedge() {
        String message = "CTARPORPYYGH";
        Integer height = 3;
        String expected = "CRYPTOGRAPHY";
        String actual = decryptionService.decryptWithHedge(message, height);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decryptWithKeyPhrase() {
        String message = "ЦЕОЯЭЛ–ТК_И_ИО_МПГАОРЛТААОШИМРИ_ВФНЯ";
        String keyPhrase = "КРИПТОГРАФИЯ";
        String expected = "ЭТО–_ЛЕКЦИЯ_ПО_АЛГОРИТМАМ_ШИФРОВАНИЯ";
        String actual = decryptionService.decryptWithKeyPhrase(message, keyPhrase);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decryptWithGrid() {
        String message = "ЭКОРПКИТПТЛЦЕОИЯ";
        Integer gridDimension = 4;
        List<GridPosition> positions = List.of(
                new GridPosition(0, 0),
                new GridPosition(1, 3),
                new GridPosition(3, 1),
                new GridPosition(2, 2)
        );
        String expected = "ЭТОЛЕКЦИЯПОКРИПТ";
        String actual = decryptionService.decryptWithGrid(message, gridDimension, positions);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decryptCesar(){
        String message = "L ORYH BRX";
        Integer encryptionKey = 3;
        String expected = "I LOVE YOU";
        String actual = decryptionService.decryptCesar(message, encryptionKey);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decryptWithSubstitution() {
        String message = "E ZURC MUK";
        Integer encryptionKey = 15;
        String expected = "I LOVE YOU";
        String actual = decryptionService.decryptWithSubstitution(message, encryptionKey);
        Assertions.assertEquals(expected, actual);
    }
}