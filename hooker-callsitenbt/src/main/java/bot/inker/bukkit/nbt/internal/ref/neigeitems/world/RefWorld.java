package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntity;
import org.spigotmc.SpigotWorldConfig;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/World")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/Level")
public abstract class RefWorld {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/World;spigotConfig:Lorg/spigotmc/SpigotWorldConfig;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/Level;spigotConfig:Lorg/spigotmc/SpigotWorldConfig;")
    public SpigotWorldConfig spigotConfig;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/World;getEntities(Lnet/minecraft/server/v1_12_R1/Entity;Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public native List<RefEntity> getEntities(@Nullable RefEntity except, RefAABB aabb, @Nullable com.google.common.base.Predicate<? super RefEntity> filter);

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/World;getEntities(Lnet/minecraft/server/v1_13_R1/Entity;Lnet/minecraft/server/v1_13_R1/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;")
    public native List<RefEntity> getEntities(@Nullable RefEntity except, RefAABB aabb, @Nullable Predicate<? super RefEntity> filter);
}
