package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtByteLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagByte;

public final class NbtByte extends NbtNumeric<RefNbtTagByte> implements NbtByteLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtByte[] instanceCache = buildInstanceCache();
  private static final bot.inker.bukkit.nbt.NbtByte falseInstance = valueOf((byte) 0);
  private static final bot.inker.bukkit.nbt.NbtByte trueInstance = valueOf((byte) 1);

  private NbtByte(RefNbtTagByte delegate) {
    super(delegate);
  }

  private static bot.inker.bukkit.nbt.NbtByte[] buildInstanceCache() {
    bot.inker.bukkit.nbt.NbtByte[] result = new bot.inker.bukkit.nbt.NbtByte[256];
    for (int i = 0; i < result.length; i++) {
      result[i] = OF_SUPPORTED
          ? new bot.inker.bukkit.nbt.NbtByte(RefNbtTagByte.of((byte) (i - 128)))
          : new bot.inker.bukkit.nbt.NbtByte(new RefNbtTagByte((byte) (i - 128)));
    }
    return result;
  }

  public static bot.inker.bukkit.nbt.NbtByte valueOf(byte value) {
    return instanceCache[value + 128];
  }

  static bot.inker.bukkit.nbt.NbtByte fromNmsImpl(RefNbtTagByte delegate) {
    return instanceCache[delegate.asByte() + 128];
  }

  public static bot.inker.bukkit.nbt.NbtByte valueOf(boolean value) {
    return value ? trueInstance : falseInstance;
  }

  public boolean getAsBoolean() {
    return getAsByte() != 0;
  }

  @Override
  public bot.inker.bukkit.nbt.NbtByte clone() {
    return this;
  }
}
