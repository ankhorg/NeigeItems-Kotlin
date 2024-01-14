package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api;

import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;

public interface NbtListLike extends NbtCollectionLike<Nbt<?>> {
    NbtListLike clone();
}
