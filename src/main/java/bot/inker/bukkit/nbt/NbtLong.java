package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtLongLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagLong;

public final class NbtLong extends NbtNumeric<RefNbtTagLong> implements NbtLongLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtLong[] instanceCache = buildInstanceCache();

  NbtLong(RefNbtTagLong delegate) {
    super(delegate);
  }

  private static bot.inker.bukkit.nbt.NbtLong[] buildInstanceCache() {
    bot.inker.bukkit.nbt.NbtLong[] result = new bot.inker.bukkit.nbt.NbtLong[1153];
    for (int i = 0; i < result.length; i++) {
      result[i] = new bot.inker.bukkit.nbt.NbtLong(OF_SUPPORTED
          ? RefNbtTagLong.of(i - 128)
          : new RefNbtTagLong(i - 128));
    }
    return result;
  }

  public static bot.inker.bukkit.nbt.NbtLong valueOf(long value) {
    return (value >= -128 && value <= 1024)
        ? instanceCache[(int) value + 128]
        : new bot.inker.bukkit.nbt.NbtLong(OF_SUPPORTED
        ? RefNbtTagLong.of(value)
        : new RefNbtTagLong(value));
  }

  static bot.inker.bukkit.nbt.NbtLong fromNmsImpl(RefNbtTagLong delegate) {
    long value = delegate.asLong();
    return (value >= -128 && value <= 1024) ? instanceCache[(int) value + 128] : new bot.inker.bukkit.nbt.NbtLong(delegate);
  }

  @Override
  public bot.inker.bukkit.nbt.NbtLong clone() {
    return this;
  }
}
