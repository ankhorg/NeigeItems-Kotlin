package pers.neige.neigeitems.maven

import pers.neige.neigeitems.JvmHacker
import sun.misc.Unsafe
import java.io.File
import java.lang.invoke.MethodHandle
import java.net.URL

/**
 * jar文件加载器
 */
object JarLoader {
    private val unsafe: Unsafe = JvmHacker.unsafe()

    private val ucp: Any = unsafe.getObject(
        this::class.java.classLoader,
        unsafe.objectFieldOffset(
            // sun.misc.Launcher$AppClassLoader
            try {
                this::class.java.classLoader.javaClass.getDeclaredField("ucp")
            } catch (e: NoSuchFieldException) {
                this::class.java.classLoader.javaClass.superclass.getDeclaredField("ucp")
            }
        )
    )

    private val addURLMethodHandle: MethodHandle =
        JvmHacker.lookup().unreflect(ucp.javaClass.getDeclaredMethod("addURL", URL::class.java))


    /**
     * 加载jar文件
     *
     * @param file 待加载jar文件
     */
    @JvmStatic
    fun load(file: File): File {
        addURLMethodHandle.invoke(ucp, file.toURI().toURL())
        return file
    }
}