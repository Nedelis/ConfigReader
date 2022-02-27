package com.github.nedelis.util.collections;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@SuppressWarnings("unused")
public final class Config<FV> {

    private final Map<String, Map<String, FV>> config = new HashMap<>();



    public static @NotNull Config<Object> fromMap(@NotNull Map<String, Map<String, Object>> map) {
        var config = new Config<>();
        config.putAll(map);
        return config;
    }



    public @NotNull FV getFieldValue(@NotNull String sectionName, @NotNull String fieldName) {
        return this.config.get(sectionName).get(fieldName);
    }

    public @NotNull FV getFieldValueOrDefault(@NotNull String sectionName, @NotNull String fieldName, @NotNull FV defaultObject) {
        return this.config.containsKey(sectionName) && this.config.get(sectionName).containsKey(fieldName) ? this.getFieldValue(sectionName, fieldName) : defaultObject;
    }

    @Contract("_ -> new")
    public @NotNull @Unmodifiable Collection<FV> fieldValues(@NotNull String sectionName) {
        return this.config.get(sectionName).values();
    }

    public @NotNull Set<String> fieldNamesSet(@NotNull String sectionName) {
        return this.config.get(sectionName).keySet();
    }

    public @NotNull Map<String, FV> getSectionFields(@NotNull String sectionName) {
        return this.config.get(sectionName);
    }

    public @NotNull Set<String> sectionSet() {
        return config.keySet();
    }



    public void putAll(@NotNull Map<String, Map<String, FV>> map) {
        this.config.putAll(map);
    }

    public void addSection(@NotNull String sectionName) {
        if(!this.config.containsKey(sectionName)) config.put(sectionName, new HashMap<>());
    }

    public void putToSection(@NotNull String sectionName, @NotNull Map<String, FV> fieldsToPut) {
        if(!this.config.containsKey(sectionName)) this.addSection(sectionName);
        this.config.get(sectionName).putAll(fieldsToPut);
    }

    public void addField(@NotNull String sectionName, @NotNull String fieldName, @NotNull FV value) {
        if(!this.containsSection(sectionName)) this.addSection(sectionName);
        if(!this.containsField(sectionName, fieldName)) this.setFieldValue(sectionName, fieldName, value);
    }

    public void setFieldValue(@NotNull String sectionName, @NotNull String fieldName, @NotNull FV newValue) {
        if(!this.containsSection(sectionName)) this.addSection(sectionName);
        this.getSectionFields(sectionName).put(fieldName, newValue);
    }



    public void removeSection(@NotNull String sectionName) {
        this.config.remove(sectionName);
    }

    public void removeField(@NotNull String sectionName, @NotNull String fieldName) {
        if(this.containsSection(sectionName)) this.getSectionFields(sectionName).remove(fieldName);
    }

    public void clearConfig() {
        this.config.clear();
    }

    public void clearSection(@NotNull String sectionName) {
        this.getSectionFields(sectionName).clear();
    }



    public boolean containsSection(@NotNull String sectionName) {
        return this.config.containsKey(sectionName);
    }

    public boolean containsField(@NotNull String sectionName, @NotNull String fieldName) {
        return this.containsSection(sectionName) && this.getSectionFields(sectionName).containsKey(fieldName);
    }



    public @NotNull @Unmodifiable Map<String, Map<String, FV>> toMap() {
        return Map.copyOf(this.config);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Config.class.getSimpleName() + "[", "]")
                .add("" + this.config)
                .toString();
    }

}
