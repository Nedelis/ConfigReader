package com.github.nedelis.vocabulary.keyword;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class FieldTypes {

    private static final Set<IFieldType> fieldTypes = new HashSet<>() {{
        add(new FieldType(Byte.class, "byte"));
        add(new FieldType(Short.class, "short"));
        add(new FieldType(Integer.class, "int"));
        add(new FieldType(Long.class, "long"));
        add(new FieldType(Float.class, "float"));
        add(new FieldType(Double.class, "double"));
        add(new FieldType(Character.class, "char"));
        add(new FieldType(String.class, "str"));
        add(new FieldType(Boolean.class, "bool"));
    }};

    public static @NotNull IFieldType getFieldTypeByKeyword(@NotNull String keyword) {
        for(var existingFieldType : fieldTypes) {
            if(existingFieldType.keyword().equals(keyword)) return existingFieldType;
        }
        return FieldType.DEFAULT_FIELD_TYPE;
    }

    public static boolean isFieldType(@NotNull String keyword) {
        return !getFieldTypeByKeyword(keyword).equals(FieldType.DEFAULT_FIELD_TYPE);
    }

    public static class ModificationUnit {

        public static void addFieldType(@NotNull FieldType fieldTypeToAdd) {
            fieldTypes.add(fieldTypeToAdd);
        }

    }

}
