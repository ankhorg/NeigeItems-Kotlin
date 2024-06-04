package pers.neige.neigeitems.ref.enchantment;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/enchantments/Enchantment", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefBukkitEnchantment {
    @HandleBy(reference = "Lorg/bukkit/enchantments/Enchantment;acceptingNew:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public static boolean acceptingNew;

    @HandleBy(reference = "Lorg/bukkit/enchantments/Enchantment;getId()I", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native int getId();
}
