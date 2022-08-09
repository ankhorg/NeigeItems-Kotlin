package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl459
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl490
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl502
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl510
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
import java.util.function.BiFunction

object HookerManager {
    val nashornHooker: NashornHooker =
        try {
            // jdk自带nashorn
            LegacyNashornHookerImpl()
        } catch (error: Throwable) {
            // 主动下载nashorn
            NashornHookerImpl()
        }

    // 某些情况下 MythicMobs 的 ItemManager 加载顺序很奇怪，因此写成 by lazy, 然后在 active 阶段主动调用
    // 没事儿改包名很爽吗, 写MM的, 你妈死了
    val mythicMobsHooker: MythicMobsHooker? by lazy {
        try {
            try {
                // 5.0.3+
                MythicMobsHookerImpl510()
            } catch (error: Throwable) {
                try {
                    // 5.0.3-
                    MythicMobsHookerImpl502()
                } catch (error: Throwable) {
                    try {
                        // 5.0.0-
                        MythicMobsHookerImpl490()
                    } catch (error: Throwable) {
                        // 4.7.2-
                        MythicMobsHookerImpl459()
                    }
                }
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

    // 解析papi变量, 不解析颜色代码
    @JvmStatic
    fun papi(player: OfflinePlayer, string: String): String {
        return when (papiHooker) {
            null -> string
            else -> papiHooker.papi(player, string)
        }
    }

    // 解析papi变量的同时解析颜色代码
    @JvmStatic
    fun papiColor(player: OfflinePlayer, string: String): String {
        return when (papiHooker) {
            null -> ChatColor.translateAlternateColorCodes('&', string)
            else -> papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string))
        }
    }

    // 解析物品变量
    @JvmStatic
    fun parseItemPlaceholder(itemStack: ItemStack, string: String): String {
        return when (itemPlaceholder) {
            null -> string
            else -> itemPlaceholder.parse(itemStack, string)
        }
    }

    // 解析物品名和物品Lore中的物品变量
    @JvmStatic
    fun parseItemPlaceholders(itemStack: ItemStack) {
        itemPlaceholder?.let { itemPlaceholder.itemParse(itemStack) }
    }

    // 添加物品变量附属
    @JvmStatic
    fun addItemPlaceholderExpansion(id: String, function: BiFunction<ItemStack, String, String?>) {
        itemPlaceholder?.let { itemPlaceholder.addExpansion(id, function) }
    }
}