package pers.neige.neigeitems.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Getter
@ToString
@AllArgsConstructor
public final class ContextKey<T> {
    private final boolean putInGlobal;
    private final @NonNull Collection<String> names;

    public ContextKey(@NonNull String... names) {
        this(false, names);
    }

    public ContextKey(boolean putInGlobal, @NonNull String... names) {
        this.putInGlobal = putInGlobal;
        this.names = new ArrayList<>();
        this.names.addAll(Arrays.asList(names));
    }
}
