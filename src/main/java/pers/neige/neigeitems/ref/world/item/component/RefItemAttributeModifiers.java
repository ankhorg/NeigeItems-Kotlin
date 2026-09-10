package pers.neige.neigeitems.ref.world.item.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/item/component/ItemAttributeModifiers", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefItemAttributeModifiers {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;withTooltip(Z)Lnet/minecraft/world/item/component/ItemAttributeModifiers;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native RefItemAttributeModifiers withTooltip(boolean showInTooltip);
}
