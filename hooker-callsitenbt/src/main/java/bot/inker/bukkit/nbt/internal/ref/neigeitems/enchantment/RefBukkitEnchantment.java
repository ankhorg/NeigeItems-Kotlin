package pers.neige.neigeitems.internal.ref.enchantment;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org.bukkit.enchantments/Enchantment", predicates = "craftbukkit_version:[v1_17_R1,)")
public abstract class RefBukkitEnchantment {
    @HandleBy(reference = "Lorg/bukkit/enchantments/Enchantment;acceptingNew:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public static boolean acceptingNew;
}
