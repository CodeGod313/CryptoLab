package by.cleverdeath.vita.service;

import by.cleverdeath.vita.entyties.GridPosition;

import java.util.List;

public interface EncryptionService {
    String encryptWithHedge(String message, Integer height);

    String encryptWithKeyPhrase(String message, String keyPhrase);

    String encryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions);

    String encryptCesar(String message, Integer encryptionKey);

    String encryptWithSubstitution(String message, Integer encryptionKey);
}
