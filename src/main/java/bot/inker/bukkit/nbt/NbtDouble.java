package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtDoubleLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagDouble;

public final class NbtDouble extends NbtNumeric<RefNbtTagDouble> implements NbtDoubleLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtDouble ZERO = new bot.inker.bukkit.nbt.NbtDouble(OF_SUPPORTED
      ? RefNbtTagDouble.of(0.0)
      : new RefNbtTagDouble(0.0));

  NbtDouble(RefNbtTagDouble delegate) {
    super(delegate);
  }

  public static bot.inker.bukkit.nbt.NbtDouble valueOf(double value) {
    return value == 0.0 ? ZERO : new bot.inker.bukkit.nbt.NbtDouble(OF_SUPPORTED
        ? RefNbtTagDouble.of(value)
        : new RefNbtTagDouble(value));
  }

  static bot.inker.bukkit.nbt.NbtDouble fromNmsImpl(RefNbtTagDouble delegate) {
    return delegate.asDouble() == 0.0 ? ZERO : new bot.inker.bukkit.nbt.NbtDouble(delegate);
  }

  @Override
  public bot.inker.bukkit.nbt.NbtDouble clone() {
    return this;
  }
}
