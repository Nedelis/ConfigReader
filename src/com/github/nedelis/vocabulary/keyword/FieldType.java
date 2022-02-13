package com.github.nedelis.vocabulary.keyword;

import org.jetbrains.annotations.NotNull;

public abstract class FieldType<T> implements IFieldType<T> {

    public static final IFieldType<?> DEFAULT_FIELD_TYPE = new FieldType<>() {
        @Override
        public String fromString(@NotNull String s) {
            return s;
        }
    };

    private final String keyword;

    public FieldType() {
        this("");
    }

    public FieldType(@NotNull String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String keyword() {
        return this.keyword;
    }
}
