package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/util/random/SimpleWeightedRandomList$Builder", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefBuilder<E> {
    @HandleBy(reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;<init>()V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefBuilder() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;add(Ljava/lang/Object;I)Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefBuilder<E> add(E object, int weight);

    @HandleBy(reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;build()Lnet/minecraft/util/random/SimpleWeightedRandomList;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefSimpleWeightedRandomList<E> build();
}
