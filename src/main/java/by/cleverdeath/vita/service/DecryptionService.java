package by.cleverdeath.vita.service;

import by.cleverdeath.vita.entity.GridPosition;

import java.util.List;

public interface DecryptionService {
    String decryptWithHedge(String message, Integer height);

    String decryptWithKeyPhrase(String message, String keyPhrase);

    String decryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions);

    String decryptCesar(String message, Integer decryptionKey);

    String decryptWithSubstitution(String message, Integer decryptionKey);
}
