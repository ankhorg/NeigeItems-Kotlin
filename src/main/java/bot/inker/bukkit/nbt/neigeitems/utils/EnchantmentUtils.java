package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.internal.ref.*;

public class EnchantmentUtils {
    public static void registerEnchantment(String name, RefEnchantment enchantment) {
        RefMappedRegistry<RefEnchantment> ENCHANTMENT = (RefMappedRegistry<RefEnchantment>) RefBuiltInRegistries.ENCHANTMENT;
        ENCHANTMENT.frozen = false;
        RefBukkitEnchantment.acceptingNew = true;
        try {
            RefEnchantments.register(name, enchantment);
            RefReference<RefEnchantment> reference = ENCHANTMENT.byValue.get(enchantment);
            if (reference != null) {
                reference.bindValue(enchantment);
            }
        } catch (Throwable error) {
            error.printStackTrace();
        }
        ENCHANTMENT.frozen = true;
        RefBukkitEnchantment.acceptingNew = false;
    }

    public static Class<?> getNmsEnchantmentClass() {
        return RefEnchantment.class;
    }
}
