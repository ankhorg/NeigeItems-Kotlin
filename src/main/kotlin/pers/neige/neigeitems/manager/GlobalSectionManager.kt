package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import pers.neige.neigeitems.utils.ConfigUtils.loadConfigSections
import java.io.File

object GlobalSectionManager {
    // 加载全部节点文件
    private val files: ArrayList<File> = getAllFiles("GlobalSections")
    // 获取所有全局节点配置文件
    val configs: MutableList<ConfigurationSection> = loadConfigSections(files)
    // {config, [id]}
    val globalSections: MutableMap<ConfigurationSection, MutableList<String>> = mutableMapOf<ConfigurationSection, MutableList<String>>()
    // 每个全局节点配置文件的文件名(转为YamlConfiguration后文件路径莫名其妙莫得了, 只能另开记录)
    val globalSectionFileNames: MutableList<String> = mutableListOf<String>()
    // [id]
    val globalSectionIDList: MutableList<String> = mutableListOf<String>()

    init {
        // 遍历所有全局节点配置文件
        for (config: ConfigurationSection in configs) {
            // 获取当前全局节点配置文件
            var list = mutableListOf<String>()
            // 获取当前文件内所有全局节点
            var configSections = loadConfigSections(config)
            // 记录节点ID
            configSections.forEach { section ->
                list.add(section.getName())
                globalSectionIDList.add(section.getName())
            }
            globalSections.put(config, list)
            globalSectionFileNames.add(files[index].getName())
        }
    }
}