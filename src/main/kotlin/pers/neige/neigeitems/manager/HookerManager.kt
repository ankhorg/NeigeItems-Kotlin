package pers.neige.neigeitems.manager

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventPriority
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.hook.itemsadder.ItemsAdderHooker
import pers.neige.neigeitems.hook.itemsadder.impl.ItemsAdderHookerImpl
import pers.neige.neigeitems.hook.magicgem.MagicGemHooker
import pers.neige.neigeitems.hook.magicgem.impl.MagicGemHookerImpl
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import pers.neige.neigeitems.hook.nashorn.impl.LegacyNashornHookerImpl
import pers.neige.neigeitems.hook.nashorn.impl.NashornHookerImpl
import pers.neige.neigeitems.hook.nms.NMSHooker
import pers.neige.neigeitems.hook.oraxen.OraxenHooker
import pers.neige.neigeitems.hook.oraxen.impl.OraxenHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker
import pers.neige.neigeitems.hook.placeholderapi.impl.LegacyPapiHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.impl.PapiHookerImpl
import pers.neige.neigeitems.hook.vault.VaultHooker
import pers.neige.neigeitems.hook.vault.impl.VaultHookerImpl
import pers.neige.neigeitems.item.ItemHider
import pers.neige.neigeitems.item.ItemPlaceholder
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils
import pers.neige.neigeitems.manager.ConfigManager.config
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
            // jdk11 以下使用 jdk 自带 nashorn
            check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") && ((System.getProperty("java.class.version")
                .toDoubleOrNull() ?: 0.0) < 55.0) -> LegacyNashornHookerImpl()
            // jdk11 以上使用 openjdk nashorn
            else -> NashornHookerImpl()
        }

    val nmsHooker: NMSHooker =
        try {
            when {
                CbVersion.current() == CbVersion.v1_12_R1 -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerNamespacedKey")
                    .newInstance() as NMSHooker

                CbVersion.current().ordinal < CbVersion.v1_14_R1.ordinal -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerCustomModelData")
                    .newInstance() as NMSHooker

                CbVersion.current().ordinal < CbVersion.v1_16_R2.ordinal -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerHoverEvent")
                    .newInstance() as NMSHooker

                CbVersion.current().ordinal >= CbVersion.v1_20_R4.ordinal -> Class.forName("pers.neige.neigeitems.hook.nms.impl.NMSHookerItemStack")
                    .newInstance() as NMSHooker

                else -> NMSHooker()
            }
        } catch (error: Throwable) {
            error.printStackTrace()
            NMSHooker()
        }

    var mythicMobsHooker: MythicMobsHooker? = null

    var papiHooker: PapiHooker? = null

    var vaultHooker: VaultHooker? = null

    var oraxenHooker: OraxenHooker? = null

    var magicGemHooker: MagicGemHooker? = null

    var itemsAdderHooker: ItemsAdderHooker? = null

    /**
     * 物品变量功能
     */
    var itemPlaceholder: ItemPlaceholder = ItemPlaceholder

    /**
     * 物品隐藏功能
     */
    var itemHider: ItemHider? = null

    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE, priority = EventPriority.LOW)
    private fun init0() {
        papiHooker =
            try {
                PapiHookerImpl()
            } catch (error: Throwable) {
                if (ConfigManager.debug) {
                    println("[NeigeItems] 新版papi兼容失败, 准备尝试兼容旧版papi, 报错如下:")
                    error.printStackTrace()
                }
                try {
                    LegacyPapiHookerImpl()
                } catch (error: Throwable) {
                    ConfigManager.debug("[NeigeItems] 旧版papi兼容失败, 报错如下:")
                    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                        error.printStackTrace()
                    } else {
                        if (ConfigManager.debug) {
                            error.printStackTrace()
                        }
                        Bukkit.getLogger()
                            .info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "PlaceholderAPI"))
                    }
                    null
                }
            }

        vaultHooker =
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                try {
                    VaultHookerImpl()
                } catch (error: Throwable) {
                    if (ConfigManager.debug) {
                        println("[NeigeItems] Vault兼容失败, 报错如下:")
                        error.printStackTrace()
                    }
                    null
                }
            } else {
                ConfigManager.debug("[NeigeItems] 未检测到Vault插件")
                Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "Vault"))
                null
            }

        oraxenHooker =
            try {
                Class.forName("io.th0rgal.oraxen.api.OraxenItems")
                OraxenHookerImpl()
            } catch (error: Throwable) {
                Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "Oraxen"))
                null
            }

        magicGemHooker =
            try {
                Class.forName("pku.yim.magicgem.MagicGem")
                MagicGemHookerImpl()
            } catch (error: Throwable) {
                Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MagicGem"))
                null
            }

        itemsAdderHooker =
            if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
                try {
                    ItemsAdderHookerImpl()
                } catch (error: Throwable) {
                    null
                }
            } else {
                Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "ItemsAdder"))
                null
            }

        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            itemHider = try {
                ItemHider()
            } catch (error: Throwable) {
                null
            }
        } else {
            Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "ProtocolLib"))
            itemHider = null
        }
    }

    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ACTIVE)
    private fun init1() {
        // 没事儿改包名很爽吗, 写MM的, 你妈死了
        mythicMobsHooker =
            kotlin.runCatching {
                // 5.6.0+
                if (Class.forName("io.lumine.mythic.core.config.MythicConfigImpl")
                        .getDeclaredMethod("getFileConfiguration").returnType == FileConfiguration::class.java
                ) {
                    Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl560")
                        .newInstance() as MythicMobsHooker
                } else {
                    null
                }
            }.getOrNull() ?: kotlin.runCatching {
                // 5.1.0+
                Class.forName("io.lumine.mythic.bukkit.utils.config.file.YamlConfiguration")
                Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl510")
                    .newInstance() as MythicMobsHooker
            }.getOrNull() ?: kotlin.runCatching {
                // 5.0.2+
                Class.forName("io.lumine.mythic.utils.config.file.YamlConfiguration")
                Class.forName("io.lumine.mythic.bukkit.MythicBukkit")
                Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl502")
                    .newInstance() as MythicMobsHooker
            }.getOrNull() ?: kotlin.runCatching {
                // 4.9.0+
                Class.forName("io.lumine.xikage.mythicmobs.utils.config.file.YamlConfiguration")
                Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl490")
                    .newInstance() as MythicMobsHooker
            }.getOrNull() ?: kotlin.runCatching {
                // 4.5.9+
                Class.forName("io.lumine.utils.config.file.YamlConfiguration")
                Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl459")
                    .newInstance() as MythicMobsHooker
            }.getOrNull() ?: kotlin.runCatching {
                // 4.4.0+
                Class.forName("pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl440")
                    .newInstance() as MythicMobsHooker
            }.getOrNull() ?: null.also {
                Bukkit.getLogger().info(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
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
    fun papi(player: OfflinePlayer?, text: String): String {
        return papiHooker?.papi(player, text) ?: text
    }

    /**
     * 解析papi变量的同时解析颜色代码
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    @JvmStatic
    fun papiColor(player: OfflinePlayer?, text: String): String {
        return papiHooker?.papi(player, ChatColor.translateAlternateColorCodes('&', text))
            ?: ChatColor.translateAlternateColorCodes('&', text)
    }

    /**
     * 判断文本中是否存在有效papi变量
     *
     * @param text 待检测文本
     * @return 是否存在有效papi变量
     */
    @JvmStatic
    fun hasPapi(text: String): Boolean {
        return papiHooker?.hasPapi(text) ?: false
    }

    /**
     * 将文本中的所有papi变量改写为papi节点
     *
     * @param text 待转换文本
     * @return 转换后文本
     */
    @JvmStatic
    fun toSection(text: String): String {
        return papiHooker?.toSection(text) ?: text
    }

    /**
     * 获取papi解析值
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param identifier 识别符, 如 %player_name% 中的 player
     * @param parameters 参数, 如 %player_name% 中的 name
     * @return 解析后文本
     */
    @JvmStatic
    fun requestPapi(player: OfflinePlayer, identifier: String, parameters: String): String {
        return papiHooker?.request(player, identifier, parameters)
            ?: "%${identifier}${if (parameters.isEmpty()) "" else "_"}$parameters%"
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
        return itemPlaceholder.parse(itemStack, text).text
    }

    /**
     * 获取已解析物品变量的物品名
     *
     * @return 解析后文本
     */
    @JvmStatic
    fun ItemStack.getParsedName(): String {
        TranslationUtils.getDisplayName(this)?.let { displayName ->
            return itemPlaceholder.parse(this, displayName).text
        }
        return TranslationUtils.getTranslationName(this)
    }

    /**
     * 获取已解析物品变量的物品名
     *
     * @return 解析后文本
     */
    @JvmStatic
    fun ItemStack.getParsedComponent(): BaseComponent {
        TranslationUtils.getDisplayName(this)?.let { displayName ->
            return TextComponent(itemPlaceholder.parse(this, displayName).text)
        }
        return TranslationUtils.getTranslationComponent(this)
    }

    /**
     * 解析物品名和物品Lore中的物品变量
     *
     * @param itemStack 待解析物品
     */
    @JvmStatic
    fun parseItemPlaceholders(itemStack: ItemStack) {
        itemPlaceholder.itemParse(itemStack)
    }

    /**
     * 添加物品变量附属
     *
     * @param id 变量ID
     * @param function 操作函数
     */
    @JvmStatic
    fun addItemPlaceholderExpansion(id: String, function: BiFunction<ItemStack, String, String?>) {
        itemPlaceholder.addExpansion(id, function)
    }

    /**
     * 向聊天组件添加文本浮窗
     */
    @JvmStatic
    fun ComponentBuilder.hoverText(text: String): ComponentBuilder {
        return event(nmsHooker.hoverText(text))
    }

    /**
     * 向聊天组件添加物品浮窗
     */
    @JvmStatic
    fun ComponentBuilder.hoverItem(itemStack: ItemStack): ComponentBuilder {
        return event(nmsHooker.hoverItem(itemStack))
    }

    /**
     * 向聊天组件添加指令执行
     */
    @JvmStatic
    fun ComponentBuilder.runCommand(text: String): ComponentBuilder {
        return event(ClickEvent(ClickEvent.Action.RUN_COMMAND, text))
    }

    /**
     * 向聊天组件添加指令建议
     */
    @JvmStatic
    fun ComponentBuilder.suggestCommand(text: String): ComponentBuilder {
        return event(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, text))
    }

    /**
     * 组合聊天组件
     */
    @JvmStatic
    fun ComponentBuilder.append(builder: ComponentBuilder): ComponentBuilder {
        return append(builder.create())
    }

    /**
     * 根据各个参数, 尝试获取NI物品或挂钩的物品库中的物品.
     * 首先根据id尝试获取NI物品, 如获取不到对应的NI物品, 则进行如下操作:
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @return 对应物品.
     */
    @JvmStatic
    fun getNiOrHookedItem(
        id: String
    ): ItemStack? {
        if (ItemManager.hasItem(id)) {
            val itemStack = ItemManager.getItemStack(id, null, HashMap())
            if (itemStack != null) return itemStack
        }
        return getHookedItem(id)
    }

    /**
     * 根据各个参数, 尝试获取NI物品或挂钩的物品库中的物品.
     * 首先根据id尝试获取NI物品, 如获取不到对应的NI物品, 则进行如下操作:
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @param player 用作参数的玩家
     * @return 对应物品.
     */
    @JvmStatic
    fun getNiOrHookedItem(
        id: String,
        player: OfflinePlayer?
    ): ItemStack? {
        if (ItemManager.hasItem(id)) {
            val itemStack = ItemManager.getItemStack(id, player, HashMap())
            if (itemStack != null) return itemStack
        }
        return getHookedItem(id)
    }

    /**
     * 根据各个参数, 尝试获取NI物品或挂钩的物品库中的物品.
     * 首先根据id尝试获取NI物品, 如获取不到对应的NI物品, 则进行如下操作:
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @param player 用作参数的玩家
     * @param data 指向数据
     * @return 对应物品.
     */
    @JvmStatic
    fun getNiOrHookedItem(
        id: String,
        player: OfflinePlayer?,
        data: String?
    ): ItemStack? {
        if (ItemManager.hasItem(id)) {
            val itemStack = ItemManager.getItemStack(id, player, data)
            if (itemStack != null) return itemStack
        }
        return getHookedItem(id)
    }

    /**
     * 根据各个参数, 尝试获取NI物品或挂钩的物品库中的物品.
     * 首先根据id尝试获取NI物品, 如获取不到对应的NI物品, 则进行如下操作:
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @param data 指向数据
     * @return 对应物品.
     */
    @JvmStatic
    fun getNiOrHookedItem(
        id: String,
        data: MutableMap<String, String>?
    ): ItemStack? {
        if (ItemManager.hasItem(id)) {
            val itemStack = ItemManager.getItemStack(id, null, data)
            if (itemStack != null) return itemStack
        }
        return getHookedItem(id)
    }

    /**
     * 根据各个参数, 尝试获取NI物品或挂钩的物品库中的物品.
     * 首先根据id尝试获取NI物品, 如获取不到对应的NI物品, 则进行如下操作:
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @param data 指向数据
     * @return 对应物品.
     */
    @JvmStatic
    fun getNiOrHookedItem(
        id: String,
        data: String?
    ): ItemStack? {
        if (ItemManager.hasItem(id)) {
            val itemStack = ItemManager.getItemStack(id, null, data)
            if (itemStack != null) return itemStack
        }
        return getHookedItem(id)
    }

    /**
     * 根据各个参数, 尝试获取NI物品或挂钩的物品库中的物品.
     * 首先根据id尝试获取NI物品, 如获取不到对应的NI物品, 则进行如下操作:
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @param player 用作参数的玩家
     * @param data 指向数据
     * @return 对应物品.
     */
    @JvmStatic
    fun getNiOrHookedItem(
        id: String,
        player: OfflinePlayer?,
        data: MutableMap<String, String>?
    ): ItemStack? {
        if (ItemManager.hasItem(id)) {
            val itemStack = ItemManager.getItemStack(id, player, data)
            if (itemStack != null) return itemStack
        }
        return getHookedItem(id)
    }

    /**
     * 根据各个参数, 尝试获取挂钩的物品库中的物品.
     * 当id包含英文冒号:时, 根据冒号前的文本(不区分大小写)识别目标物品库, 根据冒号后的文本识别物品ID.
     * "mm", "MythicMobs" -> MythicMobs
     * "mg", "MagicGem" -> MagicGem
     * "ia", "ItemsAdder" -> ItemsAdder
     * "or", "Oraxen" -> Oraxen
     * "vn", "Vanilla" -> 原版物品
     * 当id不包含英文冒号:时, 以如下顺序逐个尝试进行物品获取:
     * MM > MagicGem > Oraxen > EI > 原版物品.
     *
     * @param id 物品ID
     * @return 对应物品.
     */
    @JvmStatic
    fun getHookedItem(
        id: String
    ): ItemStack? {
        if (id.contains(":")) {
            val nameSpaceToItemId = id.split(":", limit = 2)
            when (nameSpaceToItemId[0].lowercase()) {
                "mm", "mythicmobs" -> {
                    val itemStack = mythicMobsHooker?.getItemStackSync(nameSpaceToItemId[1])
                    if (itemStack != null) return itemStack
                }

                "mg", "magicgem" -> {
                    val itemStack = magicGemHooker?.getItemStack(nameSpaceToItemId[1])
                    if (itemStack != null) return itemStack
                }

                "ia", "itemsadder" -> {
                    val itemStack = itemsAdderHooker?.getItemStack(nameSpaceToItemId[1])
                    if (itemStack != null) return itemStack
                }

                "or", "oraxen" -> {
                    val itemStack = oraxenHooker?.getItemStack(nameSpaceToItemId[1])
                    if (itemStack != null) return itemStack
                }

                "vn", "vanilla" -> {
                    val material = getMaterial(nameSpaceToItemId[1])
                    if (material != null) {
                        return ItemStack(material)
                    }
                }
            }
        }

        if (mythicMobsHooker?.hasItem(id) == true) {
            val itemStack = mythicMobsHooker?.getItemStackSync(id)
            if (itemStack != null) return itemStack
        }

        if (magicGemHooker?.hasItem(id) == true) {
            val itemStack = magicGemHooker?.getItemStack(id)
            if (itemStack != null) return itemStack
        }

        if (itemsAdderHooker?.hasItem(id) == true) {
            val itemStack = itemsAdderHooker?.getItemStack(id)
            if (itemStack != null) return itemStack
        }

        if (oraxenHooker?.hasItem(id) == true) {
            val itemStack = oraxenHooker?.getItemStack(id)
            if (itemStack != null) return itemStack
        }

        val material = getMaterial(id.uppercase())
        if (material != null) {
            return ItemStack(material)
        }

        return null
    }

    @JvmStatic
    fun getMaterial(material: String?): Material? {
        return nmsHooker.getMaterial(material)
    }
}