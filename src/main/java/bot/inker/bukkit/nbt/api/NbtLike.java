package bot.inker.bukkit.nbt.api;

public interface NbtLike {
  byte getId();

  String getAsString();

  bot.inker.bukkit.nbt.api.NbtLike clone();
}
