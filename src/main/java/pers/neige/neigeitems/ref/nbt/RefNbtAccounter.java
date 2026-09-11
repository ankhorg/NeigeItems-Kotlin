package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/NbtAccounter", predicates = "craftbukkit_version:[v26_1,)")
public final class RefNbtAccounter {
    @HandleBy(reference = "Lnet/minecraft/nbt/NbtAccounter;unlimitedHeap()Lnet/minecraft/nbt/NbtAccounter;", predicates = "craftbukkit_version:[v26_1,)")
    public static native RefNbtAccounter unlimitedHeap();
}
