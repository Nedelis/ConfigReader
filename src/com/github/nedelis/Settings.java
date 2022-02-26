package com.github.nedelis;

import com.github.nedelis.util.collections.Config;
import com.github.nedelis.vocabulary.token.IToken;
import com.github.nedelis.vocabulary.token.Token;
import com.github.nedelis.vocabulary.token.Tokens;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;

public final class Settings {

    private static final Properties settings = new Properties();

    private static void init() {

        var defaultTokens = new Properties();
        defaultTokens.putAll(new HashMap<String, IToken>() {{
            put("PREFIX_AND_POSTFIX_OF_SECTION_NAME", new Token('[', ']'));
            put("PREFIX_AND_POSTFIX_OF_VAR_NAME", new Token("__", "__"));
            put("PREFIX_AND_POSTFIX_OF_VAR_VALUE", new Token('(', ')'));
            put("ASSIGNMENT_OPERATOR", new Token('='));
            put("ENUMERATION_OPERATOR", new Token(','));
        }});
        settings.put("Tokens", defaultTokens);
    }

    public static void readSettings() {

        init();

        var propertiesFromFile = new Properties();
        try {
            propertiesFromFile.load(Files.newInputStream(Path.of("src/com/github/nedelis/util/data/reader.properties")));
        } catch (IOException e) {
            System.err.println("Properties file wasn't found!");
        }

        readTokens(propertiesFromFile);
    }

    private static void readTokens(@NotNull Properties properties) {
        for(var key : properties.keySet()) {
            if(key.toString().startsWith("tok.")) {
                var value = properties.getProperty(key.toString());
                var params = value.split(" ");
                key = key.toString().substring(key.toString().indexOf("tok.") + 4);
                if(params.length == 1) {
                    var tokens = settings.get("Tokens");
                    if(tokens instanceof Properties) {
                        ((Properties) tokens).put(key, new Token(params[0]));
                    }
                    settings.put("Tokens", tokens);
                } else if(params.length == 2) {
                    var tokens = settings.get("Tokens");
                    if(tokens instanceof Properties) {
                        ((Properties) tokens).put(key, new Token(params[0], params[1]));
                    }
                    settings.put("Tokens", tokens);
                }
            }
        }
    }

    public static @NotNull IToken getTokenByName(@NotNull String name) {
        var tokens = settings.get("Tokens");
        if(tokens instanceof Properties) {
            return (IToken) ((Properties) tokens).get(name);
        }
        return Tokens.EMPTY_TOKEN;
    }

    public static void setSetting(@NotNull String settingGroupPrefix, @NotNull String settingName, @NotNull String value) {

    }

}
