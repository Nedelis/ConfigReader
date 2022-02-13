package com.github.nedelis.vocabulary.keyword;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record FieldType(@NotNull Class<?> fieldType, @NotNull String keyword) implements IFieldType {

    public static final IFieldType DEFAULT_FIELD_TYPE = new FieldType();

    public FieldType() {
        this("");
    }

    public FieldType(@NotNull String keyword) {
        this(String.class, keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldType fieldType1 = (FieldType) o;
        return fieldType.equals(fieldType1.fieldType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldType);
    }
}
