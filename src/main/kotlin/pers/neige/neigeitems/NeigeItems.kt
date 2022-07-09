package pers.neige.neigeitems

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.common.platform.function.releaseResourceFile
import java.io.File

object NeigeItems : Plugin() {

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
        releaseResourceFile("GlobalSections" + File.separator + "ExampleSection.yml")
        releaseResourceFile("ItemActions" + File.separator + "ExampleAction.yml")
        releaseResourceFile("Items" + File.separator + "ExampleItem.yml")
        releaseResourceFile("Scripts" + File.separator + "ExampleScript.js")
        releaseResourceFile("config.yml")
    }
}