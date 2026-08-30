package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@HandleBy(reference = "net/minecraft/core/component/DataComponentMap", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefDataComponentMap extends Iterable<RefTypedDataComponent<?>> {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;EMPTY:Lnet/minecraft/core/component/DataComponentMap;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    RefDataComponentMap EMPTY = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;composite(Lnet/minecraft/core/component/DataComponentMap;Lnet/minecraft/core/component/DataComponentMap;)Lnet/minecraft/core/component/DataComponentMap;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    static RefDataComponentMap composite(RefDataComponentMap base, RefDataComponentMap overrides) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;builder()Lnet/minecraft/core/component/DataComponentMap$Builder;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    static RefDataComponentMap$Builder builder() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    <T> @Nullable T get(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;keySet()Ljava/util/Set;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default Set<RefDataComponentType<?>> keySet() {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;has(Lnet/minecraft/core/component/DataComponentType;)Z", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default boolean has(RefDataComponentType<?> type) {
        return false;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default <T> T getOrDefault(RefDataComponentType<? extends T> type, T fallback) {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;getTyped(Lnet/minecraft/core/component/DataComponentType;)Lnet/minecraft/core/component/TypedDataComponent;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default <T> @Nullable RefTypedDataComponent<T> getTyped(RefDataComponentType<T> type) {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;iterator()Ljava/util/Iterator;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    @Override
    default Iterator<RefTypedDataComponent<?>> iterator() {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;stream()Ljava/util/stream/Stream;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default Stream<RefTypedDataComponent<?>> stream() {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;size()I", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default int size() {
        return 0;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;isEmpty()Z", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default boolean isEmpty() {
        return true;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;filter(Ljava/util/function/Predicate;)Lnet/minecraft/core/component/DataComponentMap;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default RefDataComponentMap filter(Predicate<RefDataComponentType<?>> predicate) {
        return null;
    }
}
