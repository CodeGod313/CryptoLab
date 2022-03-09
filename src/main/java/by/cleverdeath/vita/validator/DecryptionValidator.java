package by.cleverdeath.vita.validator;

import by.cleverdeath.vita.entity.GridPosition;

import java.util.List;

public interface DecryptionValidator {
    boolean validateHedgeParameters(String message, Integer height);

    boolean validateKeyPhraseParameters(String message, String keyPhrase);

    boolean validateGridParameters(String message, Integer gridDimension, List<GridPosition> positions);

    boolean validateSubstitutionTypeParameters(String message, Integer encryptionKey);
}
