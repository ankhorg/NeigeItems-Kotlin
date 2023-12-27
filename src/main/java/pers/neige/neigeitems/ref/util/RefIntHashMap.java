package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/IntHashMap", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
public class RefIntHashMap<V> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/IntHashMap;get(I)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native V get(int key);
}
