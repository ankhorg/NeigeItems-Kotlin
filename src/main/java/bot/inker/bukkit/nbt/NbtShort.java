package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtShortLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagShort;

public final class NbtShort extends NbtNumeric<RefNbtTagShort> implements NbtShortLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtShort[] instanceCache = buildInstanceCache();

  NbtShort(RefNbtTagShort delegate) {
    super(delegate);
  }

  private static bot.inker.bukkit.nbt.NbtShort[] buildInstanceCache() {
    bot.inker.bukkit.nbt.NbtShort[] result = new bot.inker.bukkit.nbt.NbtShort[1153];
    for (int i = 0; i < result.length; i++) {
      result[i] = new bot.inker.bukkit.nbt.NbtShort(OF_SUPPORTED
          ? RefNbtTagShort.of((short) (i - 128))
          : new RefNbtTagShort((short) (i - 128)));
    }
    return result;
  }

  public static bot.inker.bukkit.nbt.NbtShort valueOf(short value) {
    return (value >= -128 && value <= 1024)
        ? instanceCache[value + 128]
        : new bot.inker.bukkit.nbt.NbtShort(OF_SUPPORTED
        ? RefNbtTagShort.of(value)
        : new RefNbtTagShort(value));
  }

  static bot.inker.bukkit.nbt.NbtShort fromNmsImpl(RefNbtTagShort delegate) {
    short value = delegate.asShort();
    return (value >= -128 && value <= 1024) ? instanceCache[value + 128] : new bot.inker.bukkit.nbt.NbtShort(delegate);
  }

  @Override
  public bot.inker.bukkit.nbt.NbtShort clone() {
    return this;
  }
}
