package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

import java.util.UUID;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityItem")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/item/ItemEntity")
public final class RefEntityItem extends RefEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;<init>(Lnet/minecraft/server/v1_12_R1/World;DDDLnet/minecraft/server/v1_12_R1/ItemStack;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V")
    public RefEntityItem(RefWorld world, double x, double y, double z, RefNmsItemStack itemstack) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;getItemStack()Lnet/minecraft/server/v1_12_R1/ItemStack;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
    public native RefNmsItemStack getItemStack();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;setItemStack(Lnet/minecraft/server/v1_12_R1/ItemStack;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;setItem(Lnet/minecraft/world/item/ItemStack;)V")
    public native void setItemStack(RefNmsItemStack itemStack);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;despawnRate:I", accessor = true)
    public int despawnRate;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;age:I", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;age:I")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;age:I")
    public int age;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;pickupDelay:I")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;pickupDelay:I")
    public int pickupDelay;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;g:Ljava/lang/String;", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public String throwerName;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityItem;h:Ljava/lang/String;", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public String ownerName;

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;f:Ljava/util/UUID;", accessor = true)
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityItem;thrower:Ljava/util/UUID;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;thrower:Ljava/util/UUID;")
    public UUID throwerUUID;

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/EntityItem;g:Ljava/util/UUID;", accessor = true)
    @HandleBy(version = CbVersion.v1_14_R1, reference = "Lnet/minecraft/server/v1_14_R1/EntityItem;owner:Ljava/util/UUID;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/item/ItemEntity;target:Ljava/util/UUID;")
    public UUID ownerUUID;
}
