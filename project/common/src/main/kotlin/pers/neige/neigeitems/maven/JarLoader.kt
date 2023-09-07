package pers.neige.neigeitems.maven

import sun.misc.Unsafe
import java.io.File
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.net.URL

object JarLoader {
    private val unsafe: Unsafe = let {
        val theUnsafe = Unsafe::class.java.getDeclaredField("theUnsafe")
        theUnsafe.isAccessible = true
        theUnsafe[null] as Unsafe
    }

    private val ucp: Any = unsafe.getObject(
        this::class.java.classLoader,
        unsafe.objectFieldOffset(
            try {
                this::class.java.classLoader.javaClass.getDeclaredField("ucp")
            } catch (e: NoSuchFieldException) {
                this::class.java.classLoader.javaClass.superclass.getDeclaredField("ucp")
            }
        )
    )

    private val addURLMethodHandle: MethodHandle = let {
        val field = MethodHandles.Lookup::class.java.getDeclaredField("IMPL_LOOKUP")
        (unsafe.getObject(
            unsafe.staticFieldBase(field),
            unsafe.staticFieldOffset(field)
        ) as MethodHandles.Lookup).unreflect(ucp.javaClass.getDeclaredMethod("addURL", URL::class.java))
    }


    @JvmStatic
    fun load(file: File): File {
        addURLMethodHandle.invoke(ucp, file.toURI().toURL())
        return file
    }
}