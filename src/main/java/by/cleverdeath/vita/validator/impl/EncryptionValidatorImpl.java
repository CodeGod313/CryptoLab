package by.cleverdeath.vita.validator.impl;

import by.cleverdeath.vita.entity.GridPosition;
import by.cleverdeath.vita.validator.EncryptionValidator;

import java.util.HashSet;
import java.util.List;

public class EncryptionValidatorImpl implements EncryptionValidator {

    public static final String REGEX_CHARACTER = "[A-Za-z\\s]+";

    @Override
    public boolean validateHedgeParameters(String message, Integer height) {
        return message != null &&
                height != null &&
                message.length() != 0 &&
                height >= 2;
    }

    @Override
    public boolean validateKeyPhraseParameters(String message, String keyPhrase) {
        return message != null &&
                keyPhrase != null &&
                message.length() != 0 &&
                keyPhrase.length() != 0;
    }

    @Override
    public boolean validateGridParameters(String message, Integer gridDimension, List<GridPosition> positions) {
        return message != null &&
                gridDimension != null &&
                positions != null &&
                gridDimension == positions.size() &&
                positions.stream().allMatch(
                        x -> x.getX() >= 0 &&
                                x.getX() < gridDimension &&
                                x.getY() >= 0 &&
                                x.getY() < gridDimension) &&
                new HashSet<>(positions).size() == positions.size();
    }

    @Override
    public boolean validateSubstitutionTypeParameters(String message, Integer encryptionKey) {
        return message != null &&
                encryptionKey != null &&
                message.length() != 0 &&
                message.matches(REGEX_CHARACTER) &&
                encryptionKey > 0 &&
                encryptionKey <= 26;
    }
}
