package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "net/minecraft/core/component/DataComponentGetter", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
public interface RefDataComponentGetter {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentGetter;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    <T> @Nullable T get(RefDataComponentType<? extends T> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentGetter;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    default <T> T getOrDefault(RefDataComponentType<? extends T> component, T defaultValue) {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentGetter;getTyped(Lnet/minecraft/core/component/DataComponentType;)Lnet/minecraft/core/component/TypedDataComponent;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    default <T> @Nullable RefTypedDataComponent<T> getTyped(RefDataComponentType<T> component) {
        return null;
    }
}
