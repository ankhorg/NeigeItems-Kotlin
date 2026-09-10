package pers.neige.neigeitems.ref.world.item.equipment.trim;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/item/equipment/trim/ArmorTrim", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefArmorTrim {
    @HandleBy(reference = "Lnet/minecraft/world/item/equipment/trim/ArmorTrim;withTooltip(Z)Lnet/minecraft/world/item/equipment/trim/ArmorTrim;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native RefArmorTrim withTooltip(boolean showInTooltip);
}
