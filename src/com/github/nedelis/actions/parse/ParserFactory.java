package com.github.nedelis.actions.parse;

import com.github.nedelis.actions.read.Page;
import com.github.nedelis.util.annotations.parser.AParser;
import com.github.nedelis.util.annotations.parser.AParsingMethod;
import com.github.nedelis.util.annotations.parser.AParsingResult;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
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

    @Deprecated
    public static void registerParser(@NotNull Object parserToRegister) {
        if(parserToRegister.getClass().isAnnotationPresent(AParser.class)) {
            AParser parserParams = parserToRegister.getClass().getAnnotation(AParser.class);
            Method parseMethod = null;
            Field parsingResultField = null;
            Method parsingResultMethod = null;
            for(var m : parserToRegister.getClass().getDeclaredMethods()) {
                if(m.isAnnotationPresent(AParsingMethod.class)) {
                    parseMethod = m;
                    break;
                } else if(m.isAnnotationPresent(AParsingResult.class)) {
                    parsingResultMethod = m;
                    break;
                }
            }
            for(var f : parserToRegister.getClass().getDeclaredFields()) {
                if(f.isAnnotationPresent(AParsingResult.class)) {
                    parsingResultField = f;
                    break;
                }
            }

            Method finalParseMethod = parseMethod;
            Method finalParsingResultMethod = parsingResultMethod;
            Field finalParsingResultField = parsingResultField;
            registerParser(new IParser() {
                @Override
                public void parse(@NotNull Page page, @NotNull Set<String> modifications) {
                    if(finalParseMethod != null) {
                        try {
                            finalParseMethod.invoke(parserToRegister, page, modifications);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @SuppressWarnings("unchecked")
                @Override
                public @NotNull ArrayList<String> getParsingResult() {
                    if(finalParsingResultMethod != null) {
                        try {
                            return (ArrayList<String>) finalParsingResultMethod.invoke(parserToRegister);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if(finalParsingResultField != null){
                        try {
                            return (ArrayList<String>) finalParsingResultField.get(new ArrayList<String>());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    return new ArrayList<>();
                }

                @Override
                public @NotNull String parserModificationName() {
                    return parserParams.parserModificationName();
                }

                @Override
                public @NotNull String fileExtension() {
                    return parserParams.fileExtension();
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    IParser parser = (IParser) o;
                    return Objects.equals(this.parserModificationName(), parser.parserModificationName()) && Objects.equals(this.fileExtension(), parser.fileExtension());
                }

                @Override
                public int hashCode() {
                    return Objects.hash(this.parserModificationName(), this.fileExtension());
                }

            });
        }
    }

    public static void registerParser(@NotNull IParser parserToRegister) {
        registeredParsers.add(parserToRegister);
    }

}
