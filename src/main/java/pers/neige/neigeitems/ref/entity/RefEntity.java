package pers.neige.neigeitems.ref.entity;

import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.argument.RefAnchor;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.world.RefAABB;
import pers.neige.neigeitems.ref.world.RefVec3;
import pers.neige.neigeitems.ref.world.RefWorld;
import org.bukkit.entity.Entity;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/Entity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Entity", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefEntity {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;motX:D", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public double motX;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;motY:D", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public double motY;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;motZ:D", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public double motZ;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;yRot:F", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;yaw:F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public float yaw;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;xRot:F", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;pitch:F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public float pitch;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;yRotO:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastYaw:F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public float lastYaw;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;xRotO:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastPitch:F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public float lastPitch;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;level:Lnet/minecraft/world/level/Level;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;world:Lnet/minecraft/server/v1_12_R1/World;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public RefWorld world;

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftEntity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native Entity getBukkitEntity();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;saveAsPassenger(Lnet/minecraft/nbt/CompoundTag;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/Entity;a_(Lnet/minecraft/server/v1_16_R1/NBTTagCompound;)Z", predicates = "craftbukkit_version:[v1_16_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;c(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean saveAsPassenger(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;save(Lnet/minecraft/nbt/CompoundTag;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;d(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean save(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;save(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native RefNbtTagCompound saveWithoutId(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;load(Lnet/minecraft/nbt/CompoundTag;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/Entity;load(Lnet/minecraft/server/v1_16_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_16_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;f(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void load(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;move(Lnet/minecraft/server/v1_12_R1/EnumMoveType;DDD)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native void move(RefMoverType movementType, double d0, double d1, double d2);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;move(Lnet/minecraft/server/v1_14_R1/EnumMoveType;Lnet/minecraft/server/v1_14_R1/Vec3D;)V", predicates = "craftbukkit_version:[v1_14_R1,)")
    public native void move(RefMoverType movementType, RefVec3 movement);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getRootVehicle()Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getVehicle()Lnet/minecraft/server/v1_12_R1/Entity;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native Entity getRootVehicle();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;absMoveTo(DDDFF)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPositionRotation(DDDFF)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void absMoveTo(double x, double y, double z, float yaw, float pitch);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;moveTo(DDDFF)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPositionRotation(DDDFF)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void moveTo(double x, double y, double z, float yaw, float pitch);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;getMot()Lnet/minecraft/server/v1_14_R1/Vec3D;", predicates = "craftbukkit_version:[v1_14_R1,)")
    public native RefVec3 getDeltaMovement();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;setMot(Lnet/minecraft/server/v1_14_R1/Vec3D;)V", predicates = "craftbukkit_version:[v1_14_R1,)")
    public native void setDeltaMovement(RefVec3 velocity);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;addDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void addDeltaMovement(RefVec3 velocity);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;setMot(DDD)V", predicates = "craftbukkit_version:[v1_14_R1,)")
    public native void setDeltaMovement(double x, double y, double z);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getYRot()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native float getYaw();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setYRot(F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void setYaw(float yaw);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getXRot()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native float getPitch();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setXRot(F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void setPitch(float pitch);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getYHeadRot()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getHeadRotation()F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native float getHeadRotation();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setYHeadRot(F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setHeadRotation(F)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setHeadRotation(float headRotation);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;lookAt(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void lookAt(RefAnchor anchorPoint, RefVec3 target);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getHeadHeight()F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native final float getEyeHeight();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getCustomName()Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;getCustomName()Lnet/minecraft/server/v1_13_R1/IChatBaseComponent;", predicates = "craftbukkit_version:[v1_13_R1,)")
    public native RefComponent getCustomName();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setCustomName(Lnet/minecraft/network/chat/Component;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;setCustomName(Lnet/minecraft/server/v1_13_R1/IChatBaseComponent;)V", predicates = "craftbukkit_version:[v1_13_R1,)")
    public native void setCustomName(RefComponent name);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;hasCustomName()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;hasCustomName()Z", predicates = "craftbukkit_version:[v1_13_R1,)")
    public native boolean hasCustomName();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBoundingBox()Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native RefAABB getBoundingBox();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPosition(DDD)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setPosition(double x, double y, double z);
}
