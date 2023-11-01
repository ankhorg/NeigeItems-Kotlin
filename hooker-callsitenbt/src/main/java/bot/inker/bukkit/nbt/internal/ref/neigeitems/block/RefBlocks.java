package bot.inker.bukkit.nbt.internal.ref.neigeitems.block;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/Blocks")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/block/Blocks")
public final class RefBlocks {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Blocks;ANVIL:Lnet/minecraft/server/v1_12_R1/Block;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/block/Blocks;ANVIL:Lnet/minecraft/world/level/block/Block;")
    public static final RefBlock ANVIL = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Blocks;ENCHANTING_TABLE:Lnet/minecraft/server/v1_12_R1/Block;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/block/Blocks;ENCHANTING_TABLE:Lnet/minecraft/world/level/block/Block;")
    public static final RefBlock ENCHANTING_TABLE = null;
}
