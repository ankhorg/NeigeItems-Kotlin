package pers.neige.neigeitems.ref.block;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/level/block/Blocks", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Blocks", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefBlocks {
    @HandleBy(reference = "Lnet/minecraft/world/level/block/Blocks;ANVIL:Lnet/minecraft/world/level/block/Block;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Blocks;ANVIL:Lnet/minecraft/server/v1_12_R1/Block;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static final RefBlock ANVIL = null;

    @HandleBy(reference = "Lnet/minecraft/world/level/block/Blocks;ENCHANTING_TABLE:Lnet/minecraft/world/level/block/Block;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Blocks;ENCHANTING_TABLE:Lnet/minecraft/server/v1_12_R1/Block;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static final RefBlock ENCHANTING_TABLE = null;
}
