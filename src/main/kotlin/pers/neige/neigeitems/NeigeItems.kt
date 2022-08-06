package pers.neige.neigeitems

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin

object NeigeItems : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }

    val bukkitScheduler by lazy { Bukkit.getScheduler() }

    val pluginManager by lazy { Bukkit.getPluginManager() }
}