package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.service.DecryptionService;
import by.cleverdeath.vita.validator.DecryptionValidator;
import by.cleverdeath.vita.validator.impl.DecryptionValidatorImpl;

import java.util.*;

public class DecryptionServiceImpl implements DecryptionService {

    public static final String SPACE = " ";

    @Override
    public Optional<String> decryptWithHedge(String message, Integer height) {
        DecryptionValidator decryptionValidator = new DecryptionValidatorImpl(); // создаём валидатор
        if (!decryptionValidator.validateHedgeParameters(message, height)) { // проверяем, чтобы все параметры соответствовали стандарту
            return Optional.empty();
        }
        int currentHeight = 0; // текущая высота
        boolean direction = true; // false - down, true - up
        List<Integer> levelLength = new ArrayList<>(); // длины подстрок на каждом уровне
        for (int i = 0; i < height; i++) { // добавляем нолики в список длин подстрок на каждом уровне
            levelLength.add(0);
        }
        for (int i = 0; i < message.length(); i++) { // идём по буквам в зашифрованном сообщении
            levelLength.set(currentHeight, levelLength.get(currentHeight) + 1); // на каждом уровне добавляем единичку в длину подстроки текущего уровня
            if (currentHeight == height - 1 || currentHeight == 0) { // когда упираемся в потолок или в пол, меняем направление движения
                direction = !direction;
            }
            if (direction) {
                currentHeight--;
            } else {
                currentHeight++;
            }
        }
        List<String> levelStrings = new ArrayList<>(); // подстроки на каждом уровне
        levelStrings.add(message.substring(0, levelLength.get(0))); // добавляем первую подстроку в список
        int startElement = 0;
        for (int i = 1; i < height; i++) {
            startElement += levelLength.get(i - 1); // считаем стартовый элемент для каждой первой подстроки
            levelStrings.add(message.substring(startElement, startElement + levelLength.get(i))); // добавляем подстроку
        }
        StringBuffer decryptedMessage = new StringBuffer();
        direction = true;
        currentHeight = 0;
        for (int i = 0; i < message.length(); i++) {
            decryptedMessage.append(levelStrings.get(currentHeight).charAt(0)); // берём букву на текущем уровне
            if (levelStrings.get(currentHeight).length() != 1) { // если подстрока текущего уровня непустая
                levelStrings.set(currentHeight, levelStrings.get(currentHeight).substring(1)); // удаляем первую букву в подстроке
            }
            if (currentHeight == height - 1 || currentHeight == 0) { // меняем направление когда упёрлись
                direction = !direction;
            }
            if (direction) {
                currentHeight--;
            } else {
                currentHeight++;
            }
        }
        return Optional.of(decryptedMessage.toString());
    }

    @Override
    public Optional<String> decryptWithKeyPhrase(String message, String keyPhrase) {
        DecryptionValidator decryptionValidator = new DecryptionValidatorImpl();
        if (!decryptionValidator.validateKeyPhraseParameters(message, keyPhrase)) { // валидация входных параметров
            return Optional.empty();
        }
        Map<Integer, Character> positionsWithCharacters = new HashMap<>();
        for (int i = 0; i < keyPhrase.length(); i++) { // заполняем мапу парами индекс буквы в ключевой фразе-буква в ключевой фразе
            positionsWithCharacters.put(i, keyPhrase.charAt(i));
        }
        List<Integer> sortedIndexes = positionsWithCharacters.entrySet() // сортируем мапу бо буквам и достаём из неё отсортированные индексы
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map((Map.Entry::getKey))
                .toList();
        StringBuffer decryptedMessage = new StringBuffer(SPACE.repeat(message.length())); // создаём расшифрованное сообщение из пробелов размеров с зашифрованное сообщение
        for (int i = 0; i < message.length() / keyPhrase.length(); i++) { // цикл по блокам
            int startElement = i * keyPhrase.length(); // стартовый элемент каждого блока
            for (int j = 0; j < sortedIndexes.size(); j++) { // цикл по отсортированным индексам ключевого слова
                decryptedMessage.setCharAt(startElement + sortedIndexes.get(j), message.charAt(startElement + j)); // заменяем пробел в расшифрованном сообщении на букву из зашифрованного, полученную по индексу ключевой фразы
            }
        }
        return Optional.of(decryptedMessage.toString());
    }

