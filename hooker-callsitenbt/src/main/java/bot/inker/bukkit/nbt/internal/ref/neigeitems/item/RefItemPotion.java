package bot.inker.bukkit.nbt.internal.ref.neigeitems.item;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/ItemPotion")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/item/PotionItem")
public final class RefItemPotion extends RefItem {

}
