package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/util/random/WeightedEntry$Wrapper", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefWrapper<T> implements RefWeightedEntry {
    @HandleBy(reference = "Lnet/minecraft/util/random/WeightedEntry$Wrapper;<init>(Ljava/lang/Object;Lnet/minecraft/util/random/Weight;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native <T> RefWrapper<T> newInstance(T data, RefWeight weight);

    @HandleBy(reference = "Lnet/minecraft/util/random/WeightedEntry$Wrapper;getData()Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native T getData();

    @HandleBy(reference = "Lnet/minecraft/util/random/WeightedEntry$Wrapper;getWeight()Lnet/minecraft/util/random/Weight;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefWeight getWeight();
}
