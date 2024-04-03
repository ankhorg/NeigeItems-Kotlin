package pers.neige.neigeitems.script

import org.bukkit.Bukkit
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import java.io.File
import java.io.Reader

class ScriptExpansion : CompiledScript {
    /**
     * 构建JavaScript脚本扩展
     *
     * @property reader js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(reader: Reader) : super(reader)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property file js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(file: File) : super(file)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property script js脚本文本
     * @constructor JavaScript脚本扩展
     */
    constructor(script: String) : super(script)

    override fun loadLib() {
        scriptEngine.eval(
            """
                const Bukkit = Packages.org.bukkit.Bukkit
                const Command = Packages.pers.neige.neigeitems.script.tool.ScriptCommand
                const EventPriority = Packages.org.bukkit.event.EventPriority
                const Listener = Packages.pers.neige.neigeitems.script.tool.ScriptListener
                const Placeholder = Packages.pers.neige.neigeitems.script.tool.ScriptPlaceholder
                const Task = Packages.pers.neige.neigeitems.script.tool.ScriptTask
                const MavenDependency = Packages.pers.neige.neigeitems.maven.MavenDependency
                const LocalDependency = Packages.pers.neige.neigeitems.maven.LocalDependency
                
                const ActionUtils = Packages.pers.neige.neigeitems.utils.ActionUtils
                const ConfigUtils = Packages.pers.neige.neigeitems.utils.ConfigUtils
                const FileUtils = Packages.pers.neige.neigeitems.utils.FileUtils
                const ItemUtils = Packages.pers.neige.neigeitems.utils.ItemUtils
                const JsonUtils = Packages.pers.neige.neigeitems.utils.JsonUtils
                const LangUtils = Packages.pers.neige.neigeitems.utils.LangUtils
                const PlayerUtils = Packages.pers.neige.neigeitems.utils.PlayerUtils
                const SamplingUtils = Packages.pers.neige.neigeitems.utils.SamplingUtils
                const ScriptUtils = Packages.pers.neige.neigeitems.utils.ScriptUtils
                const SectionUtils = Packages.pers.neige.neigeitems.utils.SectionUtils
                const StringUtils = Packages.pers.neige.neigeitems.utils.StringUtils
                
                const NbtUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils
                const DamageEventUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.DamageEventUtils
                const EnchantmentUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EnchantmentUtils
                const EntityUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityUtils
                const EntityItemUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityItemUtils
                const EntityPlayerUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils
                const InventoryUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.InventoryUtils
                const SpigotInventoryUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.SpigotInventoryUtils
                const PaperInventoryUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PaperInventoryUtils
                const ServerUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ServerUtils
                const TranslationUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils
                const WorldUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils
                const CommandUtils = Packages.pers.neige.neigeitems.utils.CommandUtils
                const ListenerUtils = Packages.pers.neige.neigeitems.utils.ListenerUtils
                const ListUtils = Packages.pers.neige.neigeitems.utils.ListUtils
                const SchedulerUtils = Packages.pers.neige.neigeitems.utils.SchedulerUtils
                
                const AnimationType = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.animation.AnimationType
                const EnumHand = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.EnumHand
                const SpawnerBuilder = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.spawner.SpawnerBuilder
                
                const ActionManager = Packages.pers.neige.neigeitems.manager.ActionManager.INSTANCE
                const ConfigManager = Packages.pers.neige.neigeitems.manager.ConfigManager.INSTANCE
                const HookerManager = Packages.pers.neige.neigeitems.manager.HookerManager
                const ItemEditorManager = Packages.pers.neige.neigeitems.manager.ItemEditorManager.INSTANCE
                const ItemManager = Packages.pers.neige.neigeitems.manager.ItemManager.INSTANCE
                const ItemPackManager = Packages.pers.neige.neigeitems.manager.ItemPackManager.INSTANCE
                
                const plugin = Packages.pers.neige.neigeitems.NeigeItems.INSTANCE.plugin
                const pluginManager = Bukkit.getPluginManager()
                const scheduler = Bukkit.getScheduler()
                
                let sync = SchedulerUtils.sync
                let async = SchedulerUtils.async
            """.trimIndent()
        )
    }

    /**
     * 执行指定函数
     *
     * @param function 函数名
     * @param expansionName 脚本名称(默认为unnamed)
     */
    fun run(function: String, expansionName: String = "unnamed") {
        if (nashornHooker.isFunction(scriptEngine, function)) {
            try {
                invoke(function, null)
            } catch (error: Throwable) {
                Bukkit.getLogger().info(
                    ConfigManager.config.getString("Messages.expansionError")
                        ?.replace("{expansion}", expansionName)
                        ?.replace("{function}", function)
                )
                error.printStackTrace()
            }
        }
    }
}