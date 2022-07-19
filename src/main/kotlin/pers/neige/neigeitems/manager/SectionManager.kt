package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.section.impl.*
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import pers.neige.neigeitems.utils.ConfigUtils.loadConfiguration
import java.io.File
import java.io.FileReader
import java.util.concurrent.ConcurrentHashMap

object SectionManager {
    // <文件名, 该文件中的所有节点>
    val globalSectionMap = HashMap<String, ConfigurationSection>()
    // <节点ID, 节点>
    val globalSections = HashMap<String, Any>()
    // 所有节点解析器
    val sectionParsers = ConcurrentHashMap<String, SectionParser>()

    init {
        loadGlobalSections()
        loadBasicParser()
        loadCustomSections()
    }

    // 用于加载节点解析器
    fun loadParser(sectionParser: SectionParser) {
        sectionParsers[sectionParser.id] = sectionParser
    }

    // 重载节点管理器
    fun reload() {
        globalSectionMap.clear()
        globalSections.clear()
        sectionParsers.clear()
        loadGlobalSections()
        loadBasicParser()
        loadCustomSections()
    }

    // 加载全部全局节点
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

    // 加载自定义节点
    private fun loadCustomSections() {
        for (file in getAllFiles("CustomSections")) {
            // 没有main这个函数就会报错
            try {
                pers.neige.neigeitems.script.CompiledScript(FileReader(file)).invokeFunction("main", null)
            } catch (error: NoSuchMethodException) {}
        }
    }

    // 加载基础节点
    private fun loadBasicParser() {
        loadParser(CalculationParser)
        loadParser(InheritParser)
        loadParser(JavascriptParser)
        loadParser(NumberParser)
        loadParser(StringsParser)
        loadParser(WeightParser)
        loadParser(PapiParser)
    }
}