package pers.neige.neigeitems.lang

import bot.inker.bukkit.nbt.internal.annotation.CbVersion
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.parseObject
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Bukkit
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ConfigManager.language
import pers.neige.neigeitems.utils.FileUtils.createFile
import pers.neige.neigeitems.utils.FileUtils.sha1
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

object LocaleI18n {
    private val minecraftVersion = Bukkit.getServer().version.let { version ->
        version.substring(version.indexOf("(MC: ") + 5).let { temp ->
            temp.substring(0, temp.lastIndexOf(")"))
        }
    }

    private val simpleFileName = if (CbVersion.v1_13_R1.isSupport) {
        "$language.json"
    } else {
        "$language.lang"
    }

    private val fileName = "minecraft/lang/$simpleFileName"

    private val file: File
        get() = File("lang/$minecraftVersion/$simpleFileName")

    private val sha1File: File
        get() = File("lang/$minecraftVersion/$simpleFileName.sha1")

    private var provider: TranslationProvider? = null

    @JvmStatic
    fun translate(key: String): String {
        // BC给的英文保底
        return provider?.translate(key) ?: TranslatableComponent(key).toPlainText()
    }

    fun init() {
        // 检测是否需要下载语言文件
        if (
        // 语言文件不存在
            !file.exists()
            // sha1校验文件不存在
            || !sha1File.exists()
            // sha1校验不通过
            || file.sha1() != sha1File.readText()
        ) {
            plugin.logger.info("Try to find $simpleFileName")
            // 所有版本信息
            "https://launchermeta.mojang.com/mc/game/version_manifest.json".connectAndParseObject()
                .getJSONArray("versions")
                // 获取当前版本信息
                .first {
                    (it as JSONObject).getString("id") == minecraftVersion
                }?.let { ver ->
                    (ver as JSONObject)
                        .getString("url")
                        .connectAndParseObject()
                        // 资源索引
                        .getJSONObject("assetIndex")
                        .getString("url")
                        .connectAndParseObject()
                        .getJSONObject("objects")
                        // 对应语言文件
                        .getJSONObject(fileName)
                        .getString("hash")
                        .let { hash ->
                            try {
                                plugin.logger.info("Downloading $simpleFileName")
                                // 写入sha1
                                sha1File.createFile().writeText(hash)
                                // 写入文件
                                file.createFile().writeText(
                                    URL(
                                        "https://resources.download.minecraft.net/${
                                            hash.substring(
                                                0,
                                                2
                                            )
                                        }/$hash"
                                    ).readText()
                                )
                                // sha1校验
                                if (file.sha1() != hash) {
                                    sha1File.delete()
                                    file.delete()
                                    throw IllegalStateException("file " + file.name + " sha1 not match.")
                                }
                                plugin.logger.info("Successfully downloaded $simpleFileName")
                            } catch (e: IOException) {
                                plugin.logger.info("Failed to download $simpleFileName")
                            }
                        }
                }
        }
        provider = file.let { if (CbVersion.v1_13_R1.isSupport) JsonProvider(it) else PropertiesProvider(it) }
    }

    private fun String.connectAndParseObject(): JSONObject {
        return URL(this).readText().parseObject()
    }

    interface TranslationProvider {
        fun translate(key: String): String?
    }

    private class PropertiesProvider(resourceFile: File) : TranslationProvider {
        val lang = Properties()

        override fun translate(key: String): String? {
            return lang.getProperty(key)
        }

        init {
            lang.load(resourceFile.reader())
        }
    }

    private class JsonProvider(resourceFile: File) : TranslationProvider {
        private val translations: MutableMap<String, String> =
            resourceFile.readText().parseObject<HashMap<String, String>>()

        override fun translate(key: String): String? {
            return translations[key]
        }
    }
}