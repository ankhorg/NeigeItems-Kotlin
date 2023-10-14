package bot.inker.bukkit.nbt.internal.ref.neigeitems.item;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/ItemWrittenBook")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/item/WrittenBookItem")
public final class RefItemWrittenBook extends RefItem {

}
