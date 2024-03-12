package pers.neige.neigeitems.ref.world.inventory;

import org.bukkit.event.inventory.InventoryType;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftContainer", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefCraftContainer {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftContainer;getNotchInventoryType(Lorg/bukkit/event/inventory/InventoryType;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native static String getNotchInventoryType0(InventoryType type);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftContainer;getNotchInventoryType(Lorg/bukkit/inventory/Inventory;)Lnet/minecraft/world/inventory/MenuType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_14_R1/inventory/CraftContainer;getNotchInventoryType(Lorg/bukkit/event/inventory/InventoryType;)Lnet/minecraft/server/v1_14_R1/Containers;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public native static RefMenuType getNotchInventoryType1(InventoryType type);
}
