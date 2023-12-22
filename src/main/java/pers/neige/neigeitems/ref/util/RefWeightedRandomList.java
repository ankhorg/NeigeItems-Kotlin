package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/util/random/WeightedRandomList", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefWeightedRandomList<E extends RefWeightedEntry> {
    @HandleBy(reference = "Lnet/minecraft/util/random/WeightedRandomList;create(Ljava/util/List;)Lnet/minecraft/util/random/WeightedRandomList;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native <E extends RefWeightedEntry> RefWeightedRandomList<E> create(List<E> entries);

    @HandleBy(reference = "Lnet/minecraft/util/random/WeightedRandomList;unwrap()Ljava/util/List;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native List<E> unwrap();
}
