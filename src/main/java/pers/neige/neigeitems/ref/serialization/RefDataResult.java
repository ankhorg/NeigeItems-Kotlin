package pers.neige.neigeitems.ref.serialization;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "com/mojang/serialization/DataResult", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefDataResult<R> {
    @HandleBy(reference = "Lcom/mojang/serialization/DataResult;getOrThrow()Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default R getOrThrow() {
        return null;
    }
}
