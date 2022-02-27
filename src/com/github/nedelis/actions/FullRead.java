package com.github.nedelis.actions;

import com.github.nedelis.Settings;
import com.github.nedelis.actions.parse.ParserFactory;
import com.github.nedelis.actions.read.Reader;
import com.github.nedelis.actions.translate.TranslatorFactory;
import com.github.nedelis.util.collections.Config;
import com.github.nedelis.util.data.ReadFiles;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class FullRead {

    public static void read(@NotNull File file) {
        Settings.readSettings();

        var page = Reader.readConfigFile(file);
        var fileExtension = Reader.readFileExtension(file);
        var modifications = Reader.readModificationsLine(page.getLine(0));
        page.removeLine(0);

        var parser = ParserFactory.getParser(modifications, fileExtension);
        parser.parse(page, modifications);

        var translator = TranslatorFactory.getTranslator(modifications, fileExtension);
        translator.translate(parser.getParsingResult(), modifications);

        ReadFiles.getReadFiles().put(file.getName(), Config.fromMap(translator.getTranslationResult()));
    }

}
