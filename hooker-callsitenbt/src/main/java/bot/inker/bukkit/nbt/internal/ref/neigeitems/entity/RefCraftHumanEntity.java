package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftHumanEntity")
public class RefCraftHumanEntity extends RefCraftLivingEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftHumanEntity;getHandle()Lnet/minecraft/server/v1_12_R1/EntityHuman;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftHumanEntity;getHandle()Lnet/minecraft/world/entity/player/Player;")
    public native RefEntityHuman getHandle();
}
