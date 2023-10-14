package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityLiving")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/LivingEntity")
public abstract class RefEntityLiving extends RefEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;b(Lnet/minecraft/server/v1_12_R1/EnumHand;)Lnet/minecraft/server/v1_12_R1/ItemStack;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;")
    public native RefNmsItemStack getItemInHand(RefEnumHand hand);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;a(Lnet/minecraft/server/v1_12_R1/EnumHand;Lnet/minecraft/server/v1_12_R1/ItemStack;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V")
    public native void setItemInHand(RefEnumHand hand, RefNmsItemStack itemStack);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;a(Lnet/minecraft/server/v1_12_R1/EnumHand;)V")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;swingHand(Lnet/minecraft/server/v1_16_R1/EnumHand;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;swing(Lnet/minecraft/world/InteractionHand;)V")
    public native void swing(RefEnumHand hand);

    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;a(Lnet/minecraft/server/v1_15_R1/EnumHand;Z)V")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;swingHand(Lnet/minecraft/server/v1_16_R1/EnumHand;Z)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;swing(Lnet/minecraft/world/InteractionHand;Z)V")
    public native void swing(RefEnumHand hand, boolean fromServerPlayer);
}
