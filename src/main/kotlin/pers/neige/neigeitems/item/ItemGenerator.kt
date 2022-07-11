package pers.neige.neigeitems.item

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.inventory.meta.PotionMeta
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.section.Section
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ConfigUtils.coverWith
import pers.neige.neigeitems.utils.ConfigUtils.loadFromString
import pers.neige.neigeitems.utils.ConfigUtils.saveToString
import pers.neige.neigeitems.utils.ConfigUtils.toMap
import pers.neige.neigeitems.utils.GsonUtils.gson
import pers.neige.neigeitems.utils.ItemUtils.coverWith
import pers.neige.neigeitems.utils.ItemUtils.toItemTag
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// 物品ID
class ItemGenerator (itemConfig: ItemConfig) {
    // 物品ID
    val id = itemConfig.id
    // 物品所在文件
    val file = itemConfig.file
    // 物品原配置
    val originConfigSection = itemConfig.configSection?.clone() ?: YamlConfiguration() as ConfigurationSection
    // 解析后配置
    var configSection = loadGlobalSections(inherit((YamlConfiguration() as ConfigurationSection), originConfigSection))
    // 物品配置文本
    val configString = configSection.saveToString(id)
    // 物品配置文本哈希值
    val hashCode = configString.hashCode()

    private fun inherit(configSection: ConfigurationSection, originConfigSection: ConfigurationSection): ConfigurationSection {
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
                        val value = inheritInfo.get(key)
                        // 检测当前键是否为末级键
                        if (value is String) {
                            // 获取模板
                            val currentSection = ItemManager.getItem(value)?.originConfigSection
                            // 如果存在对应模板且模板存在对应键, 进行继承
                            if (currentSection != null && currentSection.contains(key)) {
                                configSection.set(key, currentSection.get(key))
                            }
                        }
                    }
                }
                is String -> {
                    // 仅指定单个模板ID，进行全局继承
                    val inheritConfigSection = ItemManager.getItem(inheritInfo)?.originConfigSection
                    inheritConfigSection?.getKeys(false)?.forEach { key ->
                        configSection.set(key, inheritConfigSection.get(key))
                    }
                }
                is List<*> -> {
                    // 顺序继承, 按顺序进行覆盖式继承
                    for (templateId in inheritInfo) {
                        // 逐个获取模板
                        val currentSection = ItemManager.getItem(templateId as String)?.originConfigSection
                        // 进行模板覆盖
                        currentSection?.getKeys(true)?.forEach { key ->
                            val value = currentSection.get(key)
                            if (value !is MemorySection) {
                                configSection.set(key, currentSection.get(key))
                            }
                        }
                    }
                }
            }
        }
        // 覆盖物品配置
        configSection.coverWith(originConfigSection)
        return configSection
    }

    // 全局节点加载
    private fun loadGlobalSections(configSection: ConfigurationSection): ConfigurationSection {
        // 如果调用了全局节点
        if (configSection.contains("globalsections")) {
            // 获取全局节点ID
            val globalSectionIds = configSection.getStringList("globalsections")
            // 针对每个试图调用的全局节点
            globalSectionIds.forEach {
                when (val values = SectionManager.globalSectionMap[it]) {
                    // 对于节点调用
                    null -> {
                        SectionManager.globalSections[it]?.let { value ->
                            configSection.set("sections.$it", value)
                        }
                    }
                    // 对于节点文件调用
                    else -> {
                        for (value in values) {
                            configSection.set("sections.$it", value)
                        }
                    }
                }
            }
        }
        return configSection
    }

    fun getItemStack(player: OfflinePlayer?, data: String?): ItemStack? {
        var configString = this.configString

        // 进行一次papi解析
        if (player != null) {
            configString = PlaceholderAPI.setPlaceholders(player, configString)
        }
        // 加载回YamlConfiguration
        var configSection = configString.loadFromString(id) ?: YamlConfiguration()

        // 加载缓存
        val cache = when (data) {
            null -> HashMap()
            else -> gson.fromJson(data, HashMap::class.java) as HashMap<String, String>
        }

        // 获取私有节点配置
        val sections = when {
            configSection.contains("sections") -> configSection.getConfigurationSection("sections")
            else -> null
        }
        // 如果当前物品包含预声明节点
        sections?.getKeys(false)?.forEach {
            when (val value = sections.get(it)) {
                // 正儿八经预声明节点
                is ConfigurationSection -> {
                    Section(value).load(cache, player, sections)
                }
                // 私有节点
                else -> {
                    value.toString().parseSection(cache, player, sections)
                }
            }
        }
        // 对文本化配置进行全局节点解析
        configString = configSection
            .saveToString(id)
            .parseSection(cache, player, sections)
            .replace("\\<", "<")
            .replace("\\>", ">")
        if (player != null) {
            configString = PlaceholderAPI.setPlaceholders(player, configString)
        }
        // if (config_NI.Debug) print(configString)
        configSection = configString.loadFromString(id) ?: YamlConfiguration()
        // 构建物品
        if (configSection.contains("material")) {
            // 获取材质
            val material = configSection.getString("material")?.let { Material.matchMaterial(it.uppercase(Locale.getDefault())) }
            if (material != null) {
                val itemStack = ItemStack(material)
                // 设置子ID/损伤值
                if (configSection.contains("damage")) {
                    itemStack.setDurability(configSection.getInt("damage").toShort())
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
                    try {
                        itemMeta.setCustomModelData(configSection.getInt("custommodeldata"))
                    } catch (error: NoSuchMethodError) {}
                }
                // 设置物品名
                if (configSection.contains("name")) {
                    itemMeta.setDisplayName(configSection.getString("name")
                        ?.let { ChatColor.translateAlternateColorCodes('&', it) })
                }
                // 设置Lore
                if (configSection.contains("lore")) {
                    val originLores = configSection.getStringList("lore")
                    val finalLores = ArrayList<String>()
                    for (i in originLores.indices) {
                        val lores = ChatColor.translateAlternateColorCodes('&', originLores[i]).split("\n")
                        finalLores.addAll(lores)
                    }
                    itemMeta.setLore(finalLores)
                }
                // 设置是否无法破坏
                if (configSection.contains("unbreakable")) {
                    itemMeta.isUnbreakable = configSection.getBoolean("unbreakable")
                }
                // 设置ItemFlags
                if (configSection.contains("hideflags")) {
                    val flags = configSection.getStringList("hideflags")
                    for (value in flags) {
                        try {
                            val itemFlag = ItemFlag.valueOf(value)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (error: Throwable) {}
                    }
                }
                // 设置物品颜色
                if (configSection.contains("color")) {
                    try {
                        var color = configSection.get("color")
                        color = when (color) {
                            is String -> color.toInt(16)
                            else -> color.toString().toInt()
                        }
                        color = 0.coerceAtLeast(color).coerceAtMost(0xFFFFFF)
                        when (itemMeta) {
                            is LeatherArmorMeta -> itemMeta.setColor(Color.fromRGB(color))
                            is MapMeta -> itemMeta.color = Color.fromRGB(color)
                            is PotionMeta -> itemMeta.color = Color.fromRGB(color)
                        }
                    } catch (error: Throwable) {}
                }
                itemStack.setItemMeta(itemMeta)
                // 设置物品NBT
                val itemTag = itemStack.getItemTag()
                val neigeItems = ItemTag()
                neigeItems["id"] = ItemTagData(id)
                neigeItems["data"] = ItemTagData(gson.toJson(cache))
                neigeItems["hashCode"] = ItemTagData(hashCode)
                // 设置物品使用次数
                if (configSection.contains("options.charge")) {
                    neigeItems["charge"] = ItemTagData(configSection.getInt("options.charge"))
                    neigeItems["maxCharge"] = ItemTagData(configSection.getInt("options.charge"))
                }
                // 设置掉落物闪光颜色
                if (configSection.contains("options.color")) {
                    try {
                        configSection.getString("options.color")?.uppercase(Locale.getDefault())?.let {
                            // 判断你这颜色保不保熟
                            ChatColor.valueOf(it)
                            neigeItems["color"] = ItemTagData(it)
                        }
                    } catch (error: Throwable) {}
                }
                // 设置掉落执行技能
                if (configSection.contains("options.dropskill")) {
                    neigeItems["dropSkill"] = ItemTagData(configSection.getString("options.dropskill") ?: "")
                }
                itemTag["NeigeItems"] = neigeItems
                // 设置物品NBT
                if (configSection.contains("nbt")) {
                    // 获取配置NBT
                    val itemNBT = configSection.getConfigurationSection("nbt")?.toMap()?.toItemTag()
                    // NBT覆盖
                    itemNBT?.let {itemTag.coverWith(it)}
                }
                itemTag.saveTo(itemStack)
                return itemStack
            }
        }
        return null
    }
}
