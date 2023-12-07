package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtEndLike;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtBase;
import pers.neige.neigeitems.ref.nbt.RefNbtTagEnd;

public final class NbtEnd extends Nbt<RefNbtTagEnd> implements NbtEndLike {
  private static final boolean INSTANCE_FIELD_SUPPORT = CbVersion.v1_15_R1.isSupport();

  static final bot.inker.bukkit.nbt.NbtEnd INSTANCE = INSTANCE_FIELD_SUPPORT
      ? new bot.inker.bukkit.nbt.NbtEnd(RefNbtTagEnd.INSTANCE)
      : new bot.inker.bukkit.nbt.NbtEnd((RefNbtTagEnd) RefNbtBase.createTag((byte) 0));

  private NbtEnd(RefNbtTagEnd delegate) {
    super(delegate);
  }

  public static bot.inker.bukkit.nbt.NbtEnd instance() {
    return INSTANCE;
  }

  @Override
  public Nbt<RefNbtTagEnd> clone() {
    return this;
  }
}
