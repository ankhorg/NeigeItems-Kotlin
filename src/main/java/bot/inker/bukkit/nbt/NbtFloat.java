package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtFloatLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagFloat;

public final class NbtFloat extends NbtNumeric<RefNbtTagFloat> implements NbtFloatLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtFloat ZERO = new bot.inker.bukkit.nbt.NbtFloat(OF_SUPPORTED
      ? RefNbtTagFloat.of(0.0F)
      : new RefNbtTagFloat(0.0F)
  );

  NbtFloat(RefNbtTagFloat delegate) {
    super(delegate);
  }

  public static bot.inker.bukkit.nbt.NbtFloat valueOf(float value) {
    return value == 0.0F ? ZERO : new bot.inker.bukkit.nbt.NbtFloat(OF_SUPPORTED
        ? RefNbtTagFloat.of(value)
        : new RefNbtTagFloat(value));
  }

  static bot.inker.bukkit.nbt.NbtFloat fromNmsImpl(RefNbtTagFloat delegate) {
    return delegate.asFloat() == 0.0F ? ZERO : new bot.inker.bukkit.nbt.NbtFloat(delegate);
  }

  @Override
  public bot.inker.bukkit.nbt.NbtFloat clone() {
    return this;
  }
}
