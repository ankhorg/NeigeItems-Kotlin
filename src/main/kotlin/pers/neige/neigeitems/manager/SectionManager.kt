package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.section.impl.*
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import pers.neige.neigeitems.utils.ConfigUtils.loadConfiguration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 全局节点 & 节点解析器管理器
 *
 * @constructor 构建全局节点 & 节点解析器管理器
 */
object SectionManager {
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
        globalSectionMap.clear()
        globalSections.clear()
        sectionParsers.clear()
        loadGlobalSections()
        loadBasicParser()
        loadCustomSections()
    }

    /**
     * 加载全部全局节点
     */
    private fun loadGlobalSections() {
        for (file in getAllFiles("GlobalSections")) {
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