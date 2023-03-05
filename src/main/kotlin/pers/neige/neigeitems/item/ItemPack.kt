package pers.neige.neigeitems.item

import com.alibaba.fastjson2.parseObject
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ConfigUtils.loadFromString
import pers.neige.neigeitems.utils.ConfigUtils.loadGlobalSections
import pers.neige.neigeitems.utils.ConfigUtils.saveToString
import pers.neige.neigeitems.utils.ItemUtils
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.HashMap

/**
 * 物品包信息
 *
 * @property id 物品包ID
 * @property rawConfigSection 物品包配置
 */
class ItemPack(
    /**
     * 获取物品包ID
     */
    val id: String,
    rawConfigSection: ConfigurationSection
) {
    /**
     * 获取物品包配置
     */
    val configSection = loadGlobalSections(rawConfigSection)

    /**
     * 获取物品包物品配置
     */
    val items: List<String> = configSection.getStringList("Items")

    /**
     * 获取物品包物品配置
     */
    val sections: ConfigurationSection? = configSection.getConfigurationSection("sections").also {
        configSection.set("sections", null)
    }

    /**
     * 获取物品包多彩掉落配置
     */
    val fancyDropConfig = configSection.getConfigurationSection("FancyDrop")

    /**
     * 获取物品包是否配置了多彩掉落信息
     */
    val fancyDrop = fancyDropConfig != null

    /**
     * 获取多彩掉落横向偏移文本
     */
    val offsetXString: String? = fancyDropConfig?.getString("offset.x")

    /**
     * 获取多彩掉落纵向偏移文本
     */
    val offsetYString: String? = fancyDropConfig?.getString("offset.y")

    /**
     * 获取多彩掉落类型文本
     */
    val angleType: String? = fancyDropConfig?.getString("angle.type")

    /**
     * 获取解析后物品包配置
     *
     * @return 解析后物品包配置
     */
    fun getSection(): ConfigurationSection? {
        return getSection(null, null as? HashMap<String, String>)
    }

    /**
     * 获取解析后物品包配置
     *
     * @param player 用于解析内容的玩家
     * @return 解析后物品包配置
     */
    fun getSection(player: OfflinePlayer?): ConfigurationSection? {
        return getSection(player, null as? HashMap<String, String>)
    }

    /**
     * 获取解析后物品包配置
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 解析后物品包配置
     */
    fun getSection(player: OfflinePlayer?, data: String?): ConfigurationSection? {
        return getSection(player, when (data) {
            null -> HashMap<String, String>()
            else -> data.parseObject<HashMap<String, String>>()
        })
    }

    /**
     * 获取解析后物品包配置
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 解析后物品包配置
     */
    fun getSection(player: OfflinePlayer?, data: HashMap<String, String>?): ConfigurationSection? {
        // 物品包configuration
        val configSection = this.configSection.clone()
        // 加载缓存
        val cache = data ?: HashMap<String, String>()

        // 获取私有节点配置
        val sections = this.sections
        // 对文本化配置进行全局节点解析
        val configString = configSection
            .saveToString(id)
            .parseSection(cache, player, sections)
        // Debug信息
        if (ConfigManager.config.getBoolean("Main.Debug")) print(configString)
        if (ConfigManager.config.getBoolean("Main.Debug") && sections != null) print(sections.saveToString("$id-sections"))
        return configString.loadFromString(id) ?: YamlConfiguration()
    }

    /**
     * 获取物品
     *
     * @return 物品
     */
    fun getItemStacks(): List<ItemStack> {
        return getItemStacks(null, null as? HashMap<String, String>)
    }

    /**
     * 获取物品
     *
     * @param player 用于解析内容的玩家
     * @return 物品
     */
    fun getItemStacks(player: OfflinePlayer?): List<ItemStack> {
        return getItemStacks(player, null as? HashMap<String, String>)
    }

    /**
     * 获取物品
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 物品
     */
    fun getItemStacks(player: OfflinePlayer?, data: String?): List<ItemStack> {
        return getItemStacks(player, when (data) {
            null -> HashMap<String, String>()
            else -> data.parseObject<HashMap<String, String>>()
        })
    }

    /**
     * 获取物品
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 物品
     */
    fun getItemStacks(player: OfflinePlayer?, data: HashMap<String, String>?): List<ItemStack> {
        val itemStacks = ArrayList<ItemStack>()
        getSection(player, data)?.also { config ->
            ItemUtils.loadItems(itemStacks, config.getStringList("Items"), player)
        }
        return itemStacks
    }
}