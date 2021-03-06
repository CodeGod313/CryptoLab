package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.service.EncryptionService;
import by.cleverdeath.vita.validator.EncryptionValidator;
import by.cleverdeath.vita.validator.impl.EncryptionValidatorImpl;

import java.util.*;

public class EncryptionServiceImpl implements EncryptionService {

    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";

    @Override
    public Optional<String> encryptWithHedge(String message, Integer height) {
        EncryptionValidator encryptionValidator = new EncryptionValidatorImpl();
        if (!encryptionValidator.validateHedgeParameters(message, height)) {
            return Optional.empty();
        }
        int currentHeight = 0; // текущая высота, на которой мы находимся (0 - вершина изгороди, height - низ)
        boolean direction = true; // false - down, true - up (направление движения)
        List<StringBuffer> levelStrings = new ArrayList<>(); // слова, которые получаются из букв на разных уровнях (в методе в примере на верхнем уровне получается CTA, на втором RPORPY и т. д.)
        for (int i = 0; i < height; i++) { // просто инициализация объектов
            levelStrings.add(new StringBuffer());
        }
        for (int i = 0; i < message.length(); i++) { // цикл по сообщению, которое шифруем
            Character currentChar = message.charAt(i); // текущая буква сообщения, которое шифруем
            levelStrings.get(currentHeight).append(currentChar); // получаем слово с уровня, на котором находимся и добавляем в него текущую букву
            if (currentHeight == height - 1 || currentHeight == 0) { // если мы упёрлись в верх изгороди или в низ, меняем направление движения на противоположное(currentHeight == height - 1 это текущая высота упёрлась в низ изгороди, currentHeight == 0 это когда упёрлись вверх)
                direction = !direction;
            }
            if (direction) { // в зависимости от направления движения переходим на уровень выше/ниже
                currentHeight--;
            } else {
                currentHeight++;
            }
        }
        StringBuffer encryptedSequence = new StringBuffer(); // зашифрованная последовательность
        levelStrings.forEach(encryptedSequence::append); // просто соединяем слова, которые получились со слов со всех уровней
        return Optional.of(encryptedSequence.toString());
    }

    @Override
    public Optional<String> encryptWithKeyPhrase(String message, String keyPhrase) {
        EncryptionValidator encryptionValidator = new EncryptionValidatorImpl();
        if (!encryptionValidator.validateKeyPhraseParameters(message, keyPhrase)) {
            return Optional.empty();
        }
        Map<Integer, Character> positionsWithCharacters = new HashMap<>(); // мапа, в которой ключ - позиция буквы в влючевом слове, а значение -  сама буква
        for (int i = 0; i < keyPhrase.length(); i++) {
            positionsWithCharacters.put(i, keyPhrase.charAt(i)); // заполняем мапу
        }
        List<Integer> sortedIndexes = positionsWithCharacters.entrySet()// сортируем мапу по буквам (чтобы в начале были позиции букв, которые в алфавите стоят раньше)
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map((Map.Entry::getKey))
                .toList();
        String spaces = EMPTY_STRING;
        if (message.length() % keyPhrase.length() != 0) {
            spaces = SPACE.repeat(keyPhrase.length() - message.length() % keyPhrase.length()); // добавляем пробелы, чтобы размер подходил под ключевую фразу
        }
        StringBuffer readyMessageBuffer = new StringBuffer(message);
        readyMessageBuffer.append(spaces); // добавляем пробелы
        String readyMessage = readyMessageBuffer.toString();
        StringBuffer encryptedSequence = new StringBuffer(); // зашифрованное сообщение
        for (int i = 0; i < readyMessage.length() / keyPhrase.length(); i++) { // цикл по блокам, размеров с ключевую фразу
            int startSubarrayIndex = i * keyPhrase.length(); // вычисляем индекс начала блока
            sortedIndexes.forEach(x -> { // цикл по всем элементам
                int currentCharIndex = startSubarrayIndex + x; // начальный индекс + индекс из наших отсортированных (собираем шифр по порядку вхождения букв улючевой фразы в алфавит)
                encryptedSequence.append(readyMessage.charAt(currentCharIndex)); // добавляем букву в выходную последовательность
            });
        }
        return Optional.of(encryptedSequence.toString());
    }

