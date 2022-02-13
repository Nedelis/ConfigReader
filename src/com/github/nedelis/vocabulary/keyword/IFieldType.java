package com.github.nedelis.vocabulary.keyword;

public interface IFieldType extends IKeyword {

    default Class<?> fieldType() {
        return String.class;
    }

}
