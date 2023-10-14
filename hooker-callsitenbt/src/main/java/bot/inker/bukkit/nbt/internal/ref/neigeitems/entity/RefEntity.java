package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
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
}
