package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftItem")
public final class RefCraftItem {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftItem;item:Lnet/minecraft/server/v1_12_R1/EntityItem;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftItem;item:Lnet/minecraft/world/entity/item/ItemEntity;", accessor = true)
    public RefEntityItem item;
}
