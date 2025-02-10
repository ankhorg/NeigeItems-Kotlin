package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.level;

import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.world.level.RefChunkPos;
import pers.neige.neigeitems.ref.world.level.chunk.storage.RefRegionFile;
import pers.neige.neigeitems.ref.world.level.chunk.storage.RefRegionFileStorage;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class RegionFileStorage {
    private final RefRegionFileStorage delegate;

    public RegionFileStorage(Path directory) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<RefRegionFileStorage> constructor = RefRegionFileStorage.class.getDeclaredConstructor(Path.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        this.delegate = constructor.newInstance(directory, true, false);
    }

    public NbtCompound read(int x, int z) throws IOException {
        RefNbtTagCompound nbt = delegate.read(new RefChunkPos(x << 5, z << 5));
        return nbt == null ? null : NbtCompound.Unsafe.of(nbt);
    }

    public RegionFile getRegionFile(Path path) throws IOException {
        RefRegionFile temp = delegate.getRegionFile(RefRegionFileStorage.getRegionFileCoordinates(path), true);
        return temp == null ? null : new RegionFile(temp);
    }
}
