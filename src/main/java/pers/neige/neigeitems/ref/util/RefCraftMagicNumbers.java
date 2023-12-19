package pers.neige.neigeitems.ref.util;

import pers.neige.neigeitems.ref.nbt.RefItem;
import org.bukkit.Material;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/util/CraftMagicNumbers", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftMagicNumbers {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftMagicNumbers;getItem(Lorg/bukkit/Material;)Lnet/minecraft/world/item/Item;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftMagicNumbers;getItem(Lorg/bukkit/Material;)Lnet/minecraft/server/v1_12_R1/Item;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native RefItem getItem(Material material);
}
