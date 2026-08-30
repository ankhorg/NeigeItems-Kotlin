package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Set;

@HandleBy(reference = "net/minecraft/core/component/DataComponentPatch$SplitResult", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefDataComponentPatch$SplitResult {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$SplitResult;EMPTY:Lnet/minecraft/core/component/DataComponentPatch$SplitResult;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentPatch$SplitResult EMPTY = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$SplitResult;added()Lnet/minecraft/core/component/DataComponentMap;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentMap added();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch$SplitResult;removed()Ljava/util/Set;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native Set<RefDataComponentType<?>> removed();
}
