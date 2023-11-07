package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefVec3;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityLiving")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/LivingEntity")
public abstract class RefEntityLiving extends RefEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aN:F")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aQ:F")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aK:F")
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aI:F")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aH:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;yBodyRot:F")
    public float yBodyRot;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aO:F")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aR:F")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aL:F")
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aJ:F")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aI:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;yBodyRotO:F")
    public float yBodyRotO;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aP:F")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aS:F")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aM:F")
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aK:F")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aJ:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;yHeadRot:F")
    public float yHeadRot;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aQ:F")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aT:F")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aN:F")
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aL:F")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aK:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;yHeadRotO:F")
    public float yHeadRotO;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aE:I", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aH:I", accessor = true)
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aD:I", accessor = true)
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aB:I", accessor = true)
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;at:I", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;attackStrengthTicker:I", accessor = true)
    public int attackStrengthTicker;

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

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;cu()V", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;cH()V", accessor = true)
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;jump()V", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;jumpFromGround()V", accessor = true)
    public native void jumpFromGround();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;getHeadRotation()F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;getYHeadRot()F")
    public native float getHeadRotation();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;setHeadRotation(F)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;setYHeadRot(F)V")
    public native void setHeadRotation(float headRotation);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/LivingEntity;lookAt(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;Lnet/minecraft/world/phys/Vec3;)V")
    public native void lookAt(RefAnchor anchorPoint, RefVec3 target);
}
