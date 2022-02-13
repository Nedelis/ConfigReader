package com.github.nedelis.actions.read;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public final class Reader {

    public static Page readConfigFile(@NotNull File configFile) {
        try (var input = Files.newInputStream(configFile.toPath())) {
            var page = Page.EMPTY_PAGE;
            var reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while((line = reader.readLine()) != null) page.addLine(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Page.EMPTY_PAGE;
    }

    public static @NotNull String readFileExtension(@NotNull File configFile) {
        var fileName = configFile.getName();
        var i = fileName.lastIndexOf('.');
        return i > 0 ? fileName.substring(i+1) : "";
    }

}
