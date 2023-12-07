package bot.inker.bukkit.nbt;

import pers.neige.neigeitems.ref.nbt.RefNbtTagByteArray;

import java.util.List;

public final class NbtByteArray extends NbtCollection<RefNbtTagByteArray, Byte> {
  NbtByteArray(RefNbtTagByteArray delegate) {
    super(delegate);
  }

  public NbtByteArray(byte[] value) {
    super(new RefNbtTagByteArray(value));
  }

  public NbtByteArray(List<Byte> value) {
    super(new RefNbtTagByteArray(value));
  }

  @Override
  public Byte get(int index) {
    return delegate.getBytes()[index];
  }

  public byte getByte(int index) {
    return delegate.getBytes()[index];
  }

  @Override
  public int size() {
    return delegate.getBytes().length;
  }

  @Override
  public Byte set(int index, Byte element) {
    throw new UnsupportedOperationException("NbtByteArray is immutable");
  }

  @Override
  public void add(int index, Byte element) {
    throw new UnsupportedOperationException("NbtByteArray is immutable");
  }

  @Override
  public Byte remove(int index) {
    throw new UnsupportedOperationException("NbtByteArray is immutable");
  }

  public byte[] getAsByteArray() {
    return delegate.getBytes();
  }

  @Override
  public bot.inker.bukkit.nbt.NbtByteArray clone() {
    return new bot.inker.bukkit.nbt.NbtByteArray(cloneNms());
  }
}
