package bot.inker.bukkit.nbt.api;

import bot.inker.bukkit.nbt.Nbt;
import bot.inker.bukkit.nbt.api.NbtCollectionLike;

public interface NbtListLike extends NbtCollectionLike<Nbt<?>> {
  bot.inker.bukkit.nbt.api.NbtListLike clone();
}
