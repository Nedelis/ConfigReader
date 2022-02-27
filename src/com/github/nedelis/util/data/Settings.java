package com.github.nedelis.util.data;

import com.github.nedelis.util.collections.Config;
import com.github.nedelis.vocabulary.token.IToken;
import com.github.nedelis.vocabulary.token.Token;
import com.github.nedelis.vocabulary.token.Tokens;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Settings {

    private static String absolutePathToSettings = "";
    private static final Config<Object> settings = new Config<>();

    public static void setAbsolutePathToSettings(@NotNull File settingsFile) {
        absolutePathToSettings = settingsFile.getAbsolutePath();
    }

    private static void init() {

        if(absolutePathToSettings.isEmpty()) {
            var dirs = new File("src/data");
            var file = new File("src/data/reader.properties");
            try {
                //noinspection StatementWithEmptyBody
                if(dirs.mkdirs() || file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            absolutePathToSettings = file.getAbsolutePath();
        }

        var defaultTokens = new HashMap<String, Object>() {{
            put("PREFIX_AND_POSTFIX_OF_SECTION_NAME", new Token('[', ']'));
            put("PREFIX_AND_POSTFIX_OF_VAR_NAME", new Token("__", "__"));
            put("PREFIX_AND_POSTFIX_OF_VAR_VALUE", new Token('(', ')'));
            put("ASSIGNMENT_OPERATOR", new Token('='));
            put("ENUMERATION_OPERATOR", new Token(','));
            put("LINE_COMMENT_PREFIX", new Token("//"));
        }};
        settings.putToSection("Tokens", defaultTokens);
    }

    private static @NotNull HashMap<String, String> propertiesToHashMap(@NotNull Properties properties) {
        var map = new HashMap<String, String>();
        for(var key : properties.keySet()) map.put(key.toString(), properties.getProperty(key.toString()));
        return map;
    }

    public static void readSettings() {
        init();

        var propertiesFromFile = new Properties();
        try {
            propertiesFromFile.load(Files.newInputStream(Path.of(absolutePathToSettings)));
        } catch (IOException e) {
            System.err.println("Properties file wasn't found!");
        }

        var propertiesMap = propertiesToHashMap(propertiesFromFile);
        readTokens(propertiesMap);
    }

    private static void readTokens(@NotNull HashMap<String, String> properties) {
        for(var key : properties.keySet()) {
            if(key.startsWith("tok.")) {
                var value = properties.get(key);
                var params = value.split(" ");
                key = key.substring(key.indexOf("tok.") + 4);
                if(params.length == 1) settings.putToSection("Tokens", Map.of(key, new Token(params[0])));
                else if(params.length == 2) settings.putToSection("Tokens", Map.of(key, new Token(params[0], params[1])));
            }
        }
    }

    public static @NotNull IToken getTokenByName(@NotNull String name) {
        var field = new Object();
        if(settings.containsField("Tokens", name)) field = settings.getFieldValue("Tokens", name);
        return field instanceof IToken token ? token : Tokens.EMPTY_TOKEN;
    }

    @SuppressWarnings("unused")
    public static void setSetting(@NotNull String settingGroupPrefix, @NotNull String settingName, @NotNull String value) {
        init();

        var propertiesFromFile = new Properties();
        try {
            propertiesFromFile.load(Files.newInputStream(Path.of(absolutePathToSettings)));
        } catch (IOException e) {
            System.err.println("Properties file wasn't found!");
        }

        var propertiesMap = propertiesToHashMap(propertiesFromFile);
        for(var key : propertiesMap.keySet()) {
            if(key.startsWith(settingGroupPrefix)) {
                propertiesFromFile.setProperty(settingGroupPrefix + settingName, value);
            }
        }

        try {
            propertiesFromFile.store(Files.newOutputStream(Path.of(absolutePathToSettings)), "Update of field " + settingGroupPrefix + settingName);
        } catch (IOException e) {
            System.err.println("Properties file wasn't found!");
        }
    }

}
