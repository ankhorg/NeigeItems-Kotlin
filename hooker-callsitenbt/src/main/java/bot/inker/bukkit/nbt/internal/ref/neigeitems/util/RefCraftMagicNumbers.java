package bot.inker.bukkit.nbt.internal.ref.neigeitems.util;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/util/CraftMagicNumbers")
public final class RefCraftMagicNumbers {
    @Nullable
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftMagicNumbers;getItem(Lorg/bukkit/Material;)Lnet/minecraft/server/v1_12_R1/Item;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftMagicNumbers;getItem(Lorg/bukkit/Material;)Lnet/minecraft/world/item/Item;")
    public static native RefItem getItem(@NotNull Material material);
}
