package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "it/unimi/dsi/fastutil/ints/Int2ObjectMap", predicates = "craftbukkit_version:[v1_18_R1,)")
@HandleBy(reference = "org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/ints/Int2ObjectMap", predicates = "craftbukkit_version:[v1_14_R1,v1_18_R1)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/IntHashMap", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
public class RefIntHashMap<V> {
    @HandleBy(reference = "Lit/unimi/dsi/fastutil/ints/Int2ObjectFunction;get(I)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_18_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/ints/Int2ObjectFunction;get(I)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_14_R1,v1_18_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/IntHashMap;get(I)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native V get(int var1);
}
