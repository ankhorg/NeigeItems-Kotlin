package bot.inker.bukkit.nbt.api;

import bot.inker.bukkit.nbt.api.NbtLike;

import java.util.List;

public interface NbtCollectionLike<E> extends NbtLike, List<E> {

  bot.inker.bukkit.nbt.api.NbtCollectionLike<E> clone();
}
