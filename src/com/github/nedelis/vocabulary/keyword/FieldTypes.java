package com.github.nedelis.vocabulary.keyword;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class FieldTypes {

    private static final Set<IFieldType<?>> fieldTypes = new HashSet<>() {{
        add(new FieldType<Byte>("byte") {
            @Override
            public Byte fromString(@NotNull String s) {
                return Byte.parseByte(s);
            }
        });
        add(new FieldType<Short>("short") {
            @Override
            public Short fromString(@NotNull String s) {
                return Short.parseShort(s);
            }
        });
        add(new FieldType<Integer>("int") {
            @Override
            public Integer fromString(@NotNull String s) {
                return Integer.parseInt(s);
            }
        });
        add(new FieldType<Long>("long") {
            @Override
            public Long fromString(@NotNull String s) {
                return Long.parseLong(s);
            }
        });
        add(new FieldType<Float>("float") {
            @Override
            public Float fromString(@NotNull String s) {
                return Float.parseFloat(s);
            }
        });
        add(new FieldType<Double>("double") {
            @Override
            public Double fromString(@NotNull String s) {
                return Double.parseDouble(s);
            }
        });
        add(new FieldType<Character>("char") {
            @Override
            public Character fromString(@NotNull String s) {
                return s.charAt(0);
            }
        });
        add(new FieldType<String>("str") {
            @Override
            public String fromString(@NotNull String s) {
                return s;
            }
        });
        add(new FieldType<Boolean>("bool") {
            @Override
            public Boolean fromString(@NotNull String s) {
                return Boolean.parseBoolean(s);
            }
        });
    }};

    public static @NotNull IFieldType<?> getFieldTypeByKeyword(@NotNull String keyword) {
        for(var existingFieldType : fieldTypes) {
            if(existingFieldType.keyword().equals(keyword)) return existingFieldType;
        }
        return FieldType.DEFAULT_FIELD_TYPE;
    }

    public static boolean isFieldType(@NotNull String keyword) {
        return !getFieldTypeByKeyword(keyword).equals(FieldType.DEFAULT_FIELD_TYPE);
    }

    public static class ModificationUnit {

        public static void addFieldType(@NotNull IFieldType<?> fieldTypeToAdd) {
            fieldTypes.add(fieldTypeToAdd);
        }

    }

}
