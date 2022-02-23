package com.github.nedelis.actions.translate;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class TranslatorFactory {

    public static final ITranslator DEFAULT_TRANSLATOR = new Translator();

    private static final Set<ITranslator> registeredTranslators = new HashSet<>() {{
        add(DEFAULT_TRANSLATOR);
    }};

    public static @NotNull ITranslator getTranslator(@NotNull String translatorModificationName, @NotNull String fileExtension) {
        for(var existingTranslator : registeredTranslators) {
            if(existingTranslator.fileExtension().equals(fileExtension) || existingTranslator.translatorModificationName().equals(translatorModificationName)) {
                return existingTranslator;
            }
        }
        return DEFAULT_TRANSLATOR;
    }

    public static @NotNull ITranslator getTranslator(@NotNull Set<String> modifications, @NotNull String fileExtension) {
        for(var modification : modifications) {
            var translator = getTranslator(modification, fileExtension);
            if(!DEFAULT_TRANSLATOR.equals(translator)) {
                return translator;
            }
        }
        return DEFAULT_TRANSLATOR;
    }


    public static void registerTranslator(@NotNull ITranslator translatorToRegister) {
        registeredTranslators.add(translatorToRegister);
    }

}
