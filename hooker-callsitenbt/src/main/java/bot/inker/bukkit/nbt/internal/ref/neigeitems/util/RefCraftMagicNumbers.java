package pers.neige.neigeitems.internal.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/util/CraftMagicNumbers", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftMagicNumbers {
    @Nullable
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftMagicNumbers;getItem(Lorg/bukkit/Material;)Lnet/minecraft/world/item/Item;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftMagicNumbers;getItem(Lorg/bukkit/Material;)Lnet/minecraft/server/v1_12_R1/Item;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static native RefItem getItem(@NotNull Material material);
}
