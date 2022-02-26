package com.github.nedelis.vocabulary.token;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Objects;

public record Token(@NotNull Object token, IToken closing) implements IToken {

    public Token(@NotNull Object token) {
        this(token, null);
    }

    public Token(@NotNull Object token, @NotNull Object closing) {
        this(token, new Token(closing));
    }

    @Contract(pure = true)
    @Override
    public @Unmodifiable @NotNull Object token() {
        return String.valueOf(this.token);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return token.equals(token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
