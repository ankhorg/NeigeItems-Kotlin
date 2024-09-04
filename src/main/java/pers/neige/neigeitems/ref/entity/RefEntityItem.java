package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.network.syncher.RefEntityDataAccessor;
import pers.neige.neigeitems.ref.world.RefWorld;

import java.util.UUID;

@HandleBy(reference = "net/minecraft/world/entity/item/ItemEntity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityItem", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefEntityItem extends RefEntity {
    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;DATA_ITEM:Lnet/minecraft/network/syncher/EntityDataAccessor;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityItem;ITEM:Lnet/minecraft/server/v1_14_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;b:Lnet/minecraft/server/v1_13_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;c:Lnet/minecraft/server/v1_12_R1/DataWatcherObject;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static final RefEntityDataAccessor<RefNmsItemStack> DATA_ITEM = null;
    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;despawnRate:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public int despawnRate;
    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;age:I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;age:I", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;age:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int age;
    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;pickupDelay:I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;pickupDelay:I", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public int pickupDelay;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;g:Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public String throwerName;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;h:Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public String ownerName;
    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;thrower:Ljava/util/UUID;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityItem;thrower:Ljava/util/UUID;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;f:Ljava/util/UUID;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    public UUID throwerUUID;
    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;target:Ljava/util/UUID;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityItem;owner:Ljava/util/UUID;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;g:Ljava/util/UUID;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
    public UUID ownerUUID;

    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;<init>(Lnet/minecraft/server/v1_12_R1/World;DDDLnet/minecraft/server/v1_12_R1/ItemStack;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefEntityItem(RefWorld world, double x, double y, double z, RefNmsItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;getItemStack()Lnet/minecraft/server/v1_12_R1/ItemStack;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefNmsItemStack getItemStack();

    @HandleBy(reference = "Lnet/minecraft/world/entity/item/ItemEntity;setItem(Lnet/minecraft/world/item/ItemStack;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;setItemStack(Lnet/minecraft/server/v1_12_R1/ItemStack;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void setItemStack(RefNmsItemStack itemStack);
}
