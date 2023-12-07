package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtIntLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagInt;

public final class NbtInt extends NbtNumeric<RefNbtTagInt> implements NbtIntLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtInt[] instanceCache = buildInstanceCache();

  NbtInt(RefNbtTagInt delegate) {
    super(delegate);
  }

  private static bot.inker.bukkit.nbt.NbtInt[] buildInstanceCache() {
    bot.inker.bukkit.nbt.NbtInt[] result = new bot.inker.bukkit.nbt.NbtInt[1153];
    for (int i = 0; i < result.length; i++) {
      result[i] = new bot.inker.bukkit.nbt.NbtInt(OF_SUPPORTED
          ? RefNbtTagInt.of(i - 128)
          : new RefNbtTagInt(i - 128));
    }
    return result;
  }

  public static bot.inker.bukkit.nbt.NbtInt valueOf(int value) {
    return (value >= -128 && value <= 1024)
        ? instanceCache[value + 128]
        : new bot.inker.bukkit.nbt.NbtInt(OF_SUPPORTED
        ? RefNbtTagInt.of(value)
        : new RefNbtTagInt(value));
  }

  static bot.inker.bukkit.nbt.NbtInt fromNmsImpl(RefNbtTagInt delegate) {
    int value = delegate.asInt();
    return (value >= -128 && value <= 1024) ? instanceCache[value + 128] : new bot.inker.bukkit.nbt.NbtInt(delegate);
  }

  @Override
  public bot.inker.bukkit.nbt.NbtInt clone() {
    return this;
  }
}
