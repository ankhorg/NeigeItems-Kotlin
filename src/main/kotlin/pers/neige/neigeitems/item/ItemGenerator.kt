package pers.neige.neigeitems.item

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ConfigUtils.clone

class ItemGenerator (private val itemConfig: ItemConfig) {
    val id = itemConfig.id
    var configSection = YamlConfiguration() as ConfigurationSection
    val file = itemConfig.file
    val originConfigSection = itemConfig.configSection?.clone()

    init {
        inherit()
        loadGlobalSections()
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.set(id, configSection)
        val configString = tempConfigSection.saveToString()
        val hashCode = configString.hashCode()
    }

    // 进行模板继承
    private fun inherit() {
        // 初始化物品配置
        originConfigSection.getKeys(true).forEach { key ->
            val value = originConfigSection.get(key)
            if (value !is MemorySection) {
                configSection.set(key, originConfigSection.get(key))
            }
        }
        // 检测是否需要进行继承
        if (originConfigSection?.contains("inherit") == true) {
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
                            val currentSection = ItemManager.getConfig(value)
                            // 如果存在对应模板且模板存在对应键, 进行继承
                            if (currentSection != null && currentSection.contains(key)) {
                                configSection.set(key, currentSection.get(key))
                            }
                        }
                    }
                }
                is String -> {
                    // 仅指定单个模板ID，进行全局继承
                    val inheritConfigSection = ItemManager.getConfig(inheritInfo)
                    if (inheritConfigSection != null) {
                        configSection = inheritConfigSection
                    }
                }
                is List<*> -> {
                    // 顺序继承, 按顺序进行覆盖式继承
                    for (templateId in inheritInfo) {
                        // 逐个获取模板
                        val currentSection = ItemManager.getConfig(templateId as String)
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
    }

    // 全局节点加载
    private fun loadGlobalSections() {
        // 如果调用了全局节点
        if (configSection.contains("globalsections")) {
            // 获取全局节点ID
            val gSectionIds = configSection.getStringList("globalsections")
            // 针对每个试图调用的全局节点
            gSectionIds.forEach {
                // 在每个全局节点文件进行查找
                for (let index = 0; index < NeigeItemsData.globalSections.length; index++) {
                    // 获取[config, [id]]
                    const gSectionIDs = NeigeItemsData.globalSections[index]
                    // 如果调用的节点名与当前文件名重复, 直接调用文件内所有节点
                    if (NeigeItemsData.globalSectionFileNames[index] == it) {
                        for (let index = 0; index < gSectionIDs[1].length; index++) {
                            const it = gSectionIDs[1][index]
                            configSection.set("sections." + it, gSectionIDs[0].getConfigurationSection(it))
                        }
                        break
                    }
                    // 如果当前文件中存在相应节点
                    const gSectionIndex = gSectionIDs[1].indexOf(it)
                    if (gSectionIndex != -1) {
                        configSection.set("sections." + it, gSectionIDs[0].getConfigurationSection(it))
                        break
                    }
                }
            }
        }
        return configSection
    }
}
