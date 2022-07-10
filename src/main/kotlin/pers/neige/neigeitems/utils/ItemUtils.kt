package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ItemUtils.inherit

object ItemUtils {
    // 进行模板继承
    @JvmStatic
    fun ConfigurationSection.inherit(originConfigSection: ConfigurationSection): ConfigurationSection {
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
                                this.set(key, currentSection.get(key))
                            }
                        }
                    }
                }
                is String -> {
                    // 仅指定单个模板ID，进行全局继承
                    val inheritConfigSection = ItemManager.getConfig(inheritInfo)
                    inheritConfigSection?.getKeys(false)?.forEach { key ->
                        this.set(key, inheritConfigSection.get(key))
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
                                this.set(key, currentSection.get(key))
                            }
                        }
                    }
                }
            }
        }
        // 覆盖物品配置
        originConfigSection?.getKeys(true)?.forEach { key ->
            val value = originConfigSection?.get(key)
            if (value !is MemorySection) {
                this.set(key, originConfigSection.get(key))
            }
        }
        return this
    }

    // 全局节点加载
    @JvmStatic
    fun ConfigurationSection.loadGlobalSections(): ConfigurationSection {
        // 如果调用了全局节点
        if (this.contains("globalsections")) {
            // 获取全局节点ID
            val globalSectionIds = this.getStringList("globalsections")
            // 针对每个试图调用的全局节点
            globalSectionIds.forEach {
                when (val sections = SectionManager.globalSectionMap[it]) {
                    // 对于节点调用
                    null -> {
                        SectionManager.globalSections[it]?.let { section ->
                            this.set("sections.$it", section)
                        }
                    }
                    // 对于节点文件调用
                    else -> {
                        for (section in sections) {
                            this.set("sections.$it", section)
                        }
                    }
                }
            }
        }
        return this
    }
}