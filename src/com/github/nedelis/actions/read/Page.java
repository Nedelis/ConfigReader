package com.github.nedelis.actions.read;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Page(@NotNull List<String> page) {

    public static final Page EMPTY_PAGE = new Page(new ArrayList<>());

    public void addLine(@NotNull String lineToAdd, int lineNum) {
        this.page.add(lineNum, lineToAdd);
    }

    public void addLine(@NotNull String lineToAdd) {
        this.page.add(lineToAdd);
    }

    public void setLine(@NotNull String newLine, int lineNum) {
        this.page.set(lineNum, newLine);
    }

    public String getLine(int lineNum) {
        return this.page.get(lineNum);
    }

    public char getCharAt(int lineNum, int charIndex) {
        return this.page.get(lineNum).charAt(charIndex);
    }

    public void removeLine(int lineNum) {
        this.page.remove(lineNum);
    }

    public char @NotNull [] toCharArray() {
        var builder = new StringBuilder();
        for (var line : this.page) {
            builder.append(line);
        }
        return builder.toString().toCharArray();
    }
}
