package by.cleverdeath.vita.service.iml;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.service.DecryptionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecryptionServiceImpl implements DecryptionService {

    public static final String SPACE = " ";

    @Override
    public String decryptWithHedge(String message, Integer height) {
        int currentHeight = 0;
        boolean direction = false; // false - down, true - up
        List<Integer> levelLength = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            levelLength.add(0);
        }
        boolean firstIteration = true;
        for (int i = 0; i < message.length(); i++) {
            Character currentChar = message.charAt(i);
            levelLength.set(currentHeight, levelLength.get(currentHeight) + 1);
            if (currentHeight == height - 1 || (currentHeight == 0 && !firstIteration)) {
                direction = !direction;
            }
            if (direction) {
                currentHeight--;
            } else {
                currentHeight++;
            }
            firstIteration = false;
        }
        List<String> levelStrings = new ArrayList<>();
        levelStrings.add(message.substring(0, levelLength.get(0)));
        int startElement = 0;
        for (int i = 1; i < height; i++) {
            startElement += levelLength.get(i - 1);
            levelStrings.add(message.substring(startElement, startElement + levelLength.get(i)));
        }
        StringBuffer decryptedMessage = new StringBuffer();
        firstIteration = false;
        for (int i = 0; i < message.length(); i++) {
            decryptedMessage.append(levelStrings.get(currentHeight).charAt(0));
            if (levelStrings.get(currentHeight).length() != 1) {
                levelStrings.set(currentHeight, levelStrings.get(currentHeight).substring(1));
            }
            if (currentHeight == height - 1 || (currentHeight == 0 && !firstIteration)) {
                direction = !direction;
            }
            if (direction) {
                currentHeight--;
            } else {
                currentHeight++;
            }
            firstIteration = false;
        }
        return decryptedMessage.toString();
    }

    @Override
    public String decryptWithKeyPhrase(String message, String keyPhrase) {
        Map<Integer, Character> positionsWithCharacters = new HashMap<>();
        for (int i = 0; i < keyPhrase.length(); i++) {
            positionsWithCharacters.put(i, keyPhrase.charAt(i));
        }
        List<Integer> sortedIndexes = positionsWithCharacters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map((Map.Entry::getKey))
                .toList();
        StringBuffer decryptedMessage = new StringBuffer(SPACE.repeat(message.length()));
        for (int i = 0; i < message.length() / keyPhrase.length(); i++) {
            int startElement = i * keyPhrase.length();
            for (int j = 0; j < sortedIndexes.size(); j++) {
                decryptedMessage.setCharAt(startElement + sortedIndexes.get(j), message.charAt(startElement + j));
            }
        }
        return decryptedMessage.toString();
    }

    @Override
    public String decryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions) {
        Character[][] grid = new Character[gridDimension][gridDimension];
        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {
                grid[i][j] = message.charAt(i * gridDimension + j);
            }
        }
        StringBuffer decryptedMessage = new StringBuffer();
        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < positions.size(); j++) {
                GridPosition position = positions.get(j);
                decryptedMessage.append(grid[position.getX()][position.getY()]);
            }
            grid = rotateGrid(grid);
        }
        return decryptedMessage.toString();
    }

    @Override
    public String decryptCesar(String message, Integer decryptionKey) {
        StringBuffer decryptedSequence = new StringBuffer(message);
        for (int i = 0; i < message.length(); i++) {
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
        return decryptedSequence.toString();
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
    public String decryptWithSubstitution(String message, Integer decryptionKey) {
        StringBuffer decryptionSequence = new StringBuffer(message);
        for (int i = 0; i < message.length(); i++) {
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
        return decryptionSequence.toString();
    }
}
