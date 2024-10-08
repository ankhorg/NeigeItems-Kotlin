package pers.neige.neigeitems.maven

import java.io.File

/**
 * 本地依赖加载器
 */
class LocalDependency(val path: String) {
    fun load() {
        JarLoader.load(File(path))
    }
}
