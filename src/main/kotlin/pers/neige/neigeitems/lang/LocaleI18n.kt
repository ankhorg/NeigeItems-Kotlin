package pers.neige.neigeitems.lang

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.parseObject
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion
import pers.neige.neigeitems.manager.ConfigManager.language
import pers.neige.neigeitems.utils.FileUtils.createFile
import pers.neige.neigeitems.utils.FileUtils.sha1
import java.io.File
import java.io.IOException
import java.net.URI
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object LocaleI18n {
    private val logger = LoggerFactory.getLogger(LocaleI18n::class.java.simpleName)
    private val releaseVersionRegex = Regex("""^\d+(\.\d+)+$""")
    private val minecraftVersion = Bukkit.getServer().version.let { version ->
        version.substring(version.indexOf("(MC: ") + 5).let { temp ->
            temp.substring(0, temp.lastIndexOf(")"))
        }
    }
    private val langFileSuffix = if (CbVersion.v1_13_R1.isSupport) ".json" else ".lang"
    private var provider: TranslationProvider? = null
    private var providers: MutableMap<String, TranslationProvider> = ConcurrentHashMap()

    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE, priority = EventPriority.LOW)
    private fun init() {
        // 下载并加载当前版本对应的语言文件
        provider = loadLanguage(language)
    }

    /**
     * 加载对应语言文件, 可能耗时较久, 注意不要在主线程调用.
     *
     * @param language 对应语言
     */
    @JvmStatic
    fun loadLanguage(language: String): TranslationProvider? {
        return loadTranslationProvider(minecraftVersion, language)?.also {
            providers[language] = it
        }
    }

    /**
     * 加载所有语言文件, 可能耗时较久, 注意不要在主线程调用.
     */
    @JvmStatic
    fun loadAllLanguage() {
        loadAllTranslationProvider(minecraftVersion)?.forEach { (language, provider) ->
            providers[language] = provider
        }
    }

    /**
     * 根据翻译键获取翻译内容, 默认采用NeigeItems的config.yml中配置的语言.
     *
     * @param key 翻译键
     * @return 翻译内容
     */
    @JvmStatic
    fun translate(key: String): String {
        // TranslatableComponent(key).toPlainText()是BC给的英文保底
        return provider?.translate(key) ?: TranslatableComponent(key).toPlainText()
    }

    /**
     * 根据翻译键获取对应语言的翻译内容.
     *
     * @param key 翻译键
     * @param language 对应语言, 需要预先通过LocaleI18n.INSTANCE.load(language)进行语言文件加载
     * @return 翻译内容
     */
    @JvmStatic
    fun translate(key: String, language: String): String {
        // TranslatableComponent(key).toPlainText()是BC给的英文保底
        return providers[language]?.translate(key) ?: TranslatableComponent(key).toPlainText()
    }

    /**
     * 将所有版本的所有语言文件下载至服务端根目录的lang文件夹, 可能耗时较久, 注意不要在主线程调用.
     */
    private fun loadAllVersions() {
        getAllReleaseVersionInfo().forEach {
            it.downloadAllLangFile()
        }
    }

    /**
     * 将当前版本的所有语言文件下载至服务端根目录的lang文件夹, 可能耗时较久, 注意不要在主线程调用.
     */
    private fun loadCurrentVersion() {
        getCurrentVersionInfo()?.downloadAllLangFile()
    }

    /**
     * 获取所有版本信息.
     * 这是一个json object array, 里面的每个json object形似{"id": "xxx", "type": "release/snapshot", "url": "xxx", "time": "xxx", "releaseTime": "xxx"}.
     * id是版本号, type代表发布或快照版本, url转到这个版本的所有内容.
     *
     * @return 所有版本信息
     */
    private fun getAllVersionInfo(): JSONArray {
        // 这网址返回一个json object, latest.release对应最新发布版本, latest.snapshot对应最新快照版本
        // versions对应一个json object array, 里面的每个json object形似{"id": "xxx", "type": "release/snapshot", "url": "xxx", "time": "xxx", "releaseTime": "xxx"}
        // id是版本号, type代表发布或快照版本, url转到这个版本的所有内容
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json".connectAndParseObject()
            .getJSONArray("versions")
    }

    /**
     * 获取所有发布版本信息.
     *
     * @return 所有发布版本信息
     */
    private fun getAllReleaseVersionInfo(): List<JSONObject> {
        return getAllVersionInfo()
            // 筛选所有稳定版本
            .filter {
                releaseVersionRegex.matches((it as JSONObject).getString("id"))
            }.map { it as JSONObject }
    }

    /**
     * 获取所有快照版本信息.
     *
     * @return 所有快照版本信息
     */
    private fun getAllSnapshotVersionInfo(): List<JSONObject> {
        return getAllVersionInfo()
            // 筛选所有快照版本
            .filter {
                !releaseVersionRegex.matches((it as JSONObject).getString("id"))
            }.map { it as JSONObject }
    }

    /**
     * 获取指定版本信息.
     *
     * @param id 版本号
     * @return 指定版本信息
     */
    private fun getVersionInfo(id: String): JSONObject? {
        return getAllVersionInfo().first {
            (it as JSONObject).getString("id") == id
        } as? JSONObject
    }

    /**
     * 获取当前版本信息.
     *
     * @return 当前版本信息
     */
    private fun getCurrentVersionInfo(): JSONObject? {
        return getVersionInfo(minecraftVersion)
    }

    /**
     * 根据版本信息获取资源文件信息.
     *
     * @this 版本信息
     * @return 资源文件信息
     */
    private fun JSONObject.getAssetInfo(): JSONObject {
        // 根据url获取当前版本信息
        return getString("url").connectAndParseObject()
            // 根据资源索引url获取所有资源信息, 这玩意儿里面只有一个objects对应一个json object, 里面key是文件名, value存储了hash和文件大小
            .getJSONObject("assetIndex").getString("url").connectAndParseObject().getJSONObject("objects")
    }

    /**
     * 根据版本信息获取语言文件信息.
     *
     * @this 版本信息
     * @return 语言文件信息
     */
    private fun JSONObject.getLangInfo(): Map<String, JSONObject> {
        return getAssetInfo()
            // 根据key(即文件名)滤出语言文件
            .filter { it.key.startsWith("minecraft/lang/") }.mapValues { it.value as JSONObject }
    }

    /**
     * 下载文件.
     *
     * @this 版本信息
     */
    private fun downloadFile(fileName: String, fileInfo: JSONObject, version: String): File? {
        // 当前语言文件的hash
        val hash = fileInfo.getString("hash")
        // 当前文件名
        val simpleFileName = fileName.removePrefix("minecraft/lang/")
        // 服务端根目录目标文件
        val file = File("lang/$version/$simpleFileName")
        // 服务端根目录目标校验文件
        val sha1File = File("lang/$version/$simpleFileName.sha1")
        // 检测是否需要下载语言文件
        if (
        // 语言文件不存在
            !file.exists()
            // sha1校验文件不存在
            || !sha1File.exists()
            // sha1校验不通过
            || file.sha1() != sha1File.readText()
        ) {
            try {
                file.createFile()
                sha1File.createFile()
                logger.info("Downloading {} {}", version, simpleFileName)
                // 写入sha1
                sha1File.createFile().writeText(hash)
                // 写入文件
                file.writeText(
                    URI("https://resources.download.minecraft.net/${hash.substring(0, 2)}/$hash").toURL().readText()
                )
                // sha1校验
                if (file.sha1() != hash) {
                    sha1File.delete()
                    file.delete()
                    throw IllegalStateException("file " + file.name + " sha1 not match.")
                }
                logger.info("Successfully downloaded {} {}.", version, simpleFileName)
                return file
            } catch (e: IOException) {
                logger.info("Failed to download {} {}.", version, simpleFileName)
                return null
            }
        } else {
            logger.info("{} {} already exists, there is no need to download it again.", version, simpleFileName)
            return file
        }
    }

    /**
     * 根据版本信息下载所有语言文件.
     *
     * @this 版本信息
     */
    private fun JSONObject.downloadAllLangFile(): MutableMap<String, File> {
        return hashMapOf<String, File>().also {
            getLangInfo().forEach { (key, value) ->
                // 当前版本信息对应的mc版本
                val version = this.getString("id")
                val file = downloadFile(key, value, version)
                if (file != null) {
                    it[key.removePrefix("minecraft/lang/").removeSuffix(langFileSuffix)] = file
                }
            }
        }
    }

    /**
     * 下载当前版本对应的语言文件.
     */
    private fun downloadLangFile(version: String, language: String): File? {
        val versionInfo = getVersionInfo(version) ?: return null
        val fileName = "minecraft/lang/$language$langFileSuffix"
        val fileInfo = versionInfo.getLangInfo()[fileName] ?: return null
        return downloadFile(fileName, fileInfo, version)
    }

    /**
     * 下载对应版本对应的语言文件.
     */
    private fun loadTranslationProvider(version: String, language: String): TranslationProvider? {
        return downloadLangFile(
            version, language
        )?.let { if (CbVersion.v1_13_R1.isSupport) JsonProvider(it) else PropertiesProvider(it) }
    }

    /**
     * 下载对应版本的所有语言文件.
     */
    private fun loadAllTranslationProvider(version: String): Map<String, TranslationProvider>? {
        return getVersionInfo(version)?.downloadAllLangFile()
            ?.mapValues { if (CbVersion.v1_13_R1.isSupport) JsonProvider(it.value) else PropertiesProvider(it.value) }
    }

    private fun String.connectAndParseObject(): JSONObject {
        return URI(this).toURL().readText().parseObject()
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