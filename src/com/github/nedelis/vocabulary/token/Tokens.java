package com.github.nedelis.vocabulary.token;

import com.github.nedelis.Settings;
import org.jetbrains.annotations.NotNull;

public enum Tokens {
    PREFIX_AND_POSTFIX_OF_SECTION_NAME(Settings.getTokenByName("PREFIX_AND_POSTFIX_OF_SECTION_NAME")), PPSN(Settings.getTokenByName("PREFIX_AND_POSTFIX_OF_SECTION_NAME")),
    PREFIX_AND_POSTFIX_OF_VAR_NAME(Settings.getTokenByName("PREFIX_AND_POSTFIX_OF_VAR_NAME")), PPVN(Settings.getTokenByName("PREFIX_AND_POSTFIX_OF_VAR_NAME")),
    PREFIX_AND_POSTFIX_OF_VAR_VALUE(Settings.getTokenByName("PREFIX_AND_POSTFIX_OF_VAR_VALUE")), PPVV(Settings.getTokenByName("PREFIX_AND_POSTFIX_OF_VAR_VALUE")),
    ASSIGNMENT_OPERATOR(Settings.getTokenByName("ASSIGNMENT_OPERATOR")), ASOP(Settings.getTokenByName("ASSIGNMENT_OPERATOR")),
    ENUMERATION_OPERATOR(Settings.getTokenByName("ENUMERATION_OPERATOR")), ENOP(Settings.getTokenByName("ENUMERATION_OPERATOR"));

    public static final IToken EMPTY_TOKEN = new Token("", "");

    private final IToken token;

    Tokens(@NotNull IToken token) {
        this.token = token;
    }

    public @NotNull IToken getToken() {
        return this.token;
    }

    public @NotNull String getTokenAsString() {
        return String.valueOf(this.token.token());
    }

    public @NotNull String getClosingAsString() {
        return this.token.hasClosing() ? String.valueOf(this.token.closing().token()) : "";
    }

    public static IToken getAsToken(@NotNull Object token) {
        for(var existingToken : Tokens.values()) {
            if(existingToken.getToken().token().toString().equals(token.toString())) {
                return existingToken.getToken();
            }
        }
        return EMPTY_TOKEN;
    }

    public static boolean isToken(@NotNull IToken token) {
        return !getAsToken(token.token()).equals(EMPTY_TOKEN);
    }

}
