package pers.neige.neigeitems.manager

import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.section.impl.*
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import pers.neige.neigeitems.utils.ConfigUtils.loadConfiguration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object SectionManager {
    // 全部全局节点文件
    val files: ArrayList<File> = getAllFiles("GlobalSections")
    val globalSectionMap = HashMap<String, ArrayList<Any>>()
    val globalSections = HashMap<String, Any>()
    // 加载节点解析器
    val sectionParsers = ConcurrentHashMap<String, SectionParser>()

    fun loadParser(sectionParser: SectionParser) {
        sectionParsers[sectionParser.id] = sectionParser
    }
    init {
        // 加载全部全局节点
        for (file in files) {
            val fileName = file.name
            globalSectionMap[fileName] = ArrayList()
            val config = file.loadConfiguration()
            for (key in config.getKeys(false)) {
                config.get(key)?.let {
                    globalSections[key] = it
                    globalSectionMap[fileName]?.add(it)
                }
            }
        }
        loadParser(CalculationParser)
        loadParser(JavascriptParser)
        loadParser(NumberParser)
        loadParser(StringsParser)
        loadParser(WeightParser)
    }
}