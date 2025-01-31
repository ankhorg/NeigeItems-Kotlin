package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.core.component.RefDataComponentType;
import pers.neige.neigeitems.ref.enchantment.RefEnchantment;
import pers.neige.neigeitems.ref.entity.RefEntityType;

@HandleBy(reference = "net/minecraft/core/registries/BuiltInRegistries", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefBuiltInRegistries {
    @HandleBy(reference = "Lnet/minecraft/core/registries/BuiltInRegistries;ENCHANTMENT:Lnet/minecraft/core/Registry;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefRegistry<RefEnchantment> ENCHANTMENT = null;

    @HandleBy(reference = "Lnet/minecraft/core/registries/BuiltInRegistries;ENTITY_TYPE:Lnet/minecraft/core/DefaultedRegistry;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDefaultedRegistry<RefEntityType<?>> ENTITY_TYPE = null;

    @HandleBy(reference = "Lnet/minecraft/core/registries/BuiltInRegistries;DATA_COMPONENT_TYPE:Lnet/minecraft/core/Registry;", predicates = "craftbukkit_version:[v1_21_R1,)")
    public static final RefRegistry<RefDataComponentType<?>> DATA_COMPONENT_TYPE = null;
}
