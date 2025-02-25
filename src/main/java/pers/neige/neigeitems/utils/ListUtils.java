package pers.neige.neigeitems.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ListUtils {
    @Nullable
    public static <T> T getOrNull(
            @NotNull List<T> list,
            int index
    ) {
        return index >= 0 && index <= (list.size() - 1) ? list.get(index) : null;
    }

    @NotNull
    public static <T> T getOrDefault(
            @NotNull List<T> list,
            int index,
            @NotNull T def
    ) {
        T value = null;
        if (index >= 0 && index <= (list.size() - 1)) {
            value = list.get(index);
        }
        return value != null ? value : def;
    }

    @NotNull
    public static <T, V> V getAndApply(
            @NotNull List<T> list,
            int index,
            @NotNull V def,
            @NotNull Function<T, V> handler
    ) {
        T value = null;
        if (index >= 0 && index <= (list.size() - 1)) {
            value = list.get(index);
        }
        if (value != null) {
            V result = handler.apply(value);
            if (result != null) {
                return result;
            }
        }
        return def;
    }

    @Nullable
    public static <T> T getOrNull(
            T @NotNull [] list,
            int index
    ) {
        return index >= 0 && index <= (list.length - 1) ? list[index] : null;
    }

    @NotNull
    public static <T> T getOrDefault(
            T @NotNull [] list,
            int index,
            @NotNull T def
    ) {
        T value = null;
        if (index >= 0 && index <= (list.length - 1)) {
            value = list[index];
        }
        return value != null ? value : def;
    }

    @NotNull
    public static <T, V> V getAndApply(
            T @NotNull [] list,
            int index,
            @NotNull V def,
            @NotNull Function<T, V> handler
    ) {
        T value = null;
        if (index >= 0 && index <= (list.length - 1)) {
            value = list[index];
        }
        if (value != null) {
            V result = handler.apply(value);
            if (result != null) {
                return result;
            }
        }
        return def;
    }
}
