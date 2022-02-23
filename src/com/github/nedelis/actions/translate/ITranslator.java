package com.github.nedelis.actions.translate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface ITranslator {

    void translate(@NotNull ArrayList<String> splitInput, @NotNull Set<String> modifications);

    @NotNull Map<String, Map<String, Object>> getTranslationResult();

    @NotNull String translatorModificationName();

    default @NotNull String fileExtension() {
        return "ncf";
    }

}
