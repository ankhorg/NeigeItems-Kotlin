package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "net/minecraft/core/component/DataComponentMap$Builder", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefDataComponentMap$Builder {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap$Builder;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentMap$Builder;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> RefDataComponentMap$Builder set(RefDataComponentType<T> component, @Nullable T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap$Builder;addAll(Lnet/minecraft/core/component/DataComponentMap;)Lnet/minecraft/core/component/DataComponentMap$Builder;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentMap$Builder addAll(RefDataComponentMap components);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentMap$Builder;build()Lnet/minecraft/core/component/DataComponentMap;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentMap build();
}