    @Override
    public Optional<String> decryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions) {
        DecryptionValidator decryptionValidator = new DecryptionValidatorImpl();
        if (!decryptionValidator.validateGridParameters(message, gridDimension, positions)) { // валидация входных параметров
            return Optional.empty();
        }
        StringBuffer decryptedMessage = new StringBuffer();
        for (int k = 0; k < message.length() / (gridDimension * gridDimension); k++) { // цикл по количеству решёток в зашифрованном сообщении,
            int startBlockIndex = k * gridDimension * gridDimension; // стартовый элемент блока(решётки)
            Character[][] grid = new Character[gridDimension][gridDimension]; // сама решётка
            for (int i = 0; i < gridDimension; i++) { // идём по решётке
                for (int j = 0; j < gridDimension; j++) {
                    grid[i][j] = message.charAt(i * gridDimension + startBlockIndex + j); // заполняем решётку по порядку буквами из зашифрованной последовательности
                }
            }

            for (int i = 0; i < gridDimension; i++) {
                for (int j = 0; j < positions.size(); j++) { // цикл по "дыркам" в решётке, что даны нам в параметрах метода
                    GridPosition position = positions.get(j);
                    decryptedMessage.append(grid[position.getX()][position.getY()]); // берём букву в позиции дырки и добавляем её в расшифрованное сообщение
                }
                grid = rotateGrid(grid); // поворачиваем решётку
            }
        }
        return Optional.of(decryptedMessage.toString());
    }

    @Override
    public Optional<String> decryptCesar(String message, Integer decryptionKey) {
        DecryptionValidator decryptionValidator = new DecryptionValidatorImpl();
        if (!decryptionValidator.validateSubstitutionTypeParameters(message, decryptionKey)) { // валидация данных
            return Optional.empty();
        }
        StringBuffer decryptedSequence = new StringBuffer(message);
        for (int i = 0; i < message.length(); i++) { // тут вроде всё понятно
            Character currentChar = message.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z') {
                Character replaceCharacter = (char) ((currentChar - 'A' + 26 - decryptionKey) % 26 + 'A');
                decryptedSequence.setCharAt(i, replaceCharacter);
            }
            if (currentChar >= 'a' && currentChar <= 'z') {
                Character replaceCharacter = (char) ((currentChar - 'a' + 26 - decryptionKey) % 26 + 'a');
                decryptedSequence.setCharAt(i, replaceCharacter);
            }
        }
        return Optional.of(decryptedSequence.toString());
    }

    private Character[][] rotateGrid(Character[][] grid) { // объяснение поворота есть в EncryptionServiceImpl
        Character[][] rotatedGrid = new Character[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                rotatedGrid[j][grid.length - i - 1] = grid[i][j];
            }
        }
        return rotatedGrid;
    }

    @Override
    public Optional<String> decryptWithSubstitution(String message, Integer decryptionKey) {
        DecryptionValidator decryptionValidator = new DecryptionValidatorImpl();
        if(!decryptionValidator.validateSubstitutionTypeParameters(message,decryptionKey)){ // валидация
            return Optional.empty();
        }
        StringBuffer decryptionSequence = new StringBuffer(message);
        for (int i = 0; i < message.length(); i++) { // тута вроде тоже всё понятно :)
            Character currentChar = message.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z') {
                Character replaceCharacter = (char) ((decryptionKey * (currentChar - 'A')) % 26 + 'A');
                decryptionSequence.setCharAt(i, replaceCharacter);
            }
            if (currentChar >= 'a' && currentChar <= 'z') {
                Character replaceCharacter = (char) ((decryptionKey * (currentChar - 'a')) % 26 + 'a');
                decryptionSequence.setCharAt(i, replaceCharacter);
            }
        }
        return Optional.of(decryptionSequence.toString());
    }
}
