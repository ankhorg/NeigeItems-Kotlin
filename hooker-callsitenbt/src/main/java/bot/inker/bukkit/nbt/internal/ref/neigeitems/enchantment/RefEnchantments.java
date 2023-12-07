package pers.neige.neigeitems.internal.ref.enchantment;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/item/enchantment/Enchantments", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefEnchantments {
    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantments;register(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native RefEnchantment register(String name, RefEnchantment enchantment);
}
