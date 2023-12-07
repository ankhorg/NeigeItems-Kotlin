package pers.neige.neigeitems.internal.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/util/random/Weight", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefWeight {
    @HandleBy(reference = "Lnet/minecraft/util/random/Weight;of(I)Lnet/minecraft/util/random/Weight;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native RefWeight of(int weight);

    @HandleBy(reference = "Lnet/minecraft/util/random/Weight;asInt()I", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native int asInt();
}
