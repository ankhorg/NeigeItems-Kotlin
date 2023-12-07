package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtLike;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.ref.nbt.*;

public abstract class Nbt<NMS extends RefNbtBase> implements NbtLike {
  final NMS delegate;

  Nbt(NMS delegate) {
    this.delegate = delegate;
  }

  @SuppressWarnings("unchecked")
  static @Nullable <T extends RefNbtBase> bot.inker.bukkit.nbt.Nbt<T> fromNms(@Nullable T source) {
    if (source == null) {
      return null;
    } else if (source instanceof RefNbtTagByte) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtByte.fromNmsImpl((RefNbtTagByte) source);
    } else if (source instanceof RefNbtTagByteArray) {
      return (bot.inker.bukkit.nbt.Nbt<T>) new NbtByteArray((RefNbtTagByteArray) source);
    } else if (source instanceof RefNbtTagCompound) {
      return (bot.inker.bukkit.nbt.Nbt<T>) new NbtCompound((RefNbtTagCompound) source);
    } else if (source instanceof RefNbtTagDouble) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtDouble.fromNmsImpl((RefNbtTagDouble) source);
    } else if (source instanceof RefNbtTagEnd) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtEnd.INSTANCE;
    } else if (source instanceof RefNbtTagFloat) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtFloat.fromNmsImpl((RefNbtTagFloat) source);
    } else if (source instanceof RefNbtTagInt) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtInt.fromNmsImpl((RefNbtTagInt) source);
    } else if (source instanceof RefNbtTagIntArray) {
      return (bot.inker.bukkit.nbt.Nbt<T>) new NbtIntArray((RefNbtTagIntArray) source);
    } else if (source instanceof RefNbtTagList) {
      return (bot.inker.bukkit.nbt.Nbt<T>) new NbtList((RefNbtTagList) source);
    } else if (source instanceof RefNbtTagLong) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtLong.fromNmsImpl((RefNbtTagLong) source);
    } else if (source instanceof RefNbtTagLongArray) {
      return (bot.inker.bukkit.nbt.Nbt<T>) new NbtLongArray((RefNbtTagLongArray) source);
    } else if (source instanceof RefNbtTagShort) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtShort.fromNmsImpl((RefNbtTagShort) source);
    } else if (source instanceof RefNbtTagString) {
      return (bot.inker.bukkit.nbt.Nbt<T>) NbtString.fromNmsImpl((RefNbtTagString) source);
    } else {
      throw new UnsupportedOperationException("unknown source: " + source.getClass());
    }
  }

  @Override
  public byte getId() {
    return delegate.getTypeId();
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof bot.inker.bukkit.nbt.Nbt && delegate.equals(((bot.inker.bukkit.nbt.Nbt<?>) o).delegate);
  }

  @Override
  public abstract bot.inker.bukkit.nbt.Nbt<NMS> clone();

  @SuppressWarnings("unchecked")
  protected NMS cloneNms() {
    return (NMS) delegate.rClone();
  }

  @Override
  public String getAsString() {
    return delegate.asString();
  }
}
