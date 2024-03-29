package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.enchantment;

import pers.neige.neigeitems.ref.chat.RefChatFormatting;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.chat.RefMutableComponent;
import pers.neige.neigeitems.ref.enchantment.RefEnchantment;
import pers.neige.neigeitems.ref.enchantment.RefEnchantmentCategory;
import pers.neige.neigeitems.ref.enchantment.RefRarity;
import pers.neige.neigeitems.ref.entity.RefEquipmentSlot;
import pers.neige.neigeitems.ref.entity.RefMobType;

/**
 * 一级提供 100 伤害
 */
public class SuperDamageEnchantment extends RefEnchantment {
    public SuperDamageEnchantment() {
        super(
                RefRarity.COMMON,
                RefEnchantmentCategory.WEAPON,
                new RefEquipmentSlot[]{
                        RefEquipmentSlot.MAINHAND,
                        RefEquipmentSlot.OFFHAND,
                        RefEquipmentSlot.FEET,
                        RefEquipmentSlot.LEGS,
                        RefEquipmentSlot.CHEST,
                        RefEquipmentSlot.HEAD,
                }
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public RefComponent getFullname(int level) {
        RefMutableComponent mutableComponent = RefComponent.literal("超级锋利");
        mutableComponent.withStyle(RefChatFormatting.GRAY);

        if (level != 1 || this.getMaxLevel() != 1) {
            mutableComponent.append(RefComponent.literal(" ")).append(RefComponent.translatable("enchantment.level." + level));
        }

        return mutableComponent;
    }

    @Override
    public float getDamageBonus(int level, RefMobType group) {
        return level * 100;
    }
}
