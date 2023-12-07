package pers.neige.neigeitems.ref.block;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/BlockPos", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/BlockPosition", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefBlockPos {
    @HandleBy(reference = "Lnet/minecraft/core/BlockPos;ZERO:Lnet/minecraft/core/BlockPos;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/BlockPosition;ZERO:Lnet/minecraft/server/v1_12_R1/BlockPosition;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static final RefBlockPos ZERO = null;

    @HandleBy(reference = "Lnet/minecraft/core/BlockPos;<init>(III)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/BlockPosition;<init>(III)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public RefBlockPos(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }
}
