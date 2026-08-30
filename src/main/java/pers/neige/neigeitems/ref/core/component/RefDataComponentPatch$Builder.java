package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/component/DataComponentPatch$Builder", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefDataComponentPatch$Builder {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentPatch$Builder;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> RefDataComponentPatch$Builder set(RefDataComponentType<T> component, T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;remove(Lnet/minecraft/core/component/DataComponentType;)Lnet/minecraft/core/component/DataComponentPatch$Builder;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> RefDataComponentPatch$Builder remove(RefDataComponentType<T> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;set(Lnet/minecraft/core/component/TypedDataComponent;)Lnet/minecraft/core/component/DataComponentPatch$Builder;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> RefDataComponentPatch$Builder set(RefTypedDataComponent<T> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;copy(Lnet/minecraft/core/component/DataComponentPatch;)V", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native void copy(RefDataComponentPatch original);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;clear(Lnet/minecraft/core/component/DataComponentType;)V", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native void clear(RefDataComponentType<?> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;isSet(Lnet/minecraft/core/component/DataComponentType;)Z", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native boolean isSet(RefDataComponentType<?> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;isEmpty()Z", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native boolean isEmpty();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$Builder;build()Lnet/minecraft/core/component/DataComponentPatch;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentPatch build();
}
