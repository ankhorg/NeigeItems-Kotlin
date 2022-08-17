package pers.neige.neigeitems.item

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.utils.ConfigUtils.loadGlobalSections

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
    val sections: ConfigurationSection? = configSection.getConfigurationSection("sections")
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
}