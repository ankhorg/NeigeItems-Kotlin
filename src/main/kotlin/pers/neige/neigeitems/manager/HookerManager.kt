package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
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
import pers.neige.neigeitems.item.ItemPlaceholder
import pers.neige.neigeitems.manager.ConfigManager.config
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object HookerManager {
    val nashornHooker: NashornHooker =
        try {
            LegacyNashornHookerImpl()
        } catch (error: Throwable) {
            NashornHookerImpl()
        }

    // 某些情况下 MythicMobs 的 ItemManager 加载顺序很奇怪，因此写成 by lazy, 然后在 active 阶段主动调用
    val mythicMobsHooker: MythicMobsHooker? by lazy {
        try {
            try {
                LegacyMythicMobsHookerImpl()
            } catch (error: Throwable) {
                MythicMobsHookerImpl()
            }
        } catch (error: Throwable) {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
            null
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun loadMythicMobsHooker() {
        mythicMobsHooker
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

    val itemPlaceholder: ItemPlaceholder? = if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
        try {
            ItemPlaceholder()
        } catch (error: Throwable) {
            null
        }
    } else {
        Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "ProtocolLib"))
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

    @JvmStatic
    fun parseItemPlaceholder(itemStack: ItemStack, string: String): String {
        return when (itemPlaceholder) {
            null -> string
            else -> itemPlaceholder.parse(itemStack, string)
        }
    }
}