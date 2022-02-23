package com.github.nedelis.actions;

import com.github.nedelis.actions.parse.Parser;
import com.github.nedelis.actions.parse.ParserFactory;
import com.github.nedelis.actions.read.Reader;
import com.github.nedelis.actions.translate.TranslatorFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FullRead {

    private static final Map<String, Map<String, Map<String, Object>>> readFiles = new HashMap<>();

    public static void read(@NotNull File file) {
        var page = Reader.readConfigFile(file);
        var fileExtension = Reader.readFileExtension(file);
        var modifications = Reader.readModificationsLine(page.getLine(0));
        page.removeLine(0);

        var parser = ParserFactory.getParser(modifications, fileExtension);
        parser.parse(page, modifications);

        var translator = TranslatorFactory.getTranslator(modifications, fileExtension);
        translator.translate(parser.getParsingResult(), modifications);

        readFiles.put(file.getName(), Map.copyOf(translator.getTranslationResult()));
    }

    public static @NotNull Map<String, Map<String, Map<String, Object>>> getReadFiles() {
        return readFiles;
    }

}
