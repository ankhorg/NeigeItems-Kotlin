package pers.neige.neigeitems.task

import com.alibaba.fastjson2.parseObject
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Schedule
import pers.neige.neigeitems.manager.ConfigManager.updateCheck
import java.net.URL

object Updater {
    private val VERSION_REGEX = Regex("\\d+\\.\\d+\\.\\d+")
    val currentVersion = VERSION_REGEX.find(NeigeItems.getInstance().description.version)?.value
    var latestVersion = currentVersion

    @JvmStatic
    @Schedule(period = 72000, async = true)
    fun schedule() {
        if (updateCheck) {
            kotlin.runCatching {
                latestVersion = VERSION_REGEX.find(
                    URL("https://api.github.com/repos/ankhorg/NeigeItems-Kotlin/releases/latest").readText()
                        .parseObject().getString("tag_name")
                )?.value

                if (latestVersion != currentVersion) {
                    NeigeItems.getInstance().logger.info("发现新版本: $latestVersion")
                    NeigeItems.getInstance().logger.info("链接: https://github.com/ankhorg/NeigeItems-Kotlin/releases/latest")
                }
            }
        }
    }
}