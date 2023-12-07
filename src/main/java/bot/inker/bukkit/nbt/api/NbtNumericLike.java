package bot.inker.bukkit.nbt.api;

import bot.inker.bukkit.nbt.api.NbtLike;

public interface NbtNumericLike extends NbtLike {
  long getAsLong();

  int getAsInt();

  short getAsShort();

  byte getAsByte();

  double getAsDouble();

  float getAsFloat();

  Number getAsNumber();

  bot.inker.bukkit.nbt.api.NbtNumericLike clone();
}
