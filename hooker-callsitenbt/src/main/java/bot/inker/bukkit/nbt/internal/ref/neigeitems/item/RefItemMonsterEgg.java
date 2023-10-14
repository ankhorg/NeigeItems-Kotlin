package bot.inker.bukkit.nbt.internal.ref.neigeitems.item;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.RefMinecraftKey;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/ItemMonsterEgg")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/item/SpawnEggItem")
public final class RefItemMonsterEgg extends RefItem {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/ItemMonsterEgg;h(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/MinecraftKey;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public static native RefMinecraftKey getEntityType(RefNmsItemStack itemStack);
}
