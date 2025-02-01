package pers.neige.neigeitems.ref.nbt;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "org/bukkit/inventory/ItemStack", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefBukkitItemStack {
    @HandleBy(reference = "Lorg/bukkit/inventory/ItemStack;meta:Lorg/bukkit/inventory/meta/ItemMeta;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public @Nullable ItemMeta meta;
    @HandleBy(reference = "Lorg/bukkit/inventory/ItemStack;craftDelegate:Lorg/bukkit/inventory/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public ItemStack craftDelegate;
}
