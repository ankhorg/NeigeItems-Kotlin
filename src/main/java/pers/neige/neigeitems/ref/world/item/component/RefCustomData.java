package pers.neige.neigeitems.ref.world.item.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;

@HandleBy(reference = "net/minecraft/world/item/component/CustomData", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefCustomData {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomData;of(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/item/component/CustomData;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static native RefCustomData of(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomData;getUnsafe()Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefNbtTagCompound getUnsafe();
}
