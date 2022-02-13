package com.github.nedelis.vocabulary.keyword;

import org.jetbrains.annotations.NotNull;

public interface IFieldType<T> extends IKeyword {

    T fromString(@NotNull String s);

}
