package pers.neige.neigeitems.task

import com.alibaba.fastjson2.parseObject
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Schedule
import pers.neige.neigeitems.manager.ConfigManager.updateCheck
import java.net.URI

/**
 * 新版本检查任务
 */
object Updater {
    @JvmStatic
    private val logger = LoggerFactory.getLogger(Updater::class.java.simpleName)

    private val VERSION_REGEX = Regex("\\d+\\.\\d+\\.\\d+")
    val currentVersion = VERSION_REGEX.find(NeigeItems.getInstance().description.version)?.value
    var latestVersion = currentVersion

    @JvmStatic
    @Schedule(period = 72000, async = true)
    private fun schedule() {
        if (updateCheck) {
            kotlin.runCatching {
                latestVersion = VERSION_REGEX.find(
                    URI("https://api.github.com/repos/ankhorg/NeigeItems-Kotlin/releases/latest").toURL().readText()
                        .parseObject().getString("tag_name")
                )?.value

                if (latestVersion != currentVersion) {
                    logger.info("发现新版本: {}", latestVersion)
                    logger.info("链接: https://github.com/ankhorg/NeigeItems-Kotlin/releases/latest")
                }
            }
        }
    }
}