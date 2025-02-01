package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import javax.annotation.Nullable;

@HandleBy(reference = "net/minecraft/core/component/DataComponentMap", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefDataComponentMap {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    @Nullable
    <T> T get(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;has(Lnet/minecraft/core/component/DataComponentType;)Z", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default boolean has(RefDataComponentType<?> type) {
        return false;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default <T> T getOrDefault(RefDataComponentType<? extends T> type, T fallback) {
        return null;
    }

}
