package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@HandleBy(reference = "net/minecraft/core/component/DataComponentHolder", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public class RefDataComponentHolder {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentHolder;getComponents()Lnet/minecraft/core/component/DataComponentMap;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentMap getComponents();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentHolder;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public native @Nullable <T> T get(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentHolder;getAllOfType(Ljava/lang/Class;)Ljava/util/stream/Stream;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> Stream<T> getAllOfType(Class<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentHolder;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> T getOrDefault(RefDataComponentType<? extends T> type, T defaultValue);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentHolder;has(Lnet/minecraft/core/component/DataComponentType;)Z", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public native boolean has(RefDataComponentType<?> type);
}
