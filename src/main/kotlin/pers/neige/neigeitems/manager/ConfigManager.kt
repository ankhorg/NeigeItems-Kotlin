package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.annotations.Awake
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrNull
import pers.neige.neigeitems.utils.ConfigUtils.saveResourceNotWarn
import taboolib.common.platform.Platform
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart
import java.io.File
import java.io.InputStreamReader

/**
 * 配置文件管理器, 用于管理config.yml文件, 对其中缺少的配置项进行主动补全, 同时释放默认配置文件
 */
object ConfigManager {
    /**
     * 获取默认Config
     */
    private val originConfig: FileConfiguration =
        plugin.getResource("config.yml")?.let {
            val reader = InputStreamReader(it, "UTF-8")
            val config = YamlConfiguration.loadConfiguration(reader)
            reader.close()
            config
        } ?: YamlConfiguration()

    /**
     * 获取配置文件
     */
    val config get() = plugin.config

    var debug = config.getBoolean("Main.Debug", false)
    var comboInterval = config.getLong("ItemAction.comboInterval", 500)
    var removeNBTWhenGive = config.getBoolean("ItemOwner.removeNBTWhenGive")
    var updateInterval = config.getLong("ItemUpdate.interval", -1)

    /**
     * 加载默认配置文件
     */
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE)
    private fun saveResource() {
        if (getFileOrNull("Expansions") == null) {
            plugin.saveResourceNotWarn("Expansions${File.separator}CustomAction.js")
            plugin.saveResourceNotWarn("Expansions${File.separator}CustomItemEditor.js")
            plugin.saveResourceNotWarn("Expansions${File.separator}CustomSection.js")
            plugin.saveResourceNotWarn("Expansions${File.separator}DefaultSection.js")
            plugin.saveResourceNotWarn("Expansions${File.separator}ExampleExpansion.js")
        }
        if (getFileOrNull("GlobalSections") == null) {
            plugin.saveResourceNotWarn("GlobalSections${File.separator}ExampleSection.yml")
        }
        if (getFileOrNull("ItemActions") == null) {
            plugin.saveResourceNotWarn("ItemActions${File.separator}ExampleAction.yml")
        }
        if (getFileOrNull("ItemPacks") == null) {
            plugin.saveResourceNotWarn("ItemPacks${File.separator}ExampleItemPack.yml")
        }
        if (getFileOrNull("Items") == null) {
            plugin.saveResourceNotWarn("Items${File.separator}ExampleItem.yml")
        }
        if (getFileOrNull("Scripts") == null) {
            plugin.saveResourceNotWarn("Scripts${File.separator}ExampleScript.js")
            plugin.saveResourceNotWarn("Scripts${File.separator}ItemTime.js")
        }
        plugin.saveDefaultConfig()
        // 加载bstats
        val metrics = Metrics(15750, plugin.description.version, Platform.BUKKIT)
        metrics.addCustomChart(SingleLineChart("items") {
            ItemManager.itemIds.size
        })
        // 对当前Config查缺补漏
        loadConfig()
    }

    /**
     * 对当前Config查缺补漏
     */
    fun loadConfig() {
        originConfig.getKeys(true).forEach { key ->
            if (!plugin.config.contains(key)) {
                plugin.config.set(key, originConfig.get(key))
            } else {
                val completeValue = originConfig.get(key)
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

    /**
     * 重载配置管理器
     */
    fun reload() {
        plugin.reloadConfig()
        loadConfig()
        debug = config.getBoolean("Main.Debug", false)
        comboInterval = config.getLong("ItemAction.comboInterval", 500)
        removeNBTWhenGive = config.getBoolean("ItemOwner.removeNBTWhenGive")
        updateInterval = config.getLong("ItemUpdate.interval", -1)
    }

    fun debug(text: String) {
        if (debug) {
            Bukkit.getLogger().info(text)
        }
    }
}
