package com.github.nedelis.util.annotations.parser;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AParsingMethod {

    @NotNull String parserModificationName();

    @NotNull String fileExtension() default "ncf";

}
