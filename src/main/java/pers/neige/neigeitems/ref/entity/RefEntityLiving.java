package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.argument.RefAnchor;
import pers.neige.neigeitems.ref.damagesource.RefDamageSource;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.world.RefVec3;

@HandleBy(reference = "net/minecraft/world/entity/LivingEntity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityLiving", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public abstract class RefEntityLiving extends RefEntity {
    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;yBodyRot:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aH:F", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aI:F", predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aK:F", predicates = "craftbukkit_version:[v1_14_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aQ:F", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aN:F", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public float yBodyRot;
    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;yBodyRotO:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aI:F", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aJ:F", predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aL:F", predicates = "craftbukkit_version:[v1_14_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aR:F", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aO:F", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public float yBodyRotO;
    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;yHeadRot:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aJ:F", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aK:F", predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aM:F", predicates = "craftbukkit_version:[v1_14_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aS:F", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aP:F", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public float yHeadRot;
    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;yHeadRotO:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;aK:F", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aL:F", predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aN:F", predicates = "craftbukkit_version:[v1_14_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aT:F", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aQ:F", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public float yHeadRotO;

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;attackStrengthTicker:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;at:I", useAccessor = true, predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;aB:I", useAccessor = true, predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;aD:I", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;aH:I", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;aE:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int attackStrengthTicker;

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;lastDamageSource:Lnet/minecraft/world/damagesource/DamageSource;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefDamageSource lastDamageSource;

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;b(Lnet/minecraft/server/v1_12_R1/EnumHand;)Lnet/minecraft/server/v1_12_R1/ItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefNmsItemStack getItemInHand(RefEnumHand hand);

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;a(Lnet/minecraft/server/v1_12_R1/EnumHand;Lnet/minecraft/server/v1_12_R1/ItemStack;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void setItemInHand(RefEnumHand hand, RefNmsItemStack itemStack);

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;swing(Lnet/minecraft/world/InteractionHand;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;swingHand(Lnet/minecraft/server/v1_16_R1/EnumHand;)V", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;a(Lnet/minecraft/server/v1_12_R1/EnumHand;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_16_R1)")
    public native void swing(RefEnumHand hand);

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;swing(Lnet/minecraft/world/InteractionHand;Z)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/EntityLiving;swingHand(Lnet/minecraft/server/v1_16_R1/EnumHand;Z)V", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EntityLiving;a(Lnet/minecraft/server/v1_15_R1/EnumHand;Z)V", predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    public native void swing(RefEnumHand hand, boolean fromServerPlayer);

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;jumpFromGround()V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;jump()V", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;cH()V", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;cu()V", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native void jumpFromGround();

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;getYHeadRot()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;getHeadRotation()F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native float getHeadRotation();

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;setYHeadRot(F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;setHeadRotation(F)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void setHeadRotation(float headRotation);

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;lookAt(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void lookAt(RefAnchor anchorPoint, RefVec3 target);

    @HandleBy(reference = "Lnet/minecraft/world/entity/LivingEntity;onClimbable()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityLiving;isClimbing()Z", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityLiving;z_()Z", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityLiving;m_()Z", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native boolean onClimbable();
}
