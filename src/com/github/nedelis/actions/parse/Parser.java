package com.github.nedelis.actions.parse;

import com.github.nedelis.actions.read.Page;
import com.github.nedelis.actions.read.Reader;
import com.github.nedelis.vocabulary.keyword.FieldType;
import com.github.nedelis.vocabulary.keyword.FieldTypes;
import com.github.nedelis.vocabulary.keyword.IFieldType;
import com.github.nedelis.vocabulary.token.Tokens;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public final class Parser {

    private static final Map<String, Map<String, Object>> DATA = new HashMap<>();

    private static Set<String> parseModificationsLine(@NotNull String line) {
        var modifications = new HashSet<String>();
        line = line.replaceAll(" ", "");
        if(line.startsWith("modifications:")) {
            modifications = new HashSet<>();
            line = line.replaceFirst("modifications:", "");
            var stack = new StringBuilder();
            for(var ch : line.toCharArray()) {
                if(String.valueOf(ch).equals(String.valueOf(Tokens.COMMA.getToken().token()))) {
                    modifications.add(stack.toString());
                    stack = new StringBuilder();
                } else {
                    stack.append(ch);
                }
            }
            modifications.add(stack.toString());
        }
        return modifications;
    }

    private static @NotNull String parseSectionName(@NotNull String line) {
        return line.startsWith(Tokens.SQUARE_BRACKETS.getTokenAsString()) &&
               line.endsWith(Tokens.SQUARE_BRACKETS.getClosingAsString()) ?
               line.substring(1, line.length()-1) :
               "DEFAULT";
    }

    private static @NotNull Map<String, Object> parseField(@NotNull String line, boolean hasType) {
        line = line.replaceAll(" ", "");

        var fieldName = "";
        var fieldType = FieldType.DEFAULT_FIELD_TYPE;
        var fieldValue = "";

        var stack = new StringBuilder();
        int i = 0;
        var chArray = line.toCharArray();

        if(hasType) {
            while(!(chArray[i]+""+chArray[i+1]).equals(Tokens.DOUBLE_UNDERSCORE.getTokenAsString())) {
                stack.append(chArray[i]);
                ++i;
            }
            fieldType = FieldTypes.getFieldTypeByKeyword(stack.toString());
            stack = new StringBuilder();
        }

        if((chArray[i]+""+chArray[i+1]).equals(Tokens.DOUBLE_UNDERSCORE.getTokenAsString())) {
            i+=2;
            while(!(chArray[i]+""+chArray[i+1]).equals(Tokens.DOUBLE_UNDERSCORE.getTokenAsString())) {
                stack.append(chArray[i]);
                ++i;
            }
            fieldName = stack.toString();
            stack = new StringBuilder();
            i+=2;
        }

        if(String.valueOf(chArray[i]).equals(Tokens.EQUALS.getTokenAsString()) && String.valueOf(chArray[i+1]).equals(Tokens.ROUND_BRACKETS.getTokenAsString())) {
            i+=2;
            while(!String.valueOf(chArray[i]).equals(Tokens.ROUND_BRACKETS.getClosingAsString())) {
                stack.append(chArray[i]);
                ++i;
            }
            fieldValue = stack.toString();
        }

        String finalFieldName = fieldName;
        String finalFieldValue = fieldValue;
        IFieldType<?> finalFieldType = fieldType;
        return new HashMap<>() {{
            put(finalFieldName, finalFieldType.fromString(finalFieldValue));
        }};
    }

    private static void parseNCFC(@NotNull Page page, @NotNull Set<String> modifications, boolean hasType) {

    }

    private static void parseNCFL(@NotNull Page page, @NotNull Set<String> modifications, boolean hasType) {
        var currentSection = "DEFAULT";
        for(var line : page.page()) {
            if(!modifications.contains("NSS")) {
                var section = parseSectionName(line);
                if (!section.equals(currentSection) && !section.equals("DEFAULT")) {
                    currentSection = section;
                    continue;
                }
            }
            if(DATA.containsKey(currentSection)) DATA.get(currentSection).putAll(parseField(line, hasType));
            else DATA.put(currentSection, parseField(line, hasType));
        }
    }

    private static void parseNCFW(@NotNull Page page, @NotNull Set<String> modifications, boolean hasType) {

    }

    public static void parse(@NotNull File file) {
        var page = Reader.readConfigFile(file);
        var fileExtension = Reader.readFileExtension(file);

        DATA.clear();
        DATA.put("DEFAULT", new HashMap<>());

        var modifications = parseModificationsLine(page.getLine(0));
        if (!modifications.isEmpty()) page.removeLine(0);

        switch (fileExtension) {
            case "ncf":
                if(modifications.contains("VWP")) parseNCFW(page, modifications, !modifications.contains("NFT"));
                else if(modifications.contains("SCP")) parseNCFC(page, modifications, !modifications.contains("NFT"));
                else parseNCFL(page, modifications, !modifications.contains("NFT"));
                break;
            case "ncfc":
                parseNCFC(page, modifications, !modifications.contains("NFT"));
                break;
            case "ncfl":
                parseNCFL(page, modifications, !modifications.contains("NFT"));
                break;
            case "ncfw":
                parseNCFW(page, modifications, !modifications.contains("NFT"));
                break;
        }
    }

    public static Map<String, Map<String, Object>> getData() {
        return DATA;
    }
}