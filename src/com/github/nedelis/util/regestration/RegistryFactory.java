package com.github.nedelis.util.regestration;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class RegistryFactory<K extends IRegistryGroup, V extends Set<Registrable>> implements Registrable {

    private final Registrable DEFAULT_REGISTERED_OBJECT;
    private final Map<K, V> entry = new HashMap<>();

    public RegistryFactory(@NotNull Registrable defaultRegisteredObject) {
        this.DEFAULT_REGISTERED_OBJECT = defaultRegisteredObject;
    }

    public final @NotNull Registrable getIfRegistered(@NotNull K group, @NotNull String[] names) {
        for(Registrable registeredValue : this.getRegisteredObjectsInGroup(group)) {
            for(int i = 0; i < registeredValue.names().length; ++i) {
                if(registeredValue.names()[i].equals(names[i])) {
                    return registeredValue;
                }
            }
        }
        return this.getDefaultRegisteredObject();
    }

    public final @NotNull V getRegisteredObjectsInGroup(@NotNull K group) {
        return this.entry.get(group);
    }

    public final @NotNull Registrable getDefaultRegisteredObject() {
        return this.DEFAULT_REGISTERED_OBJECT;
    }

    @SuppressWarnings("unchecked")
    public void register(@NotNull K group, @NotNull Registrable objectToRegister) {
        if(!this.entry.containsKey(group)) this.entry.put(group, (V) new HashSet<Registrable>());
        this.entry.get(group).add(objectToRegister);
    }
}
