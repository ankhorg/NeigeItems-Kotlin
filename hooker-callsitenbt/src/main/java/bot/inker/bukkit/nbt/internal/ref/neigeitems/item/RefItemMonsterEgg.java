package pers.neige.neigeitems.internal.ref.item;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.RefMinecraftKey;

@HandleBy(reference = "net/minecraft/world/item/SpawnEggItem", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ItemMonsterEgg", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefItemMonsterEgg extends RefItem {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemMonsterEgg;h(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/MinecraftKey;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native RefMinecraftKey getEntityType(RefNmsItemStack itemStack);
}
