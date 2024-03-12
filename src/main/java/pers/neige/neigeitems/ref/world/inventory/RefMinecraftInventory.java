package pers.neige.neigeitems.ref.world.inventory;

import net.kyori.adventure.text.Component;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftInventoryCustom$MinecraftInventory", predicates = "craftbukkit_version:[v1_12_R1,)", useAccessor = true)
public class RefMinecraftInventory implements RefContainer {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftInventoryCustom$MinecraftInventory;title:Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,)", useAccessor = true)
    public String title;

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftInventoryCustom$MinecraftInventory;adventure$title:L;", predicates = "craftbukkit_version:[v1_17_R1,)", useAccessor = true)
    public Component adventure$title;
}
