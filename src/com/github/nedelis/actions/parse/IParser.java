package com.github.nedelis.actions.parse;

import com.github.nedelis.actions.read.Page;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;

public interface IParser {

    void parse(@NotNull Page page, @NotNull Set<String> modifications);

    @NotNull ArrayList<String> getParsingResult();

    @NotNull String parserModificationName();

    default @NotNull String fileExtension() {
        return "ncf";
    }

}
