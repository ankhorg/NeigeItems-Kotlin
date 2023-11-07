package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity")
public abstract class RefCraftEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;getHandle()Lnet/minecraft/server/v1_12_R1/Entity;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftEntity;getHandle()Lnet/minecraft/world/entity/Entity;")
    public native RefEntity getHandle();
}
