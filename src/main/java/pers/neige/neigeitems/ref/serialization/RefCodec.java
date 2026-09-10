package pers.neige.neigeitems.ref.serialization;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "com/mojang/serialization/Codec", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefCodec<A> {
    @HandleBy(reference = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    <T> RefDataResult<A> parse(RefDynamicOps<T> ops, T input);
}
