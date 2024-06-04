package pers.neige.neigeitems.ref.nbt;

import org.bukkit.inventory.ItemStack;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftItemStack extends ItemStack {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;handle:Lnet/minecraft/world/item/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;handle:Lnet/minecraft/server/v1_12_R1/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefNmsItemStack handle;

    private RefCraftItemStack() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;asNMSCopy(Lorg/bukkit/inventory/ItemStack;)Lnet/minecraft/world/item/ItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;asNMSCopy(Lorg/bukkit/inventory/ItemStack;)Lnet/minecraft/server/v1_12_R1/ItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native RefNmsItemStack asNMSCopy(ItemStack original);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;copyNMSStack(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;copyNMSStack(Lnet/minecraft/server/v1_12_R1/ItemStack;I)Lnet/minecraft/server/v1_12_R1/ItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native RefNmsItemStack copyNMSStack(RefNmsItemStack original, int amount);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;asBukkitCopy(Lnet/minecraft/world/item/ItemStack;)Lorg/bukkit/inventory/ItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;asBukkitCopy(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lorg/bukkit/inventory/ItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native ItemStack asBukkitCopy(RefNmsItemStack original);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;asCraftMirror(Lnet/minecraft/world/item/ItemStack;)Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;asCraftMirror(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native RefCraftItemStack asCraftMirror(RefNmsItemStack original);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;asCraftCopy(Lorg/bukkit/inventory/ItemStack;)Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static native RefCraftItemStack asCraftCopy(ItemStack original);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;asNewCraftStack(Lnet/minecraft/world/item/Item;)Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;asNewCraftStack(Lnet/minecraft/server/v1_12_R1/Item;)Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native RefCraftItemStack asNewCraftStack(RefItem original);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;asNewCraftStack(Lnet/minecraft/world/item/Item;I)Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;asNewCraftStack(Lnet/minecraft/server/v1_12_R1/Item;I)Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static native RefCraftItemStack asNewCraftStack(RefItem original, int amount);
}
