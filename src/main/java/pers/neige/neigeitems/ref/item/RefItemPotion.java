package pers.neige.neigeitems.ref.item;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefItem;

@HandleBy(reference = "net/minecraft/world/item/PotionItem", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ItemPotion", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefItemPotion extends RefItem {

}
