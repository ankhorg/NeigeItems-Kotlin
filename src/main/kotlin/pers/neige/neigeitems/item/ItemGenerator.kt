package pers.neige.neigeitems.item

import com.alibaba.fastjson2.parseObject
import com.alibaba.fastjson2.toJSONString
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.action.container.ActionContainer
import pers.neige.neigeitems.config.ConfigReader
import pers.neige.neigeitems.event.ItemGenerateEvent
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ConfigUtils.coverWith
import pers.neige.neigeitems.utils.ConfigUtils.loadGlobalSections
import pers.neige.neigeitems.utils.ConfigUtils.saveToString
import pers.neige.neigeitems.utils.ConfigUtils.toStringMap
import pers.neige.neigeitems.utils.ItemUtils.asCraftCopy
import pers.neige.neigeitems.utils.ItemUtils.copy
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull
import pers.neige.neigeitems.utils.ItemUtils.isCraftItem
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.*

/**
 * 物品生成器
 *
 * @property itemConfig 物品基础配置
 * @constructor 根据物品基础配置构建物品生成器
 */
class ItemGenerator(val itemConfig: ItemConfig) {
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(ItemGenerator::class.java.simpleName)
    }

    /**
     * 获取物品ID
     */
    val id = itemConfig.id

    /**
     * 获取物品所在文件
     */
    val file = itemConfig.file

    /**
     * 获取物品解析后配置(经过继承和全局节点调用)
     */
    val configSection = loadGlobalSections(
        inherit(
            YamlConfiguration() as ConfigurationSection,
            itemConfig.configSection ?: YamlConfiguration() as ConfigurationSection
        )
    )

    /**
     * 获取物品节点配置
     */
    val sections = configSection.getConfigurationSection("sections")

    /**
     * 获取物品是否需要更新
     */
    val update = configSection.getBoolean("options.update.enable", false)

    /**
     * 获取更新时保护的NBT
     */
    val protectNBT = if (update) {
        configSection.getStringList("options.update.protect")
    } else listOf()

    /**
     * 获取更新时刷新的节点
     */
    val refreshData = if (update) {
        configSection.getStringList("options.update.refresh")
    } else listOf()

    /**
     * 获取更新时重构的节点
     */
    val rebuildData = if (update) {
        configSection.getConfigurationSection("options.update.rebuild")?.toStringMap()
    } else null

    val eventActions: ActionContainer =
        ActionContainer(ActionManager, "event", configSection.getConfigurationSection("event"))

    /**
     * 获取物品静态配置
     */
    val static = ConfigReader.parse(configSection.getConfigurationSection("static"))

    /**
     * 获取去除静态配置的物品配置文本
     */
    private val configStringNoSection = YamlConfiguration().also {
        this.configSection.getKeys(false).forEach { key ->
            it.set(key, this.configSection.get(key))
        }
        it.set("sections", null)
        it.set("static", null)
        it.set("options.update", null)
        it.set("event", null)
    }.saveToString()

    /**
     * 获取解析后物品配置文本哈希值
     */
    val hashCode = configSection.saveToString(id).hashCode()

    /**
     * 获取是否存在静态材质
     */
    private val hasStaticMaterial =
        static?.getString("material")?.let { HookerManager.getMaterial(it) } != null

    private val originStaticItemStack = load(static) ?: ItemStack(Material.STONE).asCraftCopy()

    private fun load(
        config: ConfigReader?,
        base: ItemStack? = null,
        baseMaterial: Material? = null,
        cache: Map<String, String>? = null
    ): ItemStack? {
        config ?: return base
        val builder = HookerManager.nmsHooker.newItemBuilder(baseMaterial)
        builder.load(config)
        builder.itemStack = if (base.isCraftItem()) base!! else base?.asCraftCopy() ?: ItemStack(
            builder.type ?: Material.STONE
        ).asCraftCopy()
        builder.runPreCoverNbt { itemStack, nbt ->
            if (config.getBoolean("options.removeNBT", false) || config.getBoolean(
                    "options.remove-nbt", false
                )
            ) return@runPreCoverNbt
            val neigeItems = nbt.getOrCreateCompound("NeigeItems")
            if (cache != null) {
                neigeItems.putString("id", id)
                if (ConfigManager.newDataFormat && cache.isNotEmpty()) {
                    val data = neigeItems.getOrCreateCompound("data")
                    (cache as Map<String, Any?>).forEach { (key, value) ->
                        if (value == null) return@forEach
                        data.putDeepString(key, value.toString())
                    }
                } else {
                    neigeItems.putString("data", cache.toJSONString())
                }
                neigeItems.putInt("hashCode", hashCode)
            }
            val optionsConfig = config.getConfig("options") ?: return@runPreCoverNbt
            for (key in optionsConfig.keySet()) {
                when (key.lowercase()) {
                    // 设置物品使用次数
                    "charge" -> {
                        val charge = optionsConfig.getInt(key)
                        neigeItems.putInt("charge", charge)
                        neigeItems.putInt("maxCharge", charge)
                    }
                    // 设置物品最大使用次数
                    "maxcharge", "max-charge" -> {
                        val maxCharge = optionsConfig.getInt(key)
                        if (!neigeItems.contains("charge")) {
                            neigeItems.putInt("charge", maxCharge)
                        }
                        neigeItems.putInt("maxCharge", maxCharge)
                    }
                    // 设置物品自定义耐久
                    "durability" -> {
                        val durability = optionsConfig.getInt(key)
                        neigeItems.putInt("durability", durability)
                        neigeItems.putInt("maxDurability", durability)
                    }
                    // 设置物品最大自定义耐久
                    "maxdurability", "max-durability" -> {
                        val maxDurability = optionsConfig.getInt(key)
                        if (!neigeItems.contains("durability")) {
                            neigeItems.putInt("durability", maxDurability)
                        }
                        neigeItems.putInt("maxDurability", maxDurability)
                    }
                    // 设置物品自定义耐久为0时是否破坏
                    "itembreak", "item-break" -> {
                        neigeItems.putBoolean("itemBreak", optionsConfig.getBoolean(key, true))
                    }
                    // 设置具有owner的掉落物是否对其他人不可见
                    "hide" -> {
                        neigeItems.putBoolean("hide", optionsConfig.getBoolean(key, true))
                    }
                    // 首次掉落归属
                    "owner" -> {
                        neigeItems.putString("owner", optionsConfig.getString(key))
                    }
                    // 设置掉落物闪光颜色
                    "color" -> {
                        optionsConfig.getString(key)?.uppercase(Locale.getDefault())?.let {
                            // 判断你这颜色保不保熟
                            if (ItemColor.getColors().containsKey(it)) {
                                neigeItems.putString("color", it)
                            }
                        }
                    }
                    // 设置掉落执行技能
                    "dropskill", "drop-skill" -> {
                        neigeItems.putString("dropSkill", optionsConfig.getString(key, ""))
                    }
                    // 设置物品时限
                    "itemtime", "item-time" -> {
                        neigeItems.putLong(
                            "itemTime", System.currentTimeMillis() + (optionsConfig.getLong(key, 0) * 1000)
                        )
                    }
                }
            }
        }
        val itemStack = builder.build()
        return itemStack
    }

    /**
     * 获取静态物品
     */
    val staticItemStack get() = originStaticItemStack.copy()

    private fun inherit(
        configSection: ConfigurationSection, originConfigSection: ConfigurationSection
    ): ConfigurationSection {
        // 检测进行全局继承/部分继承
        when (val inheritInfo = originConfigSection.get("inherit")) {
            is MemorySection -> {
                /**
                 * 指定多个ID, 进行部分继承
                 * @variable key String 要进行继承的节点ID
                 * @variable value String 用于获取继承值的模板ID
                 */
                inheritInfo.getKeys(true).forEach { key ->
                    // 获取模板ID
                    val id = inheritInfo.get(key)
                    // 检测当前键是否为末级键
                    if (id is String) {
                        // 获取模板
                        val currentSection = ItemManager.getOriginConfig(id)
                        // 如果存在对应模板且模板存在对应键, 进行继承
                        if (currentSection != null) {
                            val realConfig = loadGlobalSections(
                                inherit(
                                    (YamlConfiguration() as ConfigurationSection), currentSection
                                ), false
                            )
                            if (realConfig.contains(key)) {
                                configSection.set(key, realConfig.get(key))
                            }
                        }
                    }
                }
            }

            is String -> {
                // 仅指定单个模板ID，进行全局继承
                ItemManager.getOriginConfig(inheritInfo)?.let { inheritConfigSection ->
                    val realConfig = loadGlobalSections(
                        inherit(
                            (YamlConfiguration() as ConfigurationSection), inheritConfigSection
                        ), false
                    )
                    configSection.coverWith(realConfig)
                }
            }

            is List<*> -> {
                // 顺序继承, 按顺序进行覆盖式继承
                for (templateId in inheritInfo) {
                    // 逐个获取模板
                    ItemManager.getOriginConfig(templateId as String)?.let { currentSection ->
                        val realConfig = loadGlobalSections(
                            inherit(
                                (YamlConfiguration() as ConfigurationSection), currentSection
                            ), false
                        )
                        // 进行模板覆盖
                        configSection.coverWith(realConfig)
                    }
                }
            }
        }
        // 覆盖物品配置
        configSection.coverWith(originConfigSection)
        return configSection.clone()
    }

    /**
     * 生成物品, 生成失败则返回null
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 生成的物品, 生成失败则返回null
     */
    fun getItemStack(player: OfflinePlayer?, data: String?): ItemStack? {
        return getItemStack(
            player, when (data) {
                null -> HashMap<String, String>()
                else -> data.parseObject<HashMap<String, String>>()
            }
        )
    }

    /**
     * 生成物品, 生成失败则返回null
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 生成的物品, 生成失败则返回null
     */
    fun getItemStack(player: OfflinePlayer?, data: MutableMap<String, String>?): ItemStack? {
        // 加载缓存
        val cache = data ?: HashMap<String, String>()
        // 获取私有节点配置
        val sections = this.sections
        // 对文本化配置进行全局节点解析
        val configString = configStringNoSection.parseSection(cache, player, sections)
        // Debug信息
        if (debug) {
            logger.info(configString)
            sections?.let { logger.info(sections.saveToString("sections")) }
        }
        val configSection = ConfigReader.parse(configString)

        // 构建物品
        // 获取材质
        val material = HookerManager.getMaterial(configSection.getString("material"))
        if (material != null || hasStaticMaterial) {
            // 预处理中已将ItemStack转为CraftItemStack, 可提升NBT操作效率
            val itemStack = staticItemStack
            // 空物品检测
            if (itemStack.type == Material.AIR) {
                // 触发一下物品生成事件
                val event = ItemGenerateEvent(id, player, itemStack, cache, configSection, sections)
                event.call()
                return event.itemStack
            }
            // 加载物品配置
            load(configSection, itemStack, material, cache)
            // 触发一下物品生成事件
            val event = ItemGenerateEvent(id, player, itemStack, cache, configSection, sections)
            event.call()
            val params = hashMapOf(
                "id" to id,
                "item" to this
            )
            val context = ActionContext(
                player?.player,
                params,
                params,
                event.itemStack,
                event.itemStack.getNbtOrNull(),
                event.cache
            )
            eventActions.run("post-generate", context)
            return event.itemStack
        } else {
            Bukkit.getConsoleSender().sendLang(
                "Messages.invalidMaterial", mapOf(
                    Pair("{itemID}", id), Pair("{material}", configSection.getString("material", "")!!)
                )
            )
        }
        return null
    }
}
