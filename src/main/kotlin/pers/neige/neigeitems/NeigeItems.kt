package pers.neige.neigeitems

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin

/**
 * 插件主类
 */
object NeigeItems : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }

    val bukkitScheduler by lazy { Bukkit.getScheduler() }
}