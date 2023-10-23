package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNbtTagCompound;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefWorld;
import org.bukkit.entity.Entity;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/Entity")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/Entity")
public abstract class RefEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;world:Lnet/minecraft/server/v1_12_R1/World;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;level:Lnet/minecraft/world/level/Level;", accessor = true)
    public RefWorld world;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_20_R1/entity/CraftEntity;")
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
}
