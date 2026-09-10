package pers.neige.neigeitems.ref.world.item.enchantment;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/item/enchantment/ItemEnchantments", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefItemEnchantments {
    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/ItemEnchantments;withTooltip(Z)Lnet/minecraft/world/item/enchantment/ItemEnchantments;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native RefItemEnchantments withTooltip(boolean showInTooltip);
}
