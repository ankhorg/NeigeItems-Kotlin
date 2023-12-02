package bot.inker.bukkit.nbt.internal.ref.neigeitems.block;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/BlockPosition")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/core/BlockPos")
public final class RefBlockPos {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/BlockPosition;ZERO:Lnet/minecraft/server/v1_12_R1/BlockPosition;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/core/BlockPos;ZERO:Lnet/minecraft/core/BlockPos;")
    public static final RefBlockPos ZERO = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/BlockPosition;<init>(III)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/core/BlockPos;<init>(III)V")
    public RefBlockPos(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }
}
