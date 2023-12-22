package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api;

public interface NbtLike {
  byte getId();

  String getAsString();

  NbtLike clone();
}
