package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/util/random/SimpleWeightedRandomList", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefSimpleWeightedRandomList<E> extends RefWeightedRandomList<RefWrapper<E>> {
    @HandleBy(reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList;<init>(Ljava/util/List;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native <E> RefSimpleWeightedRandomList<E> newInstance(List<? extends RefWrapper<E>> entries);

    @HandleBy(reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList;builder()Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native <E> RefBuilder<E> builder();
}
