package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.service.EncryptionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncryptionServiceImpl implements EncryptionService {

    public static final String SPACE = " ";

    @Override
    public String encryptWithHedge(String message, Integer height) {
        int currentHeight = 0; // текущая высота, на которой мы находимся (0 - вершина изгороди, height - низ)
        boolean direction = false; // false - down, true - up (направление движения)
        List<StringBuffer> levelStrings = new ArrayList<>(); // слова, которые получаются из букв на разных уровнях (в методе в примере на верхнем уровне получается CTA, на втором RPORPY и т. д.)
        for (int i = 0; i < height; i++) { // просто инициализация объектов
            levelStrings.add(new StringBuffer());
        }
        boolean firstIteration = true; // переменная, которая определяет, первая ли это итерация нижнего цикла или нет (только что понял, что можно и без неё :) )
        for (int i = 0; i < message.length(); i++) { // цикл по сообщению, которое шифруем
            Character currentChar = message.charAt(i); // текущая буква сообщения, которое шифруем
            levelStrings.get(currentHeight).append(currentChar); // получаем слово с уровня, на котором находимся и добавляем в него текущую букву
            if (currentHeight == height - 1 || (currentHeight == 0 && !firstIteration)) { // если мы упёрлись в верх изгороди или в низ, меняем направление движения на противоположное(currentHeight == height - 1 это текущая высота упёрлась в низ изгороди, currentHeight == 0 это когда упёрлись вверх)
                direction = !direction;
            }
            if (direction) { // в зависимости от направления движения переходим на уровень выше/ниже
                currentHeight--;
            } else {
                currentHeight++;
            }
            firstIteration = false;
        }
        StringBuffer encryptedSequence = new StringBuffer(); // зашифрованная последовательность
        levelStrings.forEach(encryptedSequence::append); // просто соединяем слова, которые получились со слов со всех уровней
        return encryptedSequence.toString();
    }

    @Override
    public String encryptWithKeyPhrase(String message, String keyPhrase) {
        Map<Integer, Character> positionsWithCharacters = new HashMap<>();
        for (int i = 0; i < keyPhrase.length(); i++) {
            positionsWithCharacters.put(i, keyPhrase.charAt(i));
        }
        List<Integer> sortedIndexes = positionsWithCharacters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map((Map.Entry::getKey))
                .toList();
        String spaces = SPACE.repeat(message.length() % keyPhrase.length());
        StringBuffer readyMessageBuffer = new StringBuffer(message);
        readyMessageBuffer.append(spaces);
        String readyMessage = readyMessageBuffer.toString();
        StringBuffer encryptedSequence = new StringBuffer();
        for (int i = 0; i < readyMessage.length() / keyPhrase.length(); i++) {
            int startSubarrayIndex = i * keyPhrase.length();
            sortedIndexes.forEach(x -> {
                int currentCharIndex = startSubarrayIndex + x;
                encryptedSequence.append(readyMessage.charAt(currentCharIndex));
            });
        }
        return encryptedSequence.toString();
    }

    @Override
    public String encryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions) {
        Character[][] grid = new Character[gridDimension][gridDimension];
        String spaces = SPACE.repeat(message.length() % gridDimension);
        StringBuffer readyMessageBuffer = new StringBuffer(message);
        readyMessageBuffer.append(spaces);
        String readyMessage = readyMessageBuffer.toString();
        for (int i = 0; i < readyMessage.length() / gridDimension; i++) {
            int currentCharacter = i * gridDimension;
            for (int j = 0; j < positions.size(); j++) {
                GridPosition position = positions.get(j);
                grid[position.getX()][position.getY()] = readyMessage.charAt(currentCharacter + j);
            }
            grid = rotateGrid(grid);
        }
        StringBuffer encryptedSequence = new StringBuffer();
        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {
                encryptedSequence.append(grid[i][j]);
            }
        }
        return encryptedSequence.toString();
    }

    @Override
    public String encryptCesar(String message, Integer encryptionKey) {
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
        return encryptedSequence.toString();
    }


    private Character[][] rotateGrid(Character[][] grid) {
        Character[][] rotatedGrid = new Character[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                rotatedGrid[j][grid.length - i - 1] = grid[i][j];
            }
        }
        return rotatedGrid;
    }

    @Override
    public String encryptWithSubstitution(String message, Integer encryptionKey) {
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
        return encryptedSequence.toString();
    }
}
