package com.github.nedelis.vocabulary.token;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public interface IToken {

    @Unmodifiable @NotNull Object token();

    default @NotNull IToken closing() {
        return Tokens.EMPTY_TOKEN;
    }

    default boolean hasClosing() {
        return !Tokens.EMPTY_TOKEN.equals(this.closing());
    }

}
