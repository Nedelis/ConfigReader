package com.github.nedelis.actions.translate;

import com.github.nedelis.vocabulary.keyword.FieldType;
import com.github.nedelis.vocabulary.keyword.FieldTypes;
import com.github.nedelis.vocabulary.token.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Translator implements ITranslator {

    private final Map<String, Map<String, Object>> TRANSLATION_RESULT = new HashMap<>();

    public Translator() {
    }

    private void init() {
        this.TRANSLATION_RESULT.clear();
        this.TRANSLATION_RESULT.put("DEFAULT", new HashMap<>());
    }

    public void translate(@NotNull ArrayList<String> splitInput, @NotNull Set<String> modifications) {
        this.init();

        var hasFieldTypes = !modifications.contains("NFT");
        var hasSections = !modifications.contains("NSS");

        var currentSectionName = "DEFAULT";
        var currentFieldType = FieldType.DEFAULT_FIELD_TYPE;
        var currentFieldName = "no_name_field";
        var currentFieldValue = new Object();

        var isNewField = true;

        for(var word : splitInput) {
            if(hasSections && word.startsWith(Tokens.PPSN.getTokenAsString()) && word.endsWith(Tokens.PPSN.getClosingAsString())) {
                currentSectionName = word.substring(Tokens.PPSN.getTokenAsString().length(), word.length() - Tokens.PPSN.getClosingAsString().length());
                continue;
            }

            if(hasFieldTypes && isNewField) {
                currentFieldType = FieldTypes.getFieldTypeByKeyword(word);
                isNewField = false;
            }

            if(word.startsWith(Tokens.PPVN.getTokenAsString()) && word.endsWith(Tokens.PPVN.getClosingAsString())) {
                currentFieldName = word.substring(Tokens.PPVN.getTokenAsString().length(), word.length() - Tokens.PPVN.getClosingAsString().length());
                continue;
            }

            if(word.startsWith(Tokens.PPVV.getTokenAsString()) && word.endsWith(Tokens.PPVV.getClosingAsString())) {
                currentFieldValue = currentFieldType.fromString(word.substring(Tokens.PPVV.getTokenAsString().length(), word.length() - Tokens.PPVV.getClosingAsString().length()));

                final var finalFieldName = currentFieldName;
                final var finalFieldValue = currentFieldValue;
                var field = new HashMap<String, Object>() {{ put(finalFieldName, finalFieldValue); }};

                if(TRANSLATION_RESULT.containsKey(currentSectionName)) TRANSLATION_RESULT.get(currentSectionName).putAll(field);
                else TRANSLATION_RESULT.put(currentSectionName, field);

                isNewField = true;
            }
        }

        if(TRANSLATION_RESULT.get("DEFAULT").isEmpty()) {
            TRANSLATION_RESULT.remove("DEFAULT");
        }
    }

    public @NotNull Map<String, Map<String, Object>> getTranslationResult() {
        return this.TRANSLATION_RESULT;
    }

    @Override
    public @NotNull String translatorModificationName() {
        return "DCT";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translator translator = (Translator) o;
        return Objects.equals(this.translatorModificationName(), translator.translatorModificationName()) && Objects.equals(this.fileExtension(), translator.fileExtension());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.translatorModificationName(), this.fileExtension());
    }
}
