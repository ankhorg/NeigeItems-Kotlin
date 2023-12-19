package pers.neige.neigeitems.ref.world;

import pers.neige.neigeitems.ref.entity.RefEntity;
import org.inksnow.ankhinvoke.comments.HandleBy;
import org.spigotmc.SpigotWorldConfig;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@HandleBy(reference = "net/minecraft/world/level/Level", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/World", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public abstract class RefWorld {
    @HandleBy(reference = "Lnet/minecraft/world/level/Level;spigotConfig:Lorg/spigotmc/SpigotWorldConfig;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/World;spigotConfig:Lorg/spigotmc/SpigotWorldConfig;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public SpigotWorldConfig spigotConfig;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/World;getEntities(Lnet/minecraft/server/v1_12_R1/Entity;Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native List<RefEntity> getEntities(@Nullable RefEntity except, RefAABB aabb, @Nullable com.google.common.base.Predicate<? super RefEntity> filter);

    @HandleBy(reference = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/World;getEntities(Lnet/minecraft/server/v1_13_R1/Entity;Lnet/minecraft/server/v1_13_R1/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    public native List<RefEntity> getEntities(@Nullable RefEntity except, RefAABB aabb, @Nullable Predicate<? super RefEntity> filter);

    @HandleBy(reference = "Lnet/minecraft/world/level/Level;getChunkIfLoaded(II)Lnet/minecraft/world/level/chunk/LevelChunk;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/World;getChunkIfLoaded(II)Lnet/minecraft/server/v1_12_R1/Chunk;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefChunk getChunkIfLoaded(int x, int z);
}
