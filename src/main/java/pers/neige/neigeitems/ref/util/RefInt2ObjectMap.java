package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap", predicates = "craftbukkit_version:[v1_18_R1,)")
@HandleBy(reference = "org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap", predicates = "craftbukkit_version:[v1_14_R1,v1_18_R1)")
public class RefInt2ObjectMap<V> {
    @HandleBy(reference = "Lit/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap;get(I)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_18_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap;get(I)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_14_R1,v1_18_R1)")
    public native V get(int key);
}
