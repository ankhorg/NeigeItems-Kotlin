package pers.neige.neigeitems.utils;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ListUtils {
    public static @Nullable <T> T getOrNull(
            @NonNull List<T> list,
            int index
    ) {
        return index >= 0 && index <= (list.size() - 1) ? list.get(index) : null;
    }

    public static @NonNull <T> T getOrDefault(
            @NonNull List<T> list,
            int index,
            @NonNull T def
    ) {
        T value = null;
        if (index >= 0 && index <= (list.size() - 1)) {
            value = list.get(index);
        }
        return value != null ? value : def;
    }

    public static @NonNull <T, V> V getAndApply(
            @NonNull List<T> list,
            int index,
            @NonNull V def,
            @NonNull Function<T, V> handler
    ) {
        T value = null;
        if (index >= 0 && index <= (list.size() - 1)) {
            value = list.get(index);
        }
        if (value != null) {
            val result = handler.apply(value);
            if (result != null) {
                return result;
            }
        }
        return def;
    }

    public static @Nullable <T> T getOrNull(
            T @NonNull [] list,
            int index
    ) {
        return index >= 0 && index <= (list.length - 1) ? list[index] : null;
    }

    public static @NonNull <T> T getOrDefault(
            T @NonNull [] list,
            int index,
            @NonNull T def
    ) {
        T value = null;
        if (index >= 0 && index <= (list.length - 1)) {
            value = list[index];
        }
        return value != null ? value : def;
    }

    public static @NonNull <T, V> V getAndApply(
            T @NonNull [] list,
            int index,
            @NonNull V def,
            @NonNull Function<T, V> handler
    ) {
        T value = null;
        if (index >= 0 && index <= (list.length - 1)) {
            value = list[index];
        }
        if (value != null) {
            val result = handler.apply(value);
            if (result != null) {
                return result;
            }
        }
        return def;
    }
}
