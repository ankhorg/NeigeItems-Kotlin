package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNbtTagCompound;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefComponent;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefAABB;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefVec3;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefWorld;
import org.bukkit.entity.Entity;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/Entity")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/Entity")
public abstract class RefEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;motX:D")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "")
    public double motX;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;motY:D")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "")
    public double motY;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;motZ:D")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "")
    public double motZ;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;yaw:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;yRot:F", accessor = true)
    public float yaw;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;pitch:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;xRot:F", accessor = true)
    public float pitch;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastYaw:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;yRotO:F")
    public float lastYaw;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastPitch:F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;xRotO:F")
    public float lastPitch;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;world:Lnet/minecraft/server/v1_12_R1/World;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;level:Lnet/minecraft/world/level/Level;", accessor = true)
    public RefWorld world;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftEntity;")
    public native Entity getBukkitEntity();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;c(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Z")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/Entity;a_(Lnet/minecraft/server/v1_16_R1/NBTTagCompound;)Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;saveAsPassenger(Lnet/minecraft/nbt/CompoundTag;)Z")
    public native boolean saveAsPassenger(RefNbtTagCompound nbt);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;d(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;save(Lnet/minecraft/nbt/CompoundTag;)Z")
    public native boolean save(RefNbtTagCompound nbt);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;save(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;")
    public native RefNbtTagCompound saveWithoutId(RefNbtTagCompound nbt);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;f(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/Entity;load(Lnet/minecraft/server/v1_16_R1/NBTTagCompound;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;load(Lnet/minecraft/nbt/CompoundTag;)V")
    public native void load(RefNbtTagCompound nbt);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;move(Lnet/minecraft/server/v1_12_R1/EnumMoveType;DDD)V")
    @HandleBy(version = CbVersion.v1_14_R1, reference = "")
    public native void move(RefMoverType movementType, double d0, double d1, double d2);

    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/Entity;move(Lnet/minecraft/server/v1_14_R1/EnumMoveType;Lnet/minecraft/server/v1_14_R1/Vec3D;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V")
    public native void move(RefMoverType movementType, RefVec3 movement);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;getVehicle()Lnet/minecraft/server/v1_12_R1/Entity;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getRootVehicle()Lnet/minecraft/world/entity/Entity;")
    public native Entity getRootVehicle();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPositionRotation(DDDFF)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;absMoveTo(DDDFF)V")
    public native void absMoveTo(double x, double y, double z, float yaw, float pitch);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPositionRotation(DDDFF)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;moveTo(DDDFF)V")
    public native void moveTo(double x, double y, double z, float yaw, float pitch);

    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/Entity;getMot()Lnet/minecraft/server/v1_14_R1/Vec3D;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;")
    public native RefVec3 getDeltaMovement();

    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/Entity;setMot(Lnet/minecraft/server/v1_14_R1/Vec3D;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V")
    public native void setDeltaMovement(RefVec3 velocity);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;addDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V")
    public native void addDeltaMovement(RefVec3 velocity);

    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/Entity;setMot(DDD)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V")
    public native void setDeltaMovement(double x, double y, double z);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getYRot()F")
    public native float getYaw();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;setYRot(F)V")
    public native void setYaw(float yaw);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getXRot()F")
    public native float getPitch();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;setXRot(F)V")
    public native void setPitch(float pitch);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;getHeadRotation()F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getYHeadRot()F")
    public native float getHeadRotation();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;setHeadRotation(F)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;setYHeadRot(F)V")
    public native void setHeadRotation(float headRotation);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;lookAt(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;Lnet/minecraft/world/phys/Vec3;)V")
    public native void lookAt(RefAnchor anchorPoint, RefVec3 target);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;getHeadHeight()F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F")
    public native final float getEyeHeight();

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/Entity;getCustomName()Lnet/minecraft/server/v1_13_R1/IChatBaseComponent;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getCustomName()Lnet/minecraft/network/chat/Component;")
    public native RefComponent getCustomName();

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/Entity;setCustomName(Lnet/minecraft/server/v1_13_R1/IChatBaseComponent;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;setCustomName(Lnet/minecraft/network/chat/Component;)V")
    public native void setCustomName(RefComponent name);

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/Entity;hasCustomName()Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;hasCustomName()Z")
    public native boolean hasCustomName();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBoundingBox()Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;")
    public native RefAABB getBoundingBox();
}
