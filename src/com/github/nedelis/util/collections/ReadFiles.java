package com.github.nedelis.util.collections;

import java.util.HashMap;
import java.util.Map;

public final class ReadFiles {

    private static final Map<String, Config<Object>> readFiles = new HashMap<>();

    public static Map<String, Config<Object>> getReadFiles() {
        return readFiles;
    }

}
