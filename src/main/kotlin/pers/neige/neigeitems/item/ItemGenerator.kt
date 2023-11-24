package pers.neige.neigeitems.item

import bot.inker.bukkit.nbt.NbtItemStack
import com.alibaba.fastjson2.parseObject
import com.alibaba.fastjson2.toJSONString
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.inventory.meta.PotionMeta
import pers.neige.neigeitems.event.ItemGenerateEvent
import pers.neige.neigeitems.item.color.ItemColor
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.manager.HookerManager.nmsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ConfigUtils.coverWith
import pers.neige.neigeitems.utils.ConfigUtils.loadFromString
import pers.neige.neigeitems.utils.ConfigUtils.loadGlobalSections
import pers.neige.neigeitems.utils.ConfigUtils.saveToString
import pers.neige.neigeitems.utils.ConfigUtils.toMap
import pers.neige.neigeitems.utils.ItemUtils.copy
import pers.neige.neigeitems.utils.ItemUtils.toNbtCompound
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
    /**
     * 获取物品ID
     */
    val id = itemConfig.id

    /**
     * 获取物品所在文件
     */
    val file = itemConfig.file

    /**
     * 获取物品原配置
     */
    val originConfigSection = itemConfig.configSection ?: YamlConfiguration() as ConfigurationSection

    /**
     * 获取物品解析后配置(经过继承和全局节点调用)
     */
    val configSection = loadGlobalSections(inherit((YamlConfiguration() as ConfigurationSection), originConfigSection))

    /**
     * 获取物品节点配置
     */
    val sections = configSection.getConfigurationSection("sections")

    /**
     * 获取物品是否需要更新
     */
    val update = configSection.getBoolean(
        "options.update.enable",
        configSection.getBoolean("static.options.update.enable", false)
    )

    /**
     * 获取更新时保护的NBT
     */
    val protectNBT = when (update) {
        true -> {
            if (configSection.contains("options.update.protect")) {
                configSection.getStringList("options.update.protect")
            } else {
                configSection.getStringList("static.options.update.protect")
            }
        }
        else -> listOf()
    }

    /**
     * 获取更新时刷新的节点
     */
    val refreshData = when (update) {
        true -> {
            if (configSection.contains("options.update.refresh")) {
                configSection.getStringList("options.update.refresh")
            } else {
                configSection.getStringList("static.options.update.refresh")
            }
        }
        else -> listOf()
    }

    /**
     * 获取更新时重构的节点
     */
    val rebuildData = when (update) {
        true -> {
            if (configSection.contains("options.update.rebuild")) {
                configSection.getConfigurationSection("options.update.rebuild")?.toMap()
            } else {
                configSection.getConfigurationSection("static.options.update.rebuild")?.toMap()
            }
        }
        else -> null
    }

    /**
     * 获取物品静态配置
     */
    val static = configSection.getConfigurationSection("static")

    /**
     * 获取去除节点&静态配置的物品配置
     */
    val configNoSection = (YamlConfiguration() as ConfigurationSection).also {
        this.configSection.getKeys(false).forEach { key ->
            if (key != "sections" && key != "static") {
                it.set(key, this.configSection.get(key))
            }
        }
        it.set("options.update", null)
    }

    /**
     * 获取解析后物品配置文本
     */
    val configString = configSection.saveToString(id)

    /**
     * 获取去除节点&静态配置的物品配置文本
     */
    val configStringNoSection = configNoSection.saveToString(id)

    /**
     * 获取解析后物品配置文本哈希值
     */
    val hashCode = configString.hashCode()

    /**
     * 获取是否存在静态材质
     */
    val hasStaticMaterial =
        static?.getString("material")?.let { Material.matchMaterial(it.uppercase(Locale.getDefault())) } != null

    /**
     * 获取原始静态物品
     */
    private val originStaticItemStack = static?.let {
        var material = static.getString("material")?.let { Material.matchMaterial(it.uppercase(Locale.getDefault())) }
        material = material ?: Material.STONE
        // CraftItemStack在获取NBT方面具有无与伦比的性能优势
        val itemStack = NbtItemStack(ItemStack(material)).asCraftCopy()
        // 设置子ID/损伤值
        if (static.contains("damage")) {
            itemStack.durability = static.getInt("damage").toShort()
        }
        // 设置物品附魔
        if (static.contains("enchantments")) {
            // 获取所有待设置附魔
            val enchantSection = static.getConfigurationSection("enchantments")
            // 遍历添加
            enchantSection?.getKeys(false)?.forEach {
                val level = enchantSection.getInt(it)
                val enchant = Enchantment.getByName(it.uppercase(Locale.getDefault()))
                // 判断等级 && 附魔名称 是否合法
                if (level > 0 && enchant != null) {
                    // 添加附魔
                    itemStack.addUnsafeEnchantment(enchant, level)
                }
            }
        }
        // 获取ItemMeta
        val itemMeta = itemStack.itemMeta
        // 设置CustomModelData
        if (static.contains("custommodeldata")) {
            nmsHooker.setCustomModelData(itemMeta, static.getInt("custommodeldata"))
        }
        // 设置物品名
        if (static.contains("name")) {
            itemMeta?.setDisplayName(static.getString("name")
                ?.let { ChatColor.translateAlternateColorCodes('&', it) })
        }
        // 设置Lore
        if (static.contains("lore")) {
            val originLores = static.getStringList("lore")
            val finalLores = ArrayList<String>()
            for (i in originLores.indices) {
                val lores = ChatColor.translateAlternateColorCodes('&', originLores[i]).split("\n")
                finalLores.addAll(lores)
            }
            itemMeta?.lore = finalLores
        }
        // 设置是否无法破坏
        if (static.contains("unbreakable")) {
            itemMeta?.isUnbreakable = static.getBoolean("unbreakable")
        }
        // 设置ItemFlags
        if (static.contains("hideflags")) {
            val flags = static.getStringList("hideflags")
            for (value in flags) {
                try {
                    val itemFlag = ItemFlag.valueOf(value)
                    itemMeta?.addItemFlags(itemFlag)
                } catch (_: IllegalArgumentException) {
                }
            }
        }
        // 设置物品颜色
        if (static.contains("color")) {
            var color = static.get("color")
            color = when (color) {
                is String -> color.toIntOrNull(16) ?: 0
                else -> color.toString().toIntOrNull() ?: 0
            }
            color = color.coerceAtLeast(0).coerceAtMost(0xFFFFFF)
            when (itemMeta) {
                is LeatherArmorMeta -> itemMeta.setColor(Color.fromRGB(color))
                is MapMeta -> itemMeta.setColor(Color.fromRGB(color))
                is PotionMeta -> itemMeta.setColor(Color.fromRGB(color))
            }
        }
        itemStack.itemMeta = itemMeta
        // 获取NBT物品
        val nbtItemStack = NbtItemStack(itemStack)
        // 设置物品NBT
        val itemTag = nbtItemStack.orCreateTag
        // 检测是否移除NI自带NBT
        if (!static.getBoolean("options.removeNBT", false)) {
            val neigeItems = itemTag.getOrCreateCompound("NeigeItems")
            // 设置物品使用次数
            if (static.contains("options.charge")) {
                neigeItems.putInt("charge", static.getInt("options.charge"))
                neigeItems.putInt("maxCharge", static.getInt("options.charge"))
            }
            // 设置物品最大使用次数
            if (static.contains("options.maxcharge")) {
                neigeItems.putInt("maxCharge", static.getInt("options.maxCharge"))
            }
            // 设置物品自定义耐久
            if (static.contains("options.durability")) {
                neigeItems.putInt("durability", static.getInt("options.durability"))
                neigeItems.putInt("maxDurability", static.getInt("options.durability"))
            }
            // 设置物品最大自定义耐久
            if (static.contains("options.maxdurability")) {
                if (!neigeItems.contains("durability")) {
                    neigeItems.putInt("durability", static.getInt("options.maxdurability"))
                }
                neigeItems.putInt("maxDurability", static.getInt("options.maxDurability"))
            }
            // 设置物品自定义耐久为0时是否破坏
            if (static.contains("options.itembreak")) {
                neigeItems.putBoolean("itemBreak", static.getBoolean("options.itembreak", true))
            }
            // 设置具有owner的掉落物是否对其他人不可见
            if (static.contains("options.hide")) {
                neigeItems.putBoolean("hide", static.getBoolean("options.hide", true))
            }
            // 首次掉落归属
            if (static.contains("options.owner")) {
                neigeItems.putString("owner", static.getString("options.owner"))
            }
            // 设置掉落物闪光颜色
            if (static.contains("options.color")) {
                static.getString("options.color")?.uppercase(Locale.getDefault())?.let {
                    // 判断你这颜色保不保熟
                    if (ItemColor.colors.containsKey(it)) {
                        neigeItems.putString("color", it)
                    }
                }
            }
            // 设置掉落执行技能
            if (static.contains("options.dropskill")) {
                neigeItems.putString("dropSkill", static.getString("options.dropskill", ""))
            }
            // 设置物品时限
            if (static.contains("options.itemtime")) {
                neigeItems.putLong(
                    "itemTime",
                    System.currentTimeMillis() + (static.getLong("options.itemtime", 0) * 1000)
                )
            }
        }
        // 设置物品NBT
        if (static.contains("nbt")) {
            // 获取配置NBT
            val itemNBT = static.getConfigurationSection("nbt")?.toNbtCompound()
            // NBT覆盖
            itemNBT?.let { itemTag.coverWith(it) }
        }
        itemStack
    } ?: let {
        // CraftItemStack在获取NBT方面具有无与伦比的性能优势
        NbtItemStack(ItemStack(Material.STONE)).asCraftCopy()
    }

    /**
     * 获取静态物品
     */
    val staticItemStack get() = originStaticItemStack.copy()

    private fun inherit(
        configSection: ConfigurationSection,
        originConfigSection: ConfigurationSection
    ): ConfigurationSection {
        // 检测是否需要进行继承
        if (originConfigSection.contains("inherit") == true) {
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
                                        (YamlConfiguration() as ConfigurationSection),
                                        currentSection
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
                                (YamlConfiguration() as ConfigurationSection),
                                inheritConfigSection
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
                                    (YamlConfiguration() as ConfigurationSection),
                                    currentSection
                                ), false
                            )
                            // 进行模板覆盖
                            configSection.coverWith(realConfig)
                        }
                    }
                }
            }
        }
        // 覆盖物品配置
        configSection.coverWith(originConfigSection)
        return configSection
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
        debug(configString)
        sections?.let { debug(sections.saveToString("$id-sections")) }
        val configSection = configString.loadFromString(id) ?: YamlConfiguration()

        // 构建物品
        if (configSection.contains("material") || hasStaticMaterial) {
            // 获取材质
            val material =
                configSection.getString("material")?.let { Material.matchMaterial(it.uppercase(Locale.getDefault())) }
            if (material != null || hasStaticMaterial) {
                // 预处理中已将ItemStack转为CraftItemStack, 可提升NBT操作效率
                val itemStack = staticItemStack
                material?.let { itemStack.type = material }
                // 空物品检测
                if (itemStack.type == Material.AIR) {
                    // 触发一下物品生成事件
                    val event = ItemGenerateEvent(id, player, itemStack, cache, configSection, sections)
                    event.call()
                    return event.itemStack
                }
                // 设置子ID/损伤值
                if (configSection.contains("damage")) {
                    itemStack.durability = configSection.getInt("damage").toShort()
                }
                // 设置物品附魔
                if (configSection.contains("enchantments")) {
                    // 获取所有待设置附魔
                    val enchantSection = configSection.getConfigurationSection("enchantments")
                    // 遍历添加
                    enchantSection?.getKeys(false)?.forEach {
                        val level = enchantSection.getInt(it)
                        val enchant = Enchantment.getByName(it.uppercase(Locale.getDefault()))
                        // 判断等级 && 附魔名称 是否合法
                        if (level > 0 && enchant != null) {
                            // 添加附魔
                            itemStack.addUnsafeEnchantment(enchant, level)
                        }
                    }
                }
                // 获取ItemMeta
                val itemMeta = itemStack.itemMeta
                // 设置CustomModelData
                if (configSection.contains("custommodeldata")) {
                    nmsHooker.setCustomModelData(itemMeta, configSection.getInt("custommodeldata"))
                }
                // 设置物品名
                if (configSection.contains("name")) {
                    itemMeta?.setDisplayName(configSection.getString("name")
                        ?.let { ChatColor.translateAlternateColorCodes('&', it) }
                    )
                }
                // 设置Lore
                if (configSection.contains("lore")) {
                    val originLores = configSection.getStringList("lore")
                    val finalLores = ArrayList<String>()
                    for (i in originLores.indices) {
                        val lores = ChatColor.translateAlternateColorCodes('&', originLores[i]).split("\n")
                        finalLores.addAll(lores)
                    }
                    itemMeta?.lore = finalLores
                }
                // 设置是否无法破坏
                if (configSection.contains("unbreakable")) {
                    itemMeta?.isUnbreakable = configSection.getBoolean("unbreakable")
                }
                // 设置ItemFlags
                if (configSection.contains("hideflags")) {
                    val flags = configSection.getStringList("hideflags")
                    for (value in flags) {
                        try {
                            val itemFlag = ItemFlag.valueOf(value)
                            itemMeta?.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {
                        }
                    }
                }
                // 设置物品颜色
                if (configSection.contains("color")) {
                    var color = configSection.get("color")
                    color = when (color) {
                        is String -> color.toIntOrNull(16) ?: 0
                        else -> color.toString().toIntOrNull() ?: 0
                    }
                    color = color.coerceAtLeast(0).coerceAtMost(0xFFFFFF)
                    when (itemMeta) {
                        is LeatherArmorMeta -> itemMeta.setColor(Color.fromRGB(color))
                        is MapMeta -> itemMeta.setColor(Color.fromRGB(color))
                        is PotionMeta -> itemMeta.setColor(Color.fromRGB(color))
                    }
                }
                itemStack.itemMeta = itemMeta
                // 获取NBT物品
                val nbtItemStack = NbtItemStack(itemStack)
                // 设置物品NBT
                val itemTag = nbtItemStack.orCreateTag
                // 检测是否移除NI自带NBT
                if (!configSection.getBoolean("options.removeNBT", false)) {
                    val neigeItems = itemTag.getOrCreateCompound("NeigeItems")
                    neigeItems.putString("id", id)
                    neigeItems.putString("data", cache.toJSONString())
                    neigeItems.putInt("hashCode", hashCode)
                    // 设置物品使用次数
                    if (configSection.contains("options.charge")) {
                        neigeItems.putInt("charge", configSection.getInt("options.charge"))
                        neigeItems.putInt("maxCharge", configSection.getInt("options.charge"))
                    }
                    // 设置物品最大使用次数
                    if (configSection.contains("options.maxcharge")) {
                        neigeItems.putInt("maxCharge", configSection.getInt("options.maxcharge"))
                    }
                    // 设置物品自定义耐久
                    if (configSection.contains("options.durability")) {
                        neigeItems.putInt("durability", configSection.getInt("options.durability"))
                        neigeItems.putInt("maxDurability", configSection.getInt("options.durability"))
                    }
                    // 设置物品最大自定义耐久
                    if (configSection.contains("options.maxdurability")) {
                        if (!neigeItems.contains("durability")) {
                            neigeItems.putInt("durability", configSection.getInt("options.maxdurability"))
                        }
                        neigeItems.putInt("maxDurability", configSection.getInt("options.maxdurability"))
                    }
                    // 设置物品自定义耐久为0时是否破坏
                    if (configSection.contains("options.itembreak")) {
                        neigeItems.putBoolean("itemBreak", configSection.getBoolean("options.itembreak", true))
                    }
                    // 设置具有owner的掉落物是否对其他人不可见
                    if (configSection.contains("options.hide")) {
                        neigeItems.putBoolean("hide", configSection.getBoolean("options.hide", true))
                    }
                    // 首次掉落归属
                    if (configSection.contains("options.owner")) {
                        neigeItems.putString("owner", configSection.getString("options.owner"))
                    }
                    // 设置掉落物闪光颜色
                    if (configSection.contains("options.color")) {
                        configSection.getString("options.color")?.uppercase(Locale.getDefault())?.let {
                            // 判断你这颜色保不保熟
                            if (ItemColor.colors.containsKey(it)) {
                                neigeItems.putString("color", it)
                            }
                        }
                    }
                    // 设置掉落执行技能
                    if (configSection.contains("options.dropskill")) {
                        neigeItems.putString("dropSkill", configSection.getString("options.dropskill", ""))
                    }
                    // 设置物品时限
                    if (configSection.contains("options.itemtime")) {
                        neigeItems.putLong(
                            "itemTime",
                            System.currentTimeMillis() + (configSection.getLong("options.itemtime", 0) * 1000)
                        )
                    }
                }
                // 设置物品NBT
                if (configSection.contains("nbt")) {
                    // 获取配置NBT
                    val itemNBT = configSection.getConfigurationSection("nbt")?.toNbtCompound()
                    // NBT覆盖
                    itemNBT?.let { itemTag.coverWith(it) }
                }
                // 触发一下物品生成事件
                val event = ItemGenerateEvent(id, player, itemStack, cache, configSection, sections)
                event.call()
                return event.itemStack
            } else {
                Bukkit.getConsoleSender().sendLang(
                    "Messages.invalidMaterial", mapOf(
                        Pair("{itemID}", id),
                        Pair("{material}", configSection.getString("material") ?: "")
                    )
                )
            }
        }
        return null
    }
}
