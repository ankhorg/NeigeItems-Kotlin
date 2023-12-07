package pers.neige.neigeitems.internal.ref.enchantment;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import pers.neige.neigeitems.internal.ref.chat.RefComponent;
import pers.neige.neigeitems.internal.ref.entity.RefEntity;
import pers.neige.neigeitems.internal.ref.entity.RefEntityLiving;
import pers.neige.neigeitems.internal.ref.entity.RefEquipmentSlot;

import java.util.Map;

@HandleBy(reference = "net/minecraft/world/item/enchantment/Enchantment", predicates = "craftbukkit_version:[v1_17_R1,)")
public abstract class RefEnchantment {
    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;slots:[Lnet/minecraft/world/entity/EquipmentSlot;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public final RefEquipmentSlot slots = null;
    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;category:Lnet/minecraft/world/item/enchantment/EnchantmentCategory;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public final RefEnchantmentCategory category = null;

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;<init>(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    protected RefEnchantment(RefRarity weight, RefEnchantmentCategory target, RefEquipmentSlot[] slotTypes) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getSlotItems(Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/Map;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native Map<RefEquipmentSlot, RefNmsItemStack> getSlotItems(RefEntityLiving entity);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getRarity()Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefRarity getRarity();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinLevel()I", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native int getMinLevel();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native int getMaxLevel();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinCost(I)I", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native int getMinCost(int level);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxCost(I)I", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native int getMaxCost(int level);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isCompatibleWith(Lnet/minecraft/world/item/enchantment/Enchantment;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isCompatibleWith(RefEnchantment other);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;checkCompatibility(Lnet/minecraft/world/item/enchantment/Enchantment;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    protected native boolean checkCompatibility(RefEnchantment other);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getDescriptionId()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native String getDescriptionId();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(I)Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefComponent getFullname(int level);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean canEnchant(RefNmsItemStack stack);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;doPostAttack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void doPostAttack(RefEntityLiving user, RefEntity target, int level);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;doPostHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void doPostHurt(RefEntityLiving user, RefEntity target, int level);

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isTreasureOnly()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isTreasureOnly();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isCurse()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isCurse();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isTradeable()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isTradeable();

    @HandleBy(reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isDiscoverable()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isDiscoverable();
}
