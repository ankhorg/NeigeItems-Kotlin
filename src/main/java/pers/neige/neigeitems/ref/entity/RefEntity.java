package pers.neige.neigeitems.ref.entity;

import org.bukkit.entity.Entity;
import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.argument.RefAnchor;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.network.RefPacket;
import pers.neige.neigeitems.ref.network.syncher.RefEntityDataAccessor;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData;
import pers.neige.neigeitems.ref.world.RefAABB;
import pers.neige.neigeitems.ref.world.RefVec3;
import pers.neige.neigeitems.ref.world.RefWorld;

import java.util.Optional;

@HandleBy(reference = "net/minecraft/world/entity/Entity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Entity", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public abstract class RefEntity {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;aB:Lnet/minecraft/server/v1_12_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static final RefEntityDataAccessor<String> DATA_CUSTOM_NAME_STRING = null;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;DATA_CUSTOM_NAME:Lnet/minecraft/network/syncher/EntityDataAccessor;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R2/Entity;aq:Lnet/minecraft/server/v1_16_R2/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_16_R2,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/Entity;ax:Lnet/minecraft/server/v1_16_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_16_R1,v1_16_R2)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;az:Lnet/minecraft/server/v1_14_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;aE:Lnet/minecraft/server/v1_13_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    public static final RefEntityDataAccessor<Optional<RefComponent>> DATA_CUSTOM_NAME_COMPONENT = null;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;DATA_CUSTOM_NAME_VISIBLE:Lnet/minecraft/network/syncher/EntityDataAccessor;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R2/Entity;ar:Lnet/minecraft/server/v1_16_R2/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_16_R2,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/Entity;ay:Lnet/minecraft/server/v1_16_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_16_R1,v1_16_R2)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;aA:Lnet/minecraft/server/v1_14_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;aF:Lnet/minecraft/server/v1_13_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;aC:Lnet/minecraft/server/v1_12_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static final RefEntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE = null;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;motX:D", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public double motX;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;motY:D", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public double motY;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;motZ:D", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public double motZ;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;yRot:F", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;yaw:F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public float yaw;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;xRot:F", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;pitch:F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public float pitch;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;yRotO:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastYaw:F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public float lastYaw;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;xRotO:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastPitch:F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public float lastPitch;
    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;level:Lnet/minecraft/world/level/Level;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;world:Lnet/minecraft/server/v1_12_R1/World;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefWorld world;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastX:D", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double lastX;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastY:D", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double lastY;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;lastZ:D", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double lastZ;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;locX:D", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double locX;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;locY:D", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double locY;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;locZ:D", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double locZ;


    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftEntity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native Entity getBukkitEntity();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;saveAsPassenger(Lnet/minecraft/nbt/CompoundTag;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/Entity;a_(Lnet/minecraft/server/v1_16_R1/NBTTagCompound;)Z", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;c(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Z", predicates = "craftbukkit_version:[v1_12_R1,v1_16_R1)")
    public native boolean saveAsPassenger(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;save(Lnet/minecraft/nbt/CompoundTag;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;d(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean save(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;save(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefNbtTagCompound saveWithoutId(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;load(Lnet/minecraft/nbt/CompoundTag;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/Entity;load(Lnet/minecraft/server/v1_16_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;f(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_16_R1)")
    public native void load(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;move(Lnet/minecraft/server/v1_12_R1/EnumMoveType;DDD)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native void move(RefMoverType movementType, double d0, double d1, double d2);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;move(Lnet/minecraft/server/v1_14_R1/EnumMoveType;Lnet/minecraft/server/v1_14_R1/Vec3D;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public native void move(RefMoverType movementType, RefVec3 movement);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getRootVehicle()Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getVehicle()Lnet/minecraft/server/v1_12_R1/Entity;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native Entity getRootVehicle();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;absMoveTo(DDDFF)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPositionRotation(DDDFF)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void absMoveTo(double x, double y, double z, float yaw, float pitch);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;moveTo(DDDFF)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPositionRotation(DDDFF)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void moveTo(double x, double y, double z, float yaw, float pitch);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;getMot()Lnet/minecraft/server/v1_14_R1/Vec3D;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public native RefVec3 getDeltaMovement();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;setMot(Lnet/minecraft/server/v1_14_R1/Vec3D;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public native void setDeltaMovement(RefVec3 velocity);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;addDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void addDeltaMovement(RefVec3 velocity);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;setMot(DDD)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
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
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getHeadRotation()F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native float getHeadRotation();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setYHeadRot(F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setHeadRotation(F)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void setHeadRotation(float headRotation);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;lookAt(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void lookAt(RefAnchor anchorPoint, RefVec3 target);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getHeadHeight()F", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native final float getEyeHeight();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getCustomName()Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;getCustomName()Lnet/minecraft/server/v1_13_R1/IChatBaseComponent;", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    public native RefComponent getCustomName();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setCustomName(Lnet/minecraft/network/chat/Component;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;setCustomName(Lnet/minecraft/server/v1_13_R1/IChatBaseComponent;)V", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    public native void setCustomName(RefComponent name);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;hasCustomName()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;hasCustomName()Z", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    public native boolean hasCustomName();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBoundingBox()Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefAABB getBoundingBox();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPosition(DDD)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void setPosition(double x, double y, double z);

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;isInWater()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;isInWater()Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean isInWater();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;isPassenger()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;isPassenger()Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean isPassenger();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getType()Lnet/minecraft/world/entity/EntityType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;getEntityType()Lnet/minecraft/server/v1_14_R1/EntityTypes;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/Entity;P()Lnet/minecraft/server/v1_13_R1/EntityTypes;", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    public native RefEntityType<?> getType();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getEntityData()Lnet/minecraft/network/syncher/SynchedEntityData;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getDataWatcher()Lnet/minecraft/server/v1_12_R1/DataWatcher;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefSynchedEntityData getEntityData();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getAddEntityPacket()Lnet/minecraft/network/protocol/Packet;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/Entity;P()Lnet/minecraft/server/v1_15_R1/Packet;", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/Entity;L()Lnet/minecraft/server/v1_15_R1/Packet;", predicates = "craftbukkit_version:[v1_15_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/Entity;N()Lnet/minecraft/server/v1_14_R1/Packet;", predicates = "craftbukkit_version:[v1_14_R1,v1_15_R1)")
    public native RefPacket<?> getAddEntityPacket();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getId()I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getId()I", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native int getId();

    @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;tick()V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;B_()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native void tick();
}
