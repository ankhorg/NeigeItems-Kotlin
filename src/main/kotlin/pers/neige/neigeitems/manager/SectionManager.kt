package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.section.impl.*
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import pers.neige.neigeitems.utils.ConfigUtils.loadConfiguration
import pers.neige.neigeitems.utils.SchedulerUtils.syncLater
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 全局节点 & 节点解析器管理器
 *
 * @constructor 构建全局节点 & 节点解析器管理器
 */
object SectionManager {
    /**
     * 获取全部全局节点文件
     */
    val files: ArrayList<File> = getAllFiles("GlobalSections")

    /**
     * 获取<文件名-该文件中的所有节点>对应map
     */
    val globalSectionMap = HashMap<String, ConfigurationSection>()

    /**
     * 获取<节点ID-节点>对应map
     */
    val globalSections = HashMap<String, Any>()

    /**
     * 获取所有节点解析器
     */
    val sectionParsers = ConcurrentHashMap<String, SectionParser>()

    init {
        // 加载全部全局节点
        loadGlobalSections()
        // 加载基础节点解析器
        loadBasicParser()
        // 加载自定义节点解析器
        loadCustomSections()
    }

    /**
     * 用于加载节点解析器
     *
     * @param sectionParser 节点解析器
     */
    fun loadParser(sectionParser: SectionParser) {
        sectionParsers[sectionParser.id] = sectionParser
    }

    /**
     * 重载节点管理器
     */
    fun reload() {
        files.clear()
        files.addAll(getAllFiles("GlobalSections"))
        globalSectionMap.clear()
        globalSections.clear()
        // 清不得, 单独重载Items的时候如果清了这玩意儿, Expansion注册的自定义节点就补不回来了
//        sectionParsers.clear()
        loadGlobalSections()
        loadBasicParser()
        loadCustomSections()
    }

    /**
     * 检测替换PAPI变量
     */
    @Awake(LifeCycle.ACTIVE)
    private fun update() {
        // 延迟3秒执行, 等待PAPI扩展加载
        syncLater(60) {
            HookerManager.papiHooker?.let {
                for (file: File in files) {
                    // 仅加载.yml文件
                    if (!file.name.endsWith(".yml")) continue
                    val text = file.readText()
                    if (HookerManager.papiHooker.hasPapi(text)) {
                        reload()
                        ItemManager.reload()
                        return@syncLater
                    }
                }
                for (file: File in ItemManager.files) {
                    // 仅加载.yml文件
                    if (!file.name.endsWith(".yml")) continue
                    val text = file.readText()
                    if (HookerManager.papiHooker.hasPapi(text)) {
                        reload()
                        ItemManager.reload()
                        return@syncLater
                    }
                }
            }
        }
    }

    /**
     * 加载全部全局节点
     */
    private fun loadGlobalSections() {
        for (file in files) {
            // 仅加载.yml文件
            if (!file.name.endsWith(".yml")) continue
            // 将文件中所有的有效 %xxx_xxx% 替换为 <papi::xxx_xxx>
            HookerManager.papiHooker?.let {
                val text = file.readText()
                if (HookerManager.papiHooker.hasPapi(text)) {
                    file.writeText(HookerManager.papiHooker.toSection(text))
                }
            }
            val fileName = file.path.replace("plugins${File.separator}NeigeItems${File.separator}GlobalSections${File.separator}", "")
            val config = file.loadConfiguration()
            globalSectionMap[fileName] = config
            for (key in config.getKeys(false)) {
                config.get(key)?.let {
                    globalSections[key] = it
                }
            }
        }
    }

    /**
     * 加载基础节点解析器
     */
    private fun loadBasicParser() {
        CalculationParser.register()
        ChanceParser.register()
        CheckParser.register()
        FastCalcParser.register()
        GaussianParser.register()
        GradientParser.register()
        InheritParser.register()
        JavascriptParser.register()
        JoinParser.register()
        NumberParser.register()
        PapiParser.register()
        RepeatParser.register()
        StringsParser.register()
        WeightDeclareParser.register()
        WeightJoinParser.register()
        WeightParser.register()
        WhenParser.register()
    }

    /**
     * 加载自定义节点解析器
     */
    private fun loadCustomSections() {
        for (file in getAllFiles("CustomSections")) {
            // 防止某个脚本出错导致加载中断
            try {
                pers.neige.neigeitems.script.CompiledScript(file).invoke("main", null)
            } catch (_: Throwable) {}
        }
    }
}