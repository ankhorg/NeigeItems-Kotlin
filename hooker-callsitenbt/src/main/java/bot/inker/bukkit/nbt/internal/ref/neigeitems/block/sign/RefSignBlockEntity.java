package bot.inker.bukkit.nbt.internal.ref.neigeitems.block.sign;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefBlockEntity;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefBlockPos;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefBlockState;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/TileEntitySign")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/block/entity/SignBlockEntity")
public final class RefSignBlockEntity extends RefBlockEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/TileEntitySign;<init>()V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefSignBlockEntity() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V")
    public RefSignBlockEntity(RefBlockPos pos, RefBlockState state) {
        throw new UnsupportedOperationException();
    }
}
