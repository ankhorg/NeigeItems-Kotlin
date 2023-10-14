package bot.inker.bukkit.nbt.internal.ref.neigeitems.registry;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.enchantment.RefEnchantment;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/core/registries/BuiltInRegistries")
public final class RefBuiltInRegistries {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/core/registries/BuiltInRegistries;ENCHANTMENT:Lnet/minecraft/core/Registry;")
    public static final RefRegistry<RefEnchantment> ENCHANTMENT = null;
}
