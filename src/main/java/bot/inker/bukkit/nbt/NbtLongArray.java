package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagLongArray;

import java.util.List;

public final class NbtLongArray extends NbtCollection<RefNbtTagLongArray, Long> {
  private static final boolean DIRECT_ACCESS_SUPPORT = CbVersion.v1_13_R1.isSupport();

  NbtLongArray(RefNbtTagLongArray delegate) {
    super(delegate);
  }

  public NbtLongArray(long[] value) {
    super(new RefNbtTagLongArray(value));
  }

  public NbtLongArray(List<Long> value) {
    super(new RefNbtTagLongArray(value));
  }

  @Override
  public Long get(int index) {
    return getAsLongArray()[index];
  }

  public long getLong(int index) {
    return getAsLongArray()[index];
  }

  @Override
  public int size() {
    return getAsLongArray().length;
  }

  @Override
  public Long set(int index, Long element) {
    throw new UnsupportedOperationException("NbtLongArray is immutable");
  }

  @Override
  public void add(int index, Long element) {
    throw new UnsupportedOperationException("NbtLongArray is immutable");
  }

  @Override
  public Long remove(int index) {
    throw new UnsupportedOperationException("NbtLongArray is immutable");
  }

  public long[] getAsLongArray() {
    if (DIRECT_ACCESS_SUPPORT) {
      return delegate.getLongs();
    } else {
      return delegate.longs;
    }
  }

  @Override
  public bot.inker.bukkit.nbt.NbtLongArray clone() {
    return new bot.inker.bukkit.nbt.NbtLongArray(cloneNms());
  }
}
