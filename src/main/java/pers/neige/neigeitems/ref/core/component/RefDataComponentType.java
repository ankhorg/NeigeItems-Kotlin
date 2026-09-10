package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.serialization.RefCodec;

@HandleBy(reference = "net/minecraft/core/component/DataComponentType", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefDataComponentType<T> {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentType;codecOrThrow()Lcom/mojang/serialization/Codec;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    RefCodec<T> codecOrThrow();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentType;ignoreSwapAnimation()Z", isInterface = true, predicates = "craftbukkit_version:[v1_21_R7,)")
    boolean ignoreSwapAnimation();
}
