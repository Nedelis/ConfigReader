package com.github.nedelis.vocabulary.keyword;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class Keywords {

    private static final Set<IKeyword> keywords = new HashSet<>() {{
        add(() -> "modifications");
    }};


    public static @NotNull IKeyword getKeyword(@NotNull String keyword) {
        for(var existingKeyword : keywords) {
            if(existingKeyword.keyword().equals(keyword)) return existingKeyword;
        }
        return () -> "";
    }
}