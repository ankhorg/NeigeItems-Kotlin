package bot.inker.bukkit.nbt.internal.ref.neigeitems.block;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/block/CraftBlockEntityState")
public abstract class RefCraftBlockEntityState<T extends RefBlockEntity> {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftBlockEntityState;snapshot:Lnet/minecraft/server/v1_12_R1/TileEntity;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/block/CraftBlockEntityState;snapshot:Lnet/minecraft/world/level/block/entity/BlockEntity;", accessor = true)
    public final T snapshot = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftBlockEntityState;getSnapshot()Lnet/minecraft/server/v1_12_R1/TileEntity;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/block/CraftBlockEntityState;getSnapshot()Lnet/minecraft/world/level/block/entity/BlockEntity;", accessor = true)
    public native T getSnapshot();
}
