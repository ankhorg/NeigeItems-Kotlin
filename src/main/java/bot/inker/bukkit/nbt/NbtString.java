package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtStringLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagString;

public final class NbtString extends Nbt<RefNbtTagString> implements NbtStringLike {
  private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
  private static final bot.inker.bukkit.nbt.NbtString EMPTY = new bot.inker.bukkit.nbt.NbtString(OF_SUPPORTED
      ? RefNbtTagString.of("")
      : new RefNbtTagString(""));

  NbtString(RefNbtTagString delegate) {
    super(delegate);
  }

  public static bot.inker.bukkit.nbt.NbtString valueOf(String value) {
    return value.isEmpty()
        ? EMPTY
        : new bot.inker.bukkit.nbt.NbtString(OF_SUPPORTED
        ? RefNbtTagString.of(value)
        : new RefNbtTagString(value));
  }

  static bot.inker.bukkit.nbt.NbtString fromNmsImpl(RefNbtTagString delegate) {
    return delegate.asString().isEmpty() ? EMPTY : new bot.inker.bukkit.nbt.NbtString(delegate);
  }

  public String getAsString() {
    return delegate.asString();
  }

  @Override
  public bot.inker.bukkit.nbt.NbtString clone() {
    return this;
  }
}
