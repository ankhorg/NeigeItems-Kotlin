package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.serialization.RefDataResult;
import pers.neige.neigeitems.ref.serialization.RefDynamicOps;

@HandleBy(reference = "net/minecraft/core/component/TypedDataComponent", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefTypedDataComponent<T> {
    @HandleBy(reference = "Lnet/minecraft/core/component/TypedDataComponent;createUnchecked(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/TypedDataComponent;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native static <T> RefTypedDataComponent<T> createUnchecked(RefDataComponentType<T> type, Object value);

    @HandleBy(reference = "Lnet/minecraft/core/component/TypedDataComponent;type()Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentType<T> type();

    @HandleBy(reference = "Lnet/minecraft/core/component/TypedDataComponent;value()Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native T value();

    @HandleBy(reference = "Lnet/minecraft/core/component/TypedDataComponent;encodeValue(Lcom/mojang/serialization/DynamicOps;)Lcom/mojang/serialization/DataResult;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <D> RefDataResult<D> encodeValue(RefDynamicOps<D> ops);
}
