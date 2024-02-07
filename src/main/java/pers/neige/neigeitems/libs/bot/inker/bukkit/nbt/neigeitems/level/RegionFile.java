package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.level;

import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.ref.world.level.RefChunkPos;
import pers.neige.neigeitems.ref.world.level.chunk.storage.RefRegionFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class RegionFile {
    final RefRegionFile delegate;

    public RegionFile(RefRegionFile delegate) {
        this.delegate = delegate;
    }

    public RegionFile(File file) throws IOException {
        Path path = file.toPath();
        this.delegate = new RefRegionFile(path, path.getParent(), true);
    }

    public RegionFile(Path file) throws IOException {
        this.delegate = new RefRegionFile(file, file.getParent(), true);
    }

    public RegionFile(Path file, Path directory) throws IOException {
        this.delegate = new RefRegionFile(file, directory, true);
    }

    public RegionFile(Path file, Path directory, boolean dsync) throws IOException {
        this.delegate = new RefRegionFile(file, directory, dsync);
    }

    public NbtCompound read(int x, int z) throws IOException {
        DataInputStream stream = delegate.getChunkDataInputStream(new RefChunkPos(x, z));
        return stream == null ? null : NbtUtils.read(stream);
    }
}
