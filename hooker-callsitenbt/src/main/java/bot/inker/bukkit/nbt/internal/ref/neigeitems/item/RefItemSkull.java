package pers.neige.neigeitems.internal.ref.item;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;

@HandleBy(reference = "net/minecraft/world/item/PlayerHeadItem", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/ItemSkullPlayer", predicates = "craftbukkit_version:[v1_13_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ItemSkull", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefItemSkull extends RefItem {

}
