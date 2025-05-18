package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.serialization.RefDynamicOps;

@HandleBy(reference = "net/minecraft/nbt/NbtOps", predicates = "craftbukkit_version:[v1_20_R4,)")
public class RefNbtOps implements RefDynamicOps<RefNbtBase> {
    @HandleBy(reference = "Lnet/minecraft/nbt/NbtOps;INSTANCE:Lnet/minecraft/nbt/NbtOps;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefNbtOps INSTANCE = null;
}
