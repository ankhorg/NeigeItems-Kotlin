package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.hook.mythicmobs.impl.LegacyMythicMobsHookerImpl
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import pers.neige.neigeitems.hook.nashorn.impl.LegacyNashornHookerImpl
import pers.neige.neigeitems.hook.nashorn.impl.NashornHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker
import pers.neige.neigeitems.hook.placeholderapi.impl.LegacyPapiHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.impl.PapiHookerImpl
import pers.neige.neigeitems.hook.vault.VaultHooker
import pers.neige.neigeitems.hook.vault.impl.VaultHookerImpl
import pers.neige.neigeitems.manager.ConfigManager.config

object HookerManager {
    val nashornHooker: NashornHooker =
        try {
            LegacyNashornHookerImpl()
        } catch (error: Throwable) {
            NashornHookerImpl()
        }

    val mythicMobsHooker: MythicMobsHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            try {
                LegacyMythicMobsHookerImpl()
            } catch (error: Throwable) {
                MythicMobsHookerImpl()
            }
        } else {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
            null
        }

    // papi中间有一些兼容版本存在, 先构建新版的Hooker实现, 解析效率更高
    val papiHooker: PapiHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                PapiHookerImpl()
            } catch (error: Throwable) {
                LegacyPapiHookerImpl()
            }
        } else {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "PlaceholderAPI"))
            null
        }

    val vaultHooker: VaultHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            try {
                VaultHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "Vault"))
            null
        }

    @JvmStatic
    fun papi(player: OfflinePlayer, string: String): String {
        return when (papiHooker) {
            null -> string
            else -> papiHooker.papi(player, string)
        }
    }

    @JvmStatic
    fun papiColor(player: OfflinePlayer, string: String): String {
        return when (papiHooker) {
            null -> ChatColor.translateAlternateColorCodes('&', string)
            else -> papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string))
        }
    }
}