package com.github.nedelis.actions.parse;

import com.github.nedelis.actions.read.Page;
import com.github.nedelis.vocabulary.token.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Parser implements IParser {

    private final ArrayList<String> PARSING_RESULT = new ArrayList<>();

    public Parser() {
    }

    private void init() {
        this.PARSING_RESULT.clear();
    }

    @Override
    public void parse(@NotNull Page page, @NotNull Set<String> modifications) {
        this.init();

        var hasSections = !modifications.contains("NSS");
        var hasFieldTypes = !modifications.contains("NFT");

        for(var line : page.toStringArray()) {
            if(hasSections && line.startsWith(Tokens.SQUARE_BRACKETS.getTokenAsString()) && line.endsWith(Tokens.SQUARE_BRACKETS.getClosingAsString())) {
                PARSING_RESULT.add(line);
                continue;
            }

            var fieldDeterminants = line.substring(0, line.indexOf(Tokens.EQUALS.getTokenAsString()));
            var fieldValue = line.substring(line.indexOf(Tokens.EQUALS.getTokenAsString()) + Tokens.EQUALS.getTokenAsString().length() + " ".length());
            var index = line.indexOf(Tokens.DOUBLE_UNDERSCORE.getTokenAsString());

            if(hasFieldTypes && index > 0) {
                PARSING_RESULT.add(fieldDeterminants.substring(0, index).replaceAll(" ", ""));
                fieldDeterminants = fieldDeterminants.substring(index);
            }

            if(fieldDeterminants.startsWith(Tokens.DOUBLE_UNDERSCORE.getTokenAsString())) {
                PARSING_RESULT.add(fieldDeterminants.substring(0, fieldDeterminants.lastIndexOf(Tokens.DOUBLE_UNDERSCORE.getClosingAsString()) + Tokens.DOUBLE_UNDERSCORE.getClosingAsString().length()));
            }

            PARSING_RESULT.add(fieldValue.substring(fieldValue.indexOf(Tokens.ROUND_BRACKETS.getTokenAsString()), fieldValue.lastIndexOf(Tokens.ROUND_BRACKETS.getClosingAsString()) + Tokens.ROUND_BRACKETS.getClosingAsString().length()));
        }
    }

    @Override
    public @NotNull String parserModificationName() {
        return "DCP";
    }

    public @NotNull ArrayList<String> getParsingResult() {
        return PARSING_RESULT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parser parser = (Parser) o;
        return Objects.equals(this.parserModificationName(), parser.parserModificationName()) && Objects.equals(this.fileExtension(), parser.fileExtension());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parserModificationName(), this.fileExtension());
    }
}
