package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "org.bukkit.enchantments/Enchantment")
public abstract class RefBukkitEnchantment {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/enchantments/Enchantment;acceptingNew:Z", accessor = true)
    public static boolean acceptingNew;
}
