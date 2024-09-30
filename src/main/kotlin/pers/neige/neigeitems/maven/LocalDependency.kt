package pers.neige.neigeitems.maven

import java.io.File

class LocalDependency(val path: String) {
    fun load() {
        JarLoader.load(File(path))
    }
}
