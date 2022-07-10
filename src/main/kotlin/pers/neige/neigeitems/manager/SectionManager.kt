package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.section.impl.StringsParser
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ConfigUtils.getConfigSections
import java.io.File

object SectionManager {
    // 全部全局节点文件
    val files: ArrayList<File> = ConfigUtils.getAllFiles("GlobalSections")
    val globalSectionMap = HashMap<String, ArrayList<ConfigurationSection>>()
    val globalSections = HashMap<String, ConfigurationSection>()
    // 加载节点解析器
    val sectionParsers = HashMap<String, SectionParser>()
    init {
        // 加载全部全局节点
        for (file in files) {
            val fileName = file.name
            globalSectionMap[fileName] = ArrayList()
            for (config in file.getConfigSections()) {
                globalSections[config.name] = config
                globalSectionMap[fileName]?.add(config)
            }
        }
        sectionParsers["strings"] = StringsParser
    }
}