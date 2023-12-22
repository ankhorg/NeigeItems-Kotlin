package pers.neige.neigeitems.ref.block.sign;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.block.RefBlockEntity;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.block.RefBlockState;

@HandleBy(reference = "net/minecraft/world/level/block/entity/SignBlockEntity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/TileEntitySign", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefSignBlockEntity extends RefBlockEntity {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/TileEntitySign;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefSignBlockEntity() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefSignBlockEntity(RefBlockPos pos, RefBlockState state) {
        throw new UnsupportedOperationException();
    }
}
