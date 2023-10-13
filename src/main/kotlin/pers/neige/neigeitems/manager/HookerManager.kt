package pers.neige.neigeitems.manager

import bot.inker.bukkit.nbt.internal.annotation.CbVersion
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.annotations.Awake
import pers.neige.neigeitems.hook.easyitem.EasyItemHooker
import pers.neige.neigeitems.hook.easyitem.impl.EasyItemHookerImpl
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import pers.neige.neigeitems.hook.nashorn.impl.LegacyNashornHookerImpl
import pers.neige.neigeitems.hook.nashorn.impl.NashornHookerImpl
import pers.neige.neigeitems.hook.nms.NMSHooker
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker
import pers.neige.neigeitems.hook.placeholderapi.impl.LegacyPapiHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.impl.PapiHookerImpl
import pers.neige.neigeitems.hook.vault.VaultHooker
import pers.neige.neigeitems.hook.vault.impl.VaultHookerImpl
import pers.neige.neigeitems.item.ItemHider
import pers.neige.neigeitems.item.ItemPlaceholder
import pers.neige.neigeitems.item.color.ItemColor
import pers.neige.neigeitems.item.color.impl.ItemColorProtocol
import pers.neige.neigeitems.item.color.impl.ItemColorVanilla
import pers.neige.neigeitems.manager.ConfigManager.config
import taboolib.module.nms.getName
import java.util.*
import java.util.function.BiFunction

/**
 * 插件兼容管理器, 用于尝试与各个软依赖插件取得联系
 */
object HookerManager {
    private fun check(clazz: String): Boolean {
        return try {
            Class.forName(clazz)
            true
        } catch (error: Throwable) {
            false
        }
    }

    val nashornHooker: NashornHooker =
        when {
            // jdk自带nashorn
            check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") -> LegacyNashornHookerImpl()
            // 主动下载nashorn
            else -> NashornHookerImpl()
        }

    // 某些情况下 MythicMobs 的 ItemManager 加载顺序很奇怪，因此写成 by lazy, 然后在 active 阶段主动调用
    // 没事儿改包名很爽吗, 写MM的, 你妈死了
    val mythicMobsHooker: MythicMobsHooker? by lazy {
        try {
            try {
                // 5.0.3+
                Class.forName("io.lumine.mythic.bukkit.utils.config.file.YamlConfiguration")
                Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl510").newInstance() as MythicMobsHooker
            } catch (error: Throwable) {
                try {
                    // 5.0.3-
                    Class.forName("io.lumine.mythic.utils.config.file.YamlConfiguration")
                    Class.forName("io.lumine.mythic.bukkit.MythicBukkit")
                    Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl502").newInstance() as MythicMobsHooker
                } catch (error: Throwable) {
                    try {
                        // 5.0.0-
                        Class.forName("io.lumine.xikage.mythicmobs.utils.config.file.YamlConfiguration")
                        Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl490").newInstance() as MythicMobsHooker
                    } catch (error: Throwable) {
                        // 4.7.2-
                        Class.forName("io.lumine.utils.config.file.YamlConfiguration")
                        Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl459").newInstance() as MythicMobsHooker
                    }
                }
            }
        } catch (error: Throwable) {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
            null
        }
    }

    val nmsHooker: NMSHooker =
        try {
            when {
                CbVersion.current() == CbVersion.v1_12_R1 -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerNamespacedKey").newInstance() as NMSHooker
                CbVersion.current().ordinal < CbVersion.v1_14_R1.ordinal -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerCustomModelData").newInstance() as NMSHooker
                CbVersion.current().ordinal < CbVersion.v1_16_R2.ordinal -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerHoverEvent").newInstance() as NMSHooker
                else -> NMSHooker()
            }
        } catch (error: Throwable) {
            NMSHooker()
        }

    /**
     * 加载MM挂钩功能
     */
    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ACTIVE)
    private fun loadMythicMobsHooker() {
        mythicMobsHooker
    }

    // papi中间有一些兼容版本存在, 先构建新版的Hooker实现, 解析效率更高
    val papiHooker: PapiHooker? =
        try {
            PapiHookerImpl()
        } catch (error: Throwable) {
            try {
                LegacyPapiHookerImpl()
            } catch (error: Throwable) {
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    error.printStackTrace()
                } else {
                    Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "PlaceholderAPI"))
                }
                null
            }
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
    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ACTIVE)
    private fun loadEasyItemHooker() {
        easyItemHooker
    }

    /**
     * 物品变量功能
     */
    val itemPlaceholder: ItemPlaceholder?

    /**
     * 物品隐藏功能
     */
    val itemHider: ItemHider?

    /**
     * 物品光效功能
     */
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

    init {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            itemPlaceholder = try {
                ItemPlaceholder()
            } catch (error: Throwable) {
                null
            }
            itemHider = try {
                ItemHider()
            } catch (error: Throwable) {
                null
            }
        } else {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "ProtocolLib"))
            itemPlaceholder = null
            itemHider = null
        }
    }

    /**
     * 掉落物光效功能
     */
    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ACTIVE)
    private fun loadItemColor() {
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
     * 判断文本中是否存在有效papi变量
     *
     * @param text 待检测文本
     * @return 是否存在有效papi变量
     */
    @JvmStatic
    fun hasPapi(text: String): Boolean {
        return when (papiHooker) {
            null -> false
            else -> papiHooker.hasPapi(text)
        }
    }

    /**
     * 将文本中的所有papi变量改写为papi节点
     *
     * @param text 待转换文本
     * @return 转换后文本
     */
    @JvmStatic
    fun toSection(text: String): String {
        return when (papiHooker) {
            null -> text
            else -> papiHooker.toSection(text)
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
     * 获取已解析物品变量的物品名
     *
     * @return 解析后文本
     */
    @JvmStatic
    fun ItemStack.getParsedName(): String {
        return when (itemPlaceholder) {
            null -> getName()
            else -> itemPlaceholder.parse(this, getName())
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

    @JvmStatic
    fun ComponentBuilder.hoverText(text: String): ComponentBuilder {
        return event(nmsHooker.hoverText(text))
    }

    @JvmStatic
    fun ComponentBuilder.hoverItem(itemStack: ItemStack): ComponentBuilder {
        return event(nmsHooker.hoverItem(itemStack))
    }

    @JvmStatic
    fun ComponentBuilder.runCommand(text: String): ComponentBuilder {
        return event(ClickEvent(ClickEvent.Action.RUN_COMMAND, text))
    }

    @JvmStatic
    fun ComponentBuilder.suggestCommand(text: String): ComponentBuilder {
        return event(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, text))
    }

    @JvmStatic
    fun ComponentBuilder.append(builder: ComponentBuilder): ComponentBuilder {
        return append(builder.create())
    }
}