package pers.neige.neigeitems.ref.world.level.chunk.storage;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.world.level.RefChunkPos;

import java.io.IOException;
import java.nio.file.Path;

@HandleBy(reference = "net/minecraft/world/level/chunk/storage/RegionFileStorage", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefRegionFileStorage {
    @HandleBy(reference = "Lnet/minecraft/world/level/chunk/storage/RegionFileStorage;<init>(Ljava/nio/file/Path;ZZ)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    RefRegionFileStorage(Path directory, boolean dsync, boolean isChunkData) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/level/chunk/storage/RegionFileStorage;read(Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefNbtTagCompound read(RefChunkPos pos) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/world/level/chunk/storage/RegionFileStorage;read(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/storage/RegionFile;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefNbtTagCompound read(RefChunkPos pos, RefRegionFile regionfile) throws IOException;

    public native static RefChunkPos getRegionFileCoordinates(Path file);

    public native RefRegionFile getRegionFile(RefChunkPos chunkcoordintpair, boolean existingOnly) throws IOException;
}
