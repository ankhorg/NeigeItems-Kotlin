package pers.neige.neigeitems.manager

import org.bstats.bukkit.Metrics
import org.bstats.charts.SingleLineChart
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrNull
import pers.neige.neigeitems.utils.ConfigUtils.saveResourceNotWarn
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * 配置文件管理器, 用于管理config.yml文件, 对其中缺少的配置项进行主动补全, 同时释放默认配置文件
 */
object ConfigManager {
    /**
     * 获取默认Config
     */
    private val originConfig: FileConfiguration =
        NeigeItems.getInstance().getResource("config.yml")?.use { input ->
            InputStreamReader(input, StandardCharsets.UTF_8).use { reader ->
                YamlConfiguration.loadConfiguration(reader)
            }
        } ?: YamlConfiguration()

    /**
     * 获取配置文件
     */
    val config get() = NeigeItems.getInstance().config

    var debug = config.getBoolean("Main.Debug", false)
    var updateCheck = config.getBoolean("Main.UpdateCheck", true)
    var comboInterval = config.getLong("ItemAction.comboInterval", 500)
    var removeNBTWhenGive = config.getBoolean("ItemOwner.removeNBTWhenGive")
    var updateInterval = config.getLong("ItemUpdate.interval", -1)
    var language = config.getString("Language", "zh_cn")!!
    var forceSync = config.getBoolean("ItemDurability.forceSync", false)
    var checkInventory = config.getBoolean("ItemCheck.checkInventory", true)

    /**
     * 加载默认配置文件
     */
    fun saveResource() {
        if (getFileOrNull("Expansions") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("Expansions${File.separator}CustomAction.js")
            NeigeItems.getInstance().saveResourceNotWarn("Expansions${File.separator}CustomItemEditor.js")
            NeigeItems.getInstance().saveResourceNotWarn("Expansions${File.separator}CustomSection.js")
            NeigeItems.getInstance().saveResourceNotWarn("Expansions${File.separator}DefaultSection.js")
            NeigeItems.getInstance().saveResourceNotWarn("Expansions${File.separator}ExampleExpansion.js")
        }
        if (getFileOrNull("Functions") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("Functions${File.separator}ExampleFunction.yml")
        }
        if (getFileOrNull("GlobalSections") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("GlobalSections${File.separator}ExampleSection.yml")
        }
        if (getFileOrNull("ItemActions") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("ItemActions${File.separator}ExampleAction.yml")
        }
        if (getFileOrNull("ItemPacks") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("ItemPacks${File.separator}ExampleItemPack.yml")
        }
        if (getFileOrNull("Items") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("Items${File.separator}ExampleItem.yml")
        }
        if (getFileOrNull("Scripts") == null) {
            NeigeItems.getInstance().saveResourceNotWarn("Scripts${File.separator}ExampleScript.js")
            NeigeItems.getInstance().saveResourceNotWarn("Scripts${File.separator}ItemTime.js")
        }
        NeigeItems.getInstance().saveDefaultConfig()
        // 加载bstats
        val metrics = Metrics(NeigeItems.getInstance(), 15750)
        metrics.addCustomChart(SingleLineChart("items") { ItemManager.itemIds.size })
        // 对当前Config查缺补漏
        loadConfig()
    }

    /**
     * 对当前Config查缺补漏
     */
    fun loadConfig() {
        originConfig.getKeys(true).forEach { key ->
            if (!NeigeItems.getInstance().config.contains(key)) {
                NeigeItems.getInstance().config.set(key, originConfig.get(key))
            } else {
                val completeValue = originConfig.get(key)
                val value = NeigeItems.getInstance().config.get(key)
                if (completeValue is ConfigurationSection && value !is ConfigurationSection) {
                    NeigeItems.getInstance().config.set(key, completeValue)
                } else {
                    NeigeItems.getInstance().config.set(key, value)
                }
            }
        }
        NeigeItems.getInstance().saveConfig()
        debug = config.getBoolean("Main.Debug", false)
        updateCheck = config.getBoolean("Main.UpdateCheck", true)
        comboInterval = config.getLong("ItemAction.comboInterval", 500)
        removeNBTWhenGive = config.getBoolean("ItemOwner.removeNBTWhenGive")
        updateInterval = config.getLong("ItemUpdate.interval", -1)
        language = config.getString("Language", "zh_cn")!!
        forceSync = config.getBoolean("ItemDurability.forceSync", false)
        checkInventory = config.getBoolean("ItemCheck.checkInventory", true)
    }

    /**
     * 重载配置管理器
     */
    fun reload() {
        NeigeItems.getInstance().reloadConfig()
        loadConfig()
    }

    fun debug(text: String) {
        if (debug) {
            Bukkit.getLogger().info(text)
        }
    }
}
