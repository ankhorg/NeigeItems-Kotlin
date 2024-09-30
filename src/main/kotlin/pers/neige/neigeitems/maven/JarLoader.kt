package pers.neige.neigeitems.maven

import org.bukkit.Bukkit
import pers.neige.neigeitems.JvmHacker
import sun.misc.Unsafe
import java.io.File
import java.lang.invoke.MethodHandle
import java.net.URL

object JarLoader {
    private val unsafe: Unsafe = JvmHacker.unsafe()

    private val ucp: Any = unsafe.getObject(
        this::class.java.classLoader,
        unsafe.objectFieldOffset(
            // sun.misc.Launcher$AppClassLoader
            Bukkit::class.java.classLoader.javaClass.getDeclaredField("ucp")
        )
    )

    private val addURLMethodHandle: MethodHandle =
        JvmHacker.lookup().unreflect(ucp.javaClass.getDeclaredMethod("addURL", URL::class.java))


    @JvmStatic
    fun load(file: File): File {
        addURLMethodHandle.invoke(ucp, file.toURI().toURL())
        return file
    }
}