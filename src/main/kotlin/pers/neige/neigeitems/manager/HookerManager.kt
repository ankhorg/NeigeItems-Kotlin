package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.easyitem.EasyItemHooker
import pers.neige.neigeitems.hook.easyitem.impl.EasyItemHookerImpl
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
import pers.neige.neigeitems.item.color.ItemColor
import pers.neige.neigeitems.item.color.impl.ItemColorProtocol
import pers.neige.neigeitems.item.color.impl.ItemColorVanilla
import pers.neige.neigeitems.manager.ConfigManager.config
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*
import java.util.function.BiFunction

object HookerManager {
    val nashornHooker: NashornHooker =
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
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

    /**
     * 加载MM挂钩功能
     */
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

    val easyItemHooker: EasyItemHooker?  by lazy {
        if (Bukkit.getPluginManager().isPluginEnabled("EasyItem")) {
            try {
                EasyItemHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "EasyItem"))
            null
        }
    }

    /**
     * 加载EI挂钩功能
     */
    @Awake(LifeCycle.ACTIVE)
    fun loadEasyItemHooker() {
        easyItemHooker
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

    val itemColor: ItemColor? by lazy {
        if (config.getString("ItemColor.type")?.lowercase(Locale.getDefault()) == "protocol") {
            if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                try {
                    ItemColorProtocol()
                } catch (error: Throwable) {
                    ItemColorVanilla()
                }
            } else {
                ItemColorVanilla()
            }
        } else {
            ItemColorVanilla()
        }
    }

    /**
     * 掉落物光效功能
     */
    @Awake(LifeCycle.ACTIVE)
    fun loadItemColor() {
        itemColor
    }

    /**
     * 解析papi变量, 不解析颜色代码
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    @JvmStatic
    fun papi(player: OfflinePlayer, text: String): String {
        return when (papiHooker) {
            null -> text
            else -> papiHooker.papi(player, text)
        }
    }

    /**
     * 解析papi变量的同时解析颜色代码
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    @JvmStatic
    fun papiColor(player: OfflinePlayer, string: String): String {
        return when (papiHooker) {
            null -> ChatColor.translateAlternateColorCodes('&', string)
            else -> papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string))
        }
    }

    /**
     * 解析papi变量, 不解析颜色代码
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    @JvmStatic
    fun requestPapi(player: OfflinePlayer, identifier: String, parameters: String): String {
        return when (papiHooker) {
            null -> "%${identifier}_$parameters%"
            else -> papiHooker.request(player, identifier, parameters)
        }
    }

    /**
     * 解析物品变量
     *
     * @param itemStack 用于解析变量的物品
     * @param text 待解析文本
     * @return 解析后文本
     */
    @JvmStatic
    fun parseItemPlaceholder(itemStack: ItemStack, text: String): String {
        return when (itemPlaceholder) {
            null -> text
            else -> itemPlaceholder.parse(itemStack, text)
        }
    }

    /**
     * 解析物品名和物品Lore中的物品变量
     *
     * @param itemStack 待解析物品
     */
    @JvmStatic
    fun parseItemPlaceholders(itemStack: ItemStack) {
        itemPlaceholder?.let { itemPlaceholder.itemParse(itemStack) }
    }

    /**
     * 添加物品变量附属
     *
     * @param id 变量ID
     * @param function 操作函数
     */
    @JvmStatic
    fun addItemPlaceholderExpansion(id: String, function: BiFunction<ItemStack, String, String?>) {
        itemPlaceholder?.let { itemPlaceholder.addExpansion(id, function) }
    }
}