package com.github.nedelis.actions.parse;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class ParserFactory {

    public static final IParser DEFAULT_PARSER = new Parser();

    private static final Set<IParser> registeredParsers = new HashSet<>() {{
        add(DEFAULT_PARSER);
    }};

    public static @NotNull IParser getParser(@NotNull String parserModificationName, @NotNull String parserFileExtension) {
        for(var existingParser : registeredParsers) {
            if(existingParser.fileExtension().equals(parserFileExtension) || existingParser.parserModificationName().equals(parserModificationName)) {
                return existingParser;
            }
        }
        return DEFAULT_PARSER;
    }

    public static @NotNull IParser getParser(@NotNull Set<String> modifications, @NotNull String fileExtension) {
        for(var modification : modifications) {
            var parser = getParser(modification, fileExtension);
            if(!DEFAULT_PARSER.equals(parser)) {
                return parser;
            }
        }
        return DEFAULT_PARSER;
    }

    public static void registerParser(@NotNull IParser parserToRegister) {
        registeredParsers.add(parserToRegister);
    }

}
