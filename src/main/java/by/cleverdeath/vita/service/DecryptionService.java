package by.cleverdeath.vita.service;

import by.cleverdeath.vita.entity.GridPosition;

import java.util.List;
import java.util.Optional;

public interface DecryptionService {
    Optional<String> decryptWithHedge(String message, Integer height);

    Optional<String> decryptWithKeyPhrase(String message, String keyPhrase);

    Optional<String> decryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions);

    Optional<String> decryptCesar(String message, Integer decryptionKey);

    Optional<String> decryptWithSubstitution(String message, Integer decryptionKey);
}
