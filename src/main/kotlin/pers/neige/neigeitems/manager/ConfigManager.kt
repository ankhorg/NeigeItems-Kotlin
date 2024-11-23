package pers.neige.neigeitems.manager

import org.bstats.bukkit.Metrics
import org.bstats.charts.SingleLineChart
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventPriority
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrNull
import pers.neige.neigeitems.utils.ConfigUtils.loadConfiguration
import pers.neige.neigeitems.utils.ConfigUtils.saveResourceNotWarn
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * 配置文件管理器, 用于管理config.yml文件, 对其中缺少的配置项进行主动补全, 同时释放默认配置文件
 */
object ConfigManager {
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
    val configFile = File(NeigeItems.getInstance().dataFolder, File.separator + "config.yml")

    var debug = config.getBoolean("Main.Debug", false)
    var updateCheck = config.getBoolean("Main.UpdateCheck", true)
    var newDataFormat = config.getBoolean("Main.NewDataFormat", false)
    var comboInterval = config.getLong("ItemAction.comboInterval", 500)
    var removeNBTWhenGive = config.getBoolean("ItemOwner.removeNBTWhenGive")
    var updateInterval = config.getLong("ItemUpdate.interval", -1)
    var language = config.getString("Language", "zh_cn")!!
    var forceSync = config.getBoolean("ItemDurability.forceSync", false)
    var checkInventory = config.getBoolean("ItemCheck.checkInventory", true)

    /**
     * 加载默认配置文件
     */
    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE, priority = EventPriority.LOWEST)
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
        // 加载配置
        reload()
    }

    private fun checkConfigStatus() {
        // JavaPlugin中提供的config已经将原始config设为默认状态, 因此无法通过其检测config完整度
        val currentConfig = configFile.loadConfiguration()
        var changed = false
        originConfig.getKeys(true).forEach { key ->
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, originConfig.get(key))
                changed = true
            } else {
                val completeValue = originConfig.get(key)
                if (completeValue is ConfigurationSection && currentConfig.get(key) !is ConfigurationSection) {
                    currentConfig.set(key, completeValue)
                    changed = true
                }
            }
        }
        if (changed) {
            currentConfig.save(configFile)
        }
    }

    /**
     * 对当前Config查缺补漏
     */
    fun loadConfig() {
        debug = config.getBoolean("Main.Debug", false)
        updateCheck = config.getBoolean("Main.UpdateCheck", true)
        newDataFormat = config.getBoolean("Main.NewDataFormat", false)
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
        // 对当前Config查缺补漏
        checkConfigStatus()
        // 重新加载config
        NeigeItems.getInstance().reloadConfig()
        // 重新加载配置项
        loadConfig()
    }

    /**
     * config.yml 中 Main.Debug 为 true 时向后台发送文本
     */
    fun debug(text: String) {
        if (debug) {
            Bukkit.getLogger().info(text)
        }
    }
}