    @Override
    public Optional<String> encryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions) {
        EncryptionValidator encryptionValidator = new EncryptionValidatorImpl();
        if (!encryptionValidator.validateGridParameters(message, gridDimension, positions)) {
            return Optional.empty();
        }
        String spaces = EMPTY_STRING;
        if (message.length() % (gridDimension * gridDimension) != 0) {
            spaces = SPACE.repeat(gridDimension * gridDimension - message.length() % (gridDimension * gridDimension));// дополняем шифруемый текст польностью, чтобы решётка была полностью заполненной
        }
        StringBuffer readyMessageBuffer = new StringBuffer(message); // зашифрованное сообщение (передаём в конструктор сообщение)
        readyMessageBuffer.append(spaces); //добавляем пробелы
        String readyMessage = readyMessageBuffer.toString(); // готовое сообщение
        StringBuffer encryptedSequence = new StringBuffer();
        for (int k = 0; k < readyMessage.length() / (gridDimension * gridDimension); k++) { // Заполняем решётку столько раз, сколько надо, чтобы зашифровать сообщение (если размерность решётки 4, то можно зашифровать сообщение длиной 4*4. Для того чтобы зашифровать сообщение побольше нам необходима ещё одна решётка)
            int startIndex = k * (gridDimension * gridDimension); // индекс начала шифруемого блока
            Character[][] grid = new Character[gridDimension][gridDimension]; // создание решётки, размерностью, переданной в параметры метода

            for (int i = 0; i < gridDimension; i++) { // проходим по блокам сообщения (блоки размерностью как и размерность решётки)
                int currentCharacter = i * gridDimension; // начальный индекс блока
                for (int j = 0; j < positions.size(); j++) {
                    GridPosition position = positions.get(j);
                    grid[position.getX()][position.getY()] = readyMessage.charAt(currentCharacter + j + startIndex); //добавляем букву в решётку по позиции, которую передали в параметры метода
                }
                grid = rotateGrid(grid); // поворачиваем решётку
            }

            for (int i = 0; i < gridDimension; i++) {
                for (int j = 0; j < gridDimension; j++) {
                    encryptedSequence.append(grid[i][j]); // складываем решётку в сообщение
                }
            }
        }
        return Optional.of(encryptedSequence.toString());
    }

    @Override
    public Optional<String> encryptCesar(String message, Integer encryptionKey) { // тут всё просто по формулам
        EncryptionValidator encryptionValidator = new EncryptionValidatorImpl();
        if (!encryptionValidator.validateSubstitutionTypeParameters(message, encryptionKey)) {
            return Optional.empty();
        }
        StringBuffer encryptedSequence = new StringBuffer(message);
        for (int i = 0; i < message.length(); i++) {
            Character currentChar = message.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z') {
                Character replaceCharacter = (char) ((currentChar - 'A' + encryptionKey) % 26 + 'A');
                encryptedSequence.setCharAt(i, replaceCharacter);
            }
            if (currentChar >= 'a' && currentChar <= 'z') {
                Character replaceCharacter = (char) ((currentChar - 'a' + encryptionKey) % 26 + 'a');
                encryptedSequence.setCharAt(i, replaceCharacter);
            }
        }
        return Optional.of(encryptedSequence.toString());
    }


    private Character[][] rotateGrid(Character[][] grid) {
        Character[][] rotatedGrid = new Character[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) { // проходим по решётке
                rotatedGrid[j][grid.length - i - 1] = grid[i][j]; // значение из обычной решётки записываем в ячейку в повёрнутой решётке с противоположными координатами
            }
        }
        return rotatedGrid;
    }

    @Override
    public Optional<String> encryptWithSubstitution(String message, Integer encryptionKey) { // тут тоже всё по формулам
        EncryptionValidator encryptionValidator = new EncryptionValidatorImpl();
        if (!encryptionValidator.validateSubstitutionTypeParameters(message, encryptionKey)) {
            return Optional.empty();
        }
        StringBuffer encryptedSequence = new StringBuffer(message);
        for (int i = 0; i < message.length(); i++) {
            Character currentChar = message.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z') {
                Character replaceCharacter = (char) ((encryptionKey * (currentChar - 'A')) % 26 + 'A');
                encryptedSequence.setCharAt(i, replaceCharacter);
            }
            if (currentChar >= 'a' && currentChar <= 'z') {
                Character replaceCharacter = (char) ((encryptionKey * (currentChar - 'a')) % 26 + 'a');
                encryptedSequence.setCharAt(i, replaceCharacter);
            }
        }
        return Optional.of(encryptedSequence.toString());
    }
}
