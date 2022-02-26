package com.github.nedelis.util.regestration;

import org.jetbrains.annotations.NotNull;

public final class RegistryGroups {

    public static class Parsers implements IRegistryGroup {
        @Override
        public @NotNull String name() {
            return "Parsers";
        }
    }

    public static class Translators implements IRegistryGroup {
        @Override
        public @NotNull String name() {
            return "Translators";
        }
    }

    public static class Factories implements IRegistryGroup {
        @Override
        public @NotNull String name() {
            return "Factories";
        }
    }

}
