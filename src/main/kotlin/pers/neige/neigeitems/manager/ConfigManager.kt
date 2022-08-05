package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.NeigeItems.plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart
import java.io.File
import java.io.InputStreamReader

// 配置文件管理器, 用于管理config.yml文件, 对其中缺少的配置项进行主动补全, 同时释放默认配置文件
object ConfigManager {
    // 默认Config
    private val originConfig: FileConfiguration =
        plugin.getResource("config.yml")?.let { YamlConfiguration.loadConfiguration(InputStreamReader(it, "UTF-8")) } ?: YamlConfiguration()

    // 用于获取配置文件
    val config get() = plugin.config

    // 加载默认配置文件
    @Awake(LifeCycle.INIT)
    fun saveResource() {
        plugin.saveResource("CustomActions${File.separator}CustomAction.js", false)
        plugin.saveResource("CustomSections${File.separator}CustomSection.js", false)
        plugin.saveResource("CustomSections${File.separator}DefaultSection.js", false)
        plugin.saveResource("GlobalSections${File.separator}ExampleSection.yml", false)
        plugin.saveResource("ItemActions${File.separator}ExampleAction.yml", false)
        plugin.saveResource("Items${File.separator}ExampleItem.yml", false)
        plugin.saveResource("Scripts${File.separator}ExampleScript.js", false)
        plugin.saveDefaultConfig()
        // 加载bstats
        val metrics = Metrics(15750, plugin.description.version, Platform.BUKKIT)
        metrics.addCustomChart(SingleLineChart("items") {
            ItemManager.itemIds.size
        })
        metrics.addCustomChart(SingleLineChart("sections") {
            SectionManager.globalSections.size
        })
        metrics.addCustomChart(SingleLineChart("custom-sections") {
            SectionManager.sectionParsers.size - 7
        })
        metrics.addCustomChart(SingleLineChart("scripts") {
            ScriptManager.compiledScripts.size
        })
    }

    // 对当前Config查缺补漏
    @Awake(LifeCycle.LOAD)
    fun loadConfig() {
        originConfig.getKeys(true).forEach { key ->
            if (!plugin.config.contains(key)) {
                plugin.config.set(key, ConfigManager.originConfig.get(key))
            } else {
                val completeValue = ConfigManager.originConfig.get(key)
                val value = plugin.config.get(key)
                if (completeValue is ConfigurationSection && value !is ConfigurationSection) {
                    plugin.config.set(key, completeValue)
                } else {
                    plugin.config.set(key, value)
                }
            }
        }
        plugin.saveConfig()
    }

    // 重载配置管理器
    fun reload() {
        plugin.reloadConfig()
        loadConfig()
    }
}