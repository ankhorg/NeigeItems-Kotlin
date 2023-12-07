package pers.neige.neigeitems.internal.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.internal.ref.enchantment.RefEnchantment;

@HandleBy(reference = "net/minecraft/core/registries/BuiltInRegistries", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefBuiltInRegistries {
    @HandleBy(reference = "Lnet/minecraft/core/registries/BuiltInRegistries;ENCHANTMENT:Lnet/minecraft/core/Registry;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefRegistry<RefEnchantment> ENCHANTMENT = null;
}
