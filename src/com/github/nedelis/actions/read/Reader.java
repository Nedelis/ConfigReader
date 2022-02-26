package com.github.nedelis.actions.read;

import com.github.nedelis.vocabulary.token.Tokens;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

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

    public static @NotNull Set<String> readModificationsLine(@NotNull String line) {
        var modifications = new HashSet<java.lang.String>();
        line = line.replaceAll(" ", "");
        if(line.startsWith("modifications:")) {
            modifications = new HashSet<>();
            line = line.replaceFirst("modifications:", "");
            var stack = new StringBuilder();
            for(var ch : line.toCharArray()) {
                if(java.lang.String.valueOf(ch).equals(java.lang.String.valueOf(Tokens.ENOP.getToken().token()))) {
                    modifications.add(stack.toString());
                    stack = new StringBuilder();
                } else {
                    stack.append(ch);
                }
            }
            modifications.add(stack.toString());
        }
        return modifications;
    }

    public static @NotNull String readFileExtension(@NotNull File configFile) {
        var fileName = configFile.getName();
        var i = fileName.lastIndexOf('.');
        return i > 0 ? fileName.substring(i+1) : "";
    }

}
