package pers.neige.neigeitems.maven

import java.nio.file.Paths

class LocalDependency(val path: String) {
    fun load() {
        JarLoader.load(Paths.get(path).toFile())
    }
}
