package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import pers.neige.neigeitems.ref.nbt.RefNbtTagIntArray;

import java.util.List;

public final class NbtIntArray extends NbtCollection<RefNbtTagIntArray, Integer> {
  NbtIntArray(RefNbtTagIntArray delegate) {
    super(delegate);
  }

  public NbtIntArray(int[] value) {
    super(new RefNbtTagIntArray(value));
  }

  public NbtIntArray(List<Integer> value) {
    super(new RefNbtTagIntArray(value));
  }

  @Override
  public Integer get(int index) {
    return delegate.getInts()[index];
  }

  public int getInt(int index) {
    return delegate.getInts()[index];
  }

  @Override
  public int size() {
    return delegate.getInts().length;
  }

  @Override
  public Integer set(int index, Integer element) {
    throw new UnsupportedOperationException("NbtIntArray is immutable");
  }

  @Override
  public void add(int index, Integer element) {
    throw new UnsupportedOperationException("NbtIntArray is immutable");
  }

  @Override
  public Integer remove(int index) {
    throw new UnsupportedOperationException("NbtIntArray is immutable");
  }

  public int[] getAsIntArray() {
    return delegate.getInts();
  }

  @Override
  public NbtIntArray clone() {
    return new NbtIntArray(cloneNms());
  }
}
