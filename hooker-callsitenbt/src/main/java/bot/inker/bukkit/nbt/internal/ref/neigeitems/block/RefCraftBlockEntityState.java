package pers.neige.neigeitems.internal.ref.block;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/block/CraftBlockEntityState", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefCraftBlockEntityState<T extends RefBlockEntity> {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/block/CraftBlockEntityState;snapshot:Lnet/minecraft/world/level/block/entity/BlockEntity;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftBlockEntityState;snapshot:Lnet/minecraft/server/v1_12_R1/TileEntity;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public final T snapshot = null;

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/block/CraftBlockEntityState;getSnapshot()Lnet/minecraft/world/level/block/entity/BlockEntity;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftBlockEntityState;getSnapshot()Lnet/minecraft/server/v1_12_R1/TileEntity;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public native T getSnapshot();
}
