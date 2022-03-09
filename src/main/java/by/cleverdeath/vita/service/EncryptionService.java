package by.cleverdeath.vita.service;

import by.cleverdeath.vita.entity.GridPosition;

import java.util.List;
import java.util.Optional;

public interface EncryptionService {
    Optional<String> encryptWithHedge(String message, Integer height);

    Optional<String> encryptWithKeyPhrase(String message, String keyPhrase);

    Optional<String> encryptWithGrid(String message, Integer gridDimension, List<GridPosition> positions);

    Optional<String> encryptCesar(String message, Integer encryptionKey);

    Optional<String> encryptWithSubstitution(String message, Integer encryptionKey);
}
