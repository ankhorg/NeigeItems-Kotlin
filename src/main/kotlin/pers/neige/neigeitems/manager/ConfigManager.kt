package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart
import taboolib.platform.BukkitPlugin
import java.io.File
import java.io.InputStreamReader

object ConfigManager {
    // 默认Config
    private val originConfig: FileConfiguration =
        BukkitPlugin.getInstance().getResource("config.yml")?.let { YamlConfiguration.loadConfiguration(InputStreamReader(it, "UTF-8")) } ?: YamlConfiguration()

    val config get() = BukkitPlugin.getInstance().config

    // 加载默认配置文件
    @Awake(LifeCycle.INIT)
    fun saveResource() {
        BukkitPlugin.getInstance().saveResource("CustomSections${File.separator}CustomSection.js", false)
        BukkitPlugin.getInstance().saveResource("GlobalSections${File.separator}ExampleSection.yml", false)
        BukkitPlugin.getInstance().saveResource("ItemActions${File.separator}ExampleAction.yml", false)
        BukkitPlugin.getInstance().saveResource("Items${File.separator}ExampleItem.yml", false)
        BukkitPlugin.getInstance().saveResource("Scripts${File.separator}ExampleScript.js", false)
        BukkitPlugin.getInstance().saveDefaultConfig()
        val metrics = Metrics(15750, BukkitPlugin.getInstance().description.version, Platform.BUKKIT)
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
            if (!BukkitPlugin.getInstance().config.contains(key)) {
                BukkitPlugin.getInstance().config.set(key, ConfigManager.originConfig.get(key))
            } else {
                val completeValue = ConfigManager.originConfig.get(key)
                val value = BukkitPlugin.getInstance().config.get(key)
                if (completeValue is ConfigurationSection && value !is ConfigurationSection) {
                    BukkitPlugin.getInstance().config.set(key, completeValue)
                } else {
                    BukkitPlugin.getInstance().config.set(key, value)
                }
            }
        }
        BukkitPlugin.getInstance().saveConfig()
    }

    // 重载配置管理器
    fun reload() {
        BukkitPlugin.getInstance().reloadConfig()
        loadConfig()
    }
}