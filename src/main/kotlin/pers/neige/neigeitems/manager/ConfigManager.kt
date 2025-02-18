package pers.neige.neigeitems.manager

import org.bstats.bukkit.Metrics
import org.bstats.charts.SingleLineChart
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrNull
import pers.neige.neigeitems.utils.ConfigUtils.loadConfig
import pers.neige.neigeitems.utils.ConfigUtils.saveResourceNotWarn
import java.io.File

/**
 * 配置文件管理器, 用于管理config.yml文件, 对其中缺少的配置项进行主动补全, 同时释放默认配置文件
 */
object ConfigManager {
    /**
     * 获取配置文件
     */
    val config get() = NeigeItems.getInstance().config

    var debug = config.getBoolean("Main.Debug", false)
    var updateCheck = config.getBoolean("Main.UpdateCheck", true)
    var newDataFormat = config.getBoolean("Main.NewDataFormat", false)
    var comboInterval = config.getLong("ItemAction.comboInterval", 500)
    var removeNBTWhenGive = config.getBoolean("ItemOwner.removeNBTWhenGive")
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
        // 加载bstats
        val metrics = Metrics(NeigeItems.getInstance(), 15750)
        metrics.addCustomChart(SingleLineChart("items") { ItemManager.itemIds.size })
        // 加载配置
        reload()
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
        language = config.getString("Language", "zh_cn")!!
        forceSync = config.getBoolean("ItemDurability.forceSync", false)
        checkInventory = config.getBoolean("ItemCheck.checkInventory", true)
    }

    /**
     * 重载配置管理器
     */
    fun reload() {
        // 加载config
        NeigeItems.getInstance().loadConfig()
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
