package com.github.nedelis.vocabulary.token;

import org.jetbrains.annotations.NotNull;

public enum Tokens {
    ROUND_BRACKETS(new Token('(', ')')),
    SQUARE_BRACKETS(new Token('[', ']')),
    DOUBLE_UNDERSCORE(new Token("__", "__")),
    EQUALS(new Token('=')),
    COMMA(new Token(','));

    private final Token token;

    Tokens(@NotNull Token token) {
        this.token = token;
    }

    public @NotNull Token getToken() {
        return this.token;
    }

    public @NotNull String getTokenAsString() {
        return String.valueOf(this.token.token());
    }

    public @NotNull String getClosingAsString() {
        return this.token.hasClosing() ? String.valueOf(this.token.closing().token()) : "";
    }

    public static Token getAsToken(@NotNull Object token) {
        for(var existingToken : Tokens.values()) {
            if(existingToken.getToken().token().toString().equals(token.toString())) {
                return existingToken.getToken();
            }
        }
        return Token.EMPTY_TOKEN;
    }

    public static boolean isToken(@NotNull Token token) {
        return !getAsToken(token.token()).equals(Token.EMPTY_TOKEN);
    }

}
