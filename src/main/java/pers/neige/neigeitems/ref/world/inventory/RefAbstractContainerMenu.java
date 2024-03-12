package pers.neige.neigeitems.ref.world.inventory;

import org.bukkit.inventory.InventoryView;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/inventory/AbstractContainerMenu", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Container", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefAbstractContainerMenu {
    @HandleBy(reference = "Lnet/minecraft/world/inventory/AbstractContainerMenu;containerId:I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Container;windowId:I", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public int containerId;

    @HandleBy(reference = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getType()Lnet/minecraft/world/inventory/MenuType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Container;getType()Lnet/minecraft/server/v1_14_R1/Containers;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public native RefMenuType getType();

    @HandleBy(reference = "Lnet/minecraft/world/inventory/AbstractContainerMenu;sendAllDataToRemote()V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void sendAllDataToRemote();

    @HandleBy(reference = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getBukkitView()Lorg/bukkit/inventory/InventoryView;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Container;getBukkitView()Lorg/bukkit/inventory/InventoryView;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native InventoryView getBukkitView();
}
