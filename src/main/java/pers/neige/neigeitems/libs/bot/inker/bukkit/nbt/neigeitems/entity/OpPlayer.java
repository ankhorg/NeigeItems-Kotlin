package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.neige.neigeitems.ref.entity.RefCraftPlayer;
import pers.neige.neigeitems.ref.world.RefCraftServer;

public class OpPlayer extends RefCraftPlayer {
    public OpPlayer(Player player) {
        super((RefCraftServer) (Object) Bukkit.getServer(), ((RefCraftPlayer) player).getHandle());
    }

    @Override
    public boolean hasPermission(String name) {
        return true;
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
    }
}
