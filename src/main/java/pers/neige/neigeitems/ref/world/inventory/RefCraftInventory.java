package pers.neige.neigeitems.ref.world.inventory;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftInventory", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefCraftInventory {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftInventory;getInventory()Lnet/minecraft/world/Container;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftInventory;getInventory()Lnet/minecraft/server/v1_12_R1/IInventory;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefContainer getInventory();
}
