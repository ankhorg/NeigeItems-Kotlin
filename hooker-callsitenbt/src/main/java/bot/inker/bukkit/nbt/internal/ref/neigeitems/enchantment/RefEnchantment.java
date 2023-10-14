package bot.inker.bukkit.nbt.internal.ref.neigeitems.enchantment;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefComponent;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntity;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntityLiving;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEquipmentSlot;

import java.util.Map;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/item/enchantment/Enchantment")
public abstract class RefEnchantment {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;slots:[Lnet/minecraft/world/entity/EquipmentSlot;")
    public final RefEquipmentSlot slots = null;
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;category:Lnet/minecraft/world/item/enchantment/EnchantmentCategory;")
    public final RefEnchantmentCategory category = null;

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;<init>(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V")
    protected RefEnchantment(RefRarity weight, RefEnchantmentCategory target, RefEquipmentSlot[] slotTypes) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getSlotItems(Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/Map;")
    public native Map<RefEquipmentSlot, RefNmsItemStack> getSlotItems(RefEntityLiving entity);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getRarity()Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;")
    public native RefRarity getRarity();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinLevel()I")
    public native int getMinLevel();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I")
    public native int getMaxLevel();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinCost(I)I")
    public native int getMinCost(int level);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxCost(I)I")
    public native int getMaxCost(int level);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isCompatibleWith(Lnet/minecraft/world/item/enchantment/Enchantment;)Z")
    public native boolean isCompatibleWith(RefEnchantment other);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;checkCompatibility(Lnet/minecraft/world/item/enchantment/Enchantment;)Z")
    protected native boolean checkCompatibility(RefEnchantment other);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getDescriptionId()Ljava/lang/String;")
    public native String getDescriptionId();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(I)Lnet/minecraft/network/chat/Component;")
    public native RefComponent getFullname(int level);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z")
    public native boolean canEnchant(RefNmsItemStack stack);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;doPostAttack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;I)V")
    public native void doPostAttack(RefEntityLiving user, RefEntity target, int level);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;doPostHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;I)V")
    public native void doPostHurt(RefEntityLiving user, RefEntity target, int level);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isTreasureOnly()Z")
    public native boolean isTreasureOnly();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isCurse()Z")
    public native boolean isCurse();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isTradeable()Z")
    public native boolean isTradeable();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/item/enchantment/Enchantment;isDiscoverable()Z")
    public native boolean isDiscoverable();
}
