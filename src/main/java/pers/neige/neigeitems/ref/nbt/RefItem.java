package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/item/Item", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Item", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefItem {
    @HandleBy(reference = "Lnet/minecraft/world/item/Item;getDescriptionId()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Item;getName()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native String getDescriptionId();

    @HandleBy(reference = "Lnet/minecraft/world/item/Item;getDescriptionId(Lnet/minecraft/world/item/ItemStack;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Item;f(Lnet/minecraft/server/v1_14_R1/ItemStack;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_14_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Item;h(Lnet/minecraft/server/v1_13_R1/ItemStack;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_13_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Item;a(Lnet/minecraft/server/v1_12_R1/ItemStack;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native String getDescriptionId(RefNmsItemStack itemStack);
}
