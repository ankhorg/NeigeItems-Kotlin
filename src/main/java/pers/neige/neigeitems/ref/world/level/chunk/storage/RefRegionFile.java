package pers.neige.neigeitems.ref.world.level.chunk.storage;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.world.level.RefChunkPos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

@HandleBy(reference = "net/minecraft/world/level/chunk/storage/RegionFile", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefRegionFile {
    @HandleBy(reference = "Lnet/minecraft/world/level/chunk/storage/RegionFile;<init>(Ljava/nio/file/Path;Ljava/nio/file/Path;Z)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefRegionFile(Path file, Path directory, boolean dsync) throws IOException {
        throw new UnsupportedEncodingException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/level/chunk/storage/RegionFile;getChunkDataInputStream(Lnet/minecraft/world/level/ChunkPos;)Ljava/io/DataInputStream;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native DataInputStream getChunkDataInputStream(RefChunkPos pos) throws IOException;
}
