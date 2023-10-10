package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/item/enchantment/Enchantments")
public class RefEnchantments {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantments;register(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;", accessor = true)
    public static native RefEnchantment register(String name, RefEnchantment enchantment);
}
