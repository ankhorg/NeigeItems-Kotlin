package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import org.bukkit.entity.Entity;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/CraftChunk")
public final class RefCraftChunk {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftChunk;<init>(Lnet/minecraft/server/v1_12_R1/Chunk;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftChunk;<init>(Lnet/minecraft/world/level/chunk/LevelChunk;)V")
    public RefCraftChunk(RefChunk chunk) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftChunk;getEntities()[Lorg/bukkit/entity/Entity;")
    public native Entity[] getEntities();
}
