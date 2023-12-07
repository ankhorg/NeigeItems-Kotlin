package pers.neige.neigeitems.ref.world;

import org.bukkit.entity.Entity;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/CraftChunk", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftChunk {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftChunk;<init>(Lnet/minecraft/world/level/chunk/LevelChunk;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftChunk;<init>(Lnet/minecraft/server/v1_12_R1/Chunk;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public RefCraftChunk(RefChunk chunk) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftChunk;getEntities()[Lorg/bukkit/entity/Entity;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native Entity[] getEntities();
}
