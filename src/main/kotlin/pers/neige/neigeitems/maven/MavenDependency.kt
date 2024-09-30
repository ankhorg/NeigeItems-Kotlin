package pers.neige.neigeitems.maven

import org.slf4j.LoggerFactory
import pers.neige.neigeitems.utils.FileUtils.sha1
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * maven依赖
 *
 * @property libPath libs路径
 * @property groupId 组ID
 * @property artifactId 依赖ID
 * @property version 依赖版本
 * @property repos 仓库地址
 */
class MavenDependency {
    private val libPath: String
    private val groupId: String
    private val artifactId: String
    private val version: String
    private val repos: Array<String>

    constructor(
        libPath: String,
        groupId: String,
        artifactId: String,
        version: String,
        repos: Array<String>
    ) {
        this.libPath = libPath
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        this.repos = repos
    }

    constructor(
        groupId: String,
        artifactId: String,
        version: String,
        repos: Array<String>
    ) {
        this.libPath = "libs"
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        this.repos = repos
    }

    constructor(
        groupId: String,
        artifactId: String,
        version: String,
        repo: String
    ) {
        this.libPath = "libs"
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        this.repos = arrayOf(repo)
    }

    constructor(
        groupId: String,
        artifactId: String,
        version: String
    ) {
        this.libPath = "libs"
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        this.repos = arrayOf(
            "https://maven.aliyun.com/repository/public",
            "https://repo.maven.apache.org/maven2"
        )
    }

    /**
     * 加载maven依赖
     */
    fun load() {
        // 加载当前依赖
        JarLoader.load(getFileAndCheck())
    }

//    /**
//     * 加载maven依赖
//     */
//    fun loadDeep() {
//        // 获取pom文件
//        val pom = getFileAndCheck("pom")
//        // 获取项目信息
//        val model: Model = MavenXpp3Reader().read(FileReader(pom))
//        // 挨个加载依赖
//        for (dependency in model.dependencies) {
//            MavenDependency(
//                libPath,
//                dependency.groupId,
//                dependency.artifactId,
//                dependency.version,
//                repos
//            ).loadDeep()
//        }
//        // 加载当前依赖
//        JarLoader.load(getFileAndCheck("jar"))
//    }

    /**
     * 获取文件(获取不到则进行下载), 校验sha1(sha1不符则删除文件并报错), 返回文件
     *
     * @param extension 文件后缀
     * @return 对应文件
     */
    private fun getFileAndCheck(
        extension: String = "jar"
    ): File {
        // 获取文件
        return getFile(extension).also {
            // 比较期望sha1和实际sha1
            if (getFile("$extension.sha1").readText() != it.sha1()) {
                // 二者有差异则删除文件并报错
                it.delete()
                throw IllegalStateException("file " + it.name + " sha1 not match.")
            }
        }
    }

    /**
     * 根据后缀获取对应文件(获取不到则进行下载)
     *
     * @param extension 文件后缀
     * @return 对应文件
     */
    private fun getFile(
        extension: String
    ): File {
        // 获取文件
        return Paths.get(
            libPath,
            groupId.replace(".", File.separator),
            artifactId,
            version,
            "$artifactId-$version.$extension"
        ).toFile().also {
            // 获取不到就下载
            if (!it.exists()) {
                // 如果不存在父级目录则进行创建
                it.parentFile.mkdirs()

                // 遍历仓库链接尝试下载
                for (repoUrl in repos) {
                    // 构建目标链接
                    val url = URI(
                        "$repoUrl/${groupId.replace(".", "/")}/$artifactId/$version/$artifactId-$version.$extension"
                    ).toURL()
                    // 尝试下载
                    try {
                        // 开启链接
                        val connection = url.openConnection()
                        connection.connectTimeout = 5000
                        connection.readTimeout = 120000
                        connection.useCaches = true
                        if (extension == "jar") {
                            // 后台发送信息
                            logger.info("Downloading {}:{}:{}", groupId, artifactId, version)
                        }
                        // 将文件复制到对应目录
                        Files.copy(connection.getInputStream(), it.toPath(), StandardCopyOption.REPLACE_EXISTING)
                        if (extension == "jar") {
                            // 后台发送信息
                            logger.info("Successfully downloaded {}:{}:{}", groupId, artifactId, version)
                        }
                        // 文件下载成功，退出循环
                        return@also
                    } catch (e: IOException) {
                        // 如果下载失败，则继续迭代下一个仓库地址
                        logger.info(
                            "Failed to download {} {} {} {} from {}",
                            groupId,
                            artifactId,
                            version,
                            extension,
                            repoUrl
                        )
                    }
                }

                // 所有仓库地址均无法找到对应文件，抛出异常
                throw RuntimeException("Failed to download file: $groupId:$artifactId:$version:$extension")
            }
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(MavenDependency::class.java.simpleName)
    }
}