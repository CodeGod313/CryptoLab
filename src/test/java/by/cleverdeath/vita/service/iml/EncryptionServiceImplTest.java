package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.service.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EncryptionServiceImplTest {

    EncryptionService encryptionService;

    @BeforeAll
    void setUp() {
        encryptionService = new EncryptionServiceImpl();
    }

    @Test
    void encryptWithHedge() {
        String message = "CRYPTOGRAPHY";
        Integer height = 3;
        String expected = "CTARPORPYYGH";
        String actual = encryptionService.encryptWithHedge(message, height);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void encryptWithKeyPhrase() {
        String message = "ЭТО–_ЛЕКЦИЯ_ПО_АЛГОРИТМАМ_ШИФРОВАНИЯ";
        String keyPhrase = "КРИПТОГРАФИЯ";
        String expected = "ЦЕОЯЭЛ–ТК_И_ИО_МПГАОРЛТААОШИМРИ_ВФНЯ";
        String actual = encryptionService.encryptWithKeyPhrase(message, keyPhrase);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void encryptWithGrid() {
        String message = "ЭТОЛЕКЦИЯПОКРИПТ";
        Integer gridDimension = 4;
        List<GridPosition> positions = List.of(
                new GridPosition(0, 0),
                new GridPosition(1, 3),
                new GridPosition(3, 1),
                new GridPosition(2, 2)
        );
        String expected = "ЭКОРПКИТПТЛЦЕОИЯ";
        String actual = encryptionService.encryptWithGrid(message, gridDimension, positions);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void encryptCesar(){
        String message = "I LOVE YOU";
        Integer encryptionKey = 3;
        String expected = "L ORYH BRX";
        String actual = encryptionService.encryptCesar(message, encryptionKey);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void encryptWithSubstitution() {
        String message = "I LOVE YOU";
        Integer encryptionKey = 7;
        String expected = "E ZURC MUK";
        String actual = encryptionService.encryptWithSubstitution(message, encryptionKey);
        Assertions.assertEquals(expected, actual);
    }



    @Test
    void ass() {
        String message = "ЭТО–_ЛЕКЦИЯ_ПО_АЛГОРИТМАМ_ШИФРОВАНИЯ фывафыва фывафыва";
        String keyPhrase = "КРИПТОГРАФИЯ";
        String expected = "ЦЕОЯЭЛ–ТК_И_ИО_МПГАОРЛТААОШИМРИ_ВФНЯ";
        String actual = encryptionService.encryptWithKeyPhrase(message, keyPhrase);
        Assertions.assertEquals(expected, actual);
    }
}