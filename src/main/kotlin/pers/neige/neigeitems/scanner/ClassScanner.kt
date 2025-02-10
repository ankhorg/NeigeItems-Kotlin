package pers.neige.neigeitems.scanner

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.JvmHacker
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.annotation.Awake.LifeCycle
import pers.neige.neigeitems.annotation.Awake.LifeCycle.*
import pers.neige.neigeitems.annotation.CustomTask
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.annotation.Schedule
import pers.neige.neigeitems.utils.ListenerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.asyncTimer
import pers.neige.neigeitems.utils.SchedulerUtils.syncTimer
import java.lang.invoke.ConstantCallSite
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodType
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarFile

/**
 * 类扫描器
 */
class ClassScanner {
    private val plugin: Plugin
    private val packageName: String
    private val except: MutableSet<String>

    constructor(
        plugin: Plugin
    ) : this(plugin, plugin.javaClass.`package`.name, hashSetOf(plugin.javaClass.getPackage().name + ".libs"))

    constructor(
        plugin: Plugin, packageName: String
    ) : this(plugin, packageName, hashSetOf(plugin.javaClass.getPackage().name + ".libs"))

    constructor(
        plugin: Plugin, except: MutableSet<String>
    ) : this(plugin, plugin.javaClass.`package`.name, except)

    constructor(
        plugin: Plugin, packageName: String, except: MutableSet<String>
    ) {
        this.plugin = plugin
        this.packageName = packageName
        this.except = except
        scan()
        runAwakeTask(INIT)
    }

    /**
     * 所有插件类
     */
    val classes = mutableListOf<Class<*>>()
    private val listenerMethods = ArrayList<PackedMethod>()
    private val scheduleMethods = ArrayList<PackedMethod>()
    private val awakeMethods = ConcurrentHashMap<LifeCycle, EnumMap<EventPriority, MutableList<PackedMethod>>>()
    private val customTaskMethods = ConcurrentHashMap<String, EnumMap<EventPriority, MutableList<PackedMethod>>>()

    private fun scan() {
        loadClasses(plugin, packageName, except)
        loadMethods()
    }

    /**
     * load阶段调用
     */
    fun onLoad() {
        runAwakeTask(LOAD)
    }

    /**
     * enable阶段调用
     */
    fun onEnable() {
        listenerMethods.forEach { packedMethod ->
            val method = packedMethod.method
            val instance = packedMethod.instance
            val annotation = method.getAnnotation(Listener::class.java)
            val eventClass = method.parameterTypes[0].asSubclass(Event::class.java)
            val eventPriority = annotation.eventPriority
            val ignoreCancelled = annotation.ignoreCancelled
            ListenerUtils.registerListener(
                eventClass, eventPriority, plugin, ignoreCancelled
            ) {
                method.invoke(instance, it)
            }
        }
        runAwakeTask(ENABLE)
        Bukkit.getScheduler().runTask(plugin, Runnable {
            runAwakeTask(ACTIVE)
        })
        scheduleMethods.forEach { packedMethod ->
            try {
                val method = packedMethod.method
                val instance = packedMethod.instance
                val annotation = method.getAnnotation(Schedule::class.java)
                if (annotation.async) {
                    asyncTimer(plugin, 0, annotation.period) {
                        method.invoke(instance)
                    }
                } else {
                    syncTimer(plugin, 0, annotation.period) {
                        method.invoke(instance)
                    }
                }
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    /**
     * disable阶段调用
     */
    fun onDisable() {
        runAwakeTask(DISABLE)
    }

    private fun runAwakeTask(lifeCycle: LifeCycle) {
        awakeMethods[lifeCycle]?.values?.runMethods()
    }

    fun runCustomTask(taskId: String) {
        customTaskMethods[taskId]?.values?.runMethods()
    }

    private fun Collection<List<PackedMethod>>.runMethods() {
        forEach { it -> it.forEach { it.invoke() } }
    }

    private fun loadMethods() {
        // 扫描注解
        classes.forEach { clazz ->
            val methods = try {
                clazz.declaredMethods
            } catch (error: Throwable) {
                null
            }
            methods?.forEach { method ->
                // 监听器方法
                if (method.isAnnotationPresent(Listener::class.java) && method.parameterCount == 1 && Event::class.java.isAssignableFrom(
                        method.parameterTypes[0]
                    )
                ) {
                    val finalMethod = method.check("错误的监听器注解")
                    if (finalMethod != null) {
                        listenerMethods.add(finalMethod)
                    }
                }
                // 生命周期方法
                if (method.isAnnotationPresent(Awake::class.java) && method.parameterCount == 0) {
                    val finalMethod = method.check("错误的生命周期注解")
                    if (finalMethod != null) {
                        val annotation = method.getAnnotation(Awake::class.java)
                        awakeMethods.computeIfAbsent(annotation.lifeCycle) { EnumMap(EventPriority::class.java) }
                            .computeIfAbsent(annotation.priority) { ArrayList() }.add(finalMethod)
                    }
                }
                // 自定义任务方法
                if (method.isAnnotationPresent(CustomTask::class.java) && method.parameterCount == 0) {
                    val finalMethod = method.check("错误的自定义任务注解")
                    if (finalMethod != null) {
                        val annotation = method.getAnnotation(CustomTask::class.java)
                        customTaskMethods.computeIfAbsent(annotation.taskId) { EnumMap(EventPriority::class.java) }
                            .computeIfAbsent(annotation.priority) { ArrayList() }.add(finalMethod)
                    }
                }
                // 周期触发方法
                if (method.isAnnotationPresent(Schedule::class.java) && method.parameterCount == 0) {
                    val finalMethod = method.check("错误的周期触发注解")
                    if (finalMethod != null) {
                        scheduleMethods.add(finalMethod)
                    }
                }
            }
        }
    }

    private fun loadClasses(
        plugin: Plugin,
        packageName: String,
        except: MutableSet<String>,
    ) {
        // 扫描加载所有插件类
        val pluginClassLoaderClass = Class.forName("org.bukkit.plugin.java.PluginClassLoader")
        val classLoader: ClassLoader = plugin::class.java.classLoader
        if (classLoader.javaClass != pluginClassLoaderClass) {
            plugin.logger.warning("${plugin.name} can only install in PluginClassLoader")
            Bukkit.getPluginManager().disablePlugin(plugin)
            return
        }
        val jarField = pluginClassLoaderClass.getDeclaredField("jar")
        jarField.isAccessible = true
        val pluginJar = jarField[classLoader] as JarFile
        val entries = pluginJar.entries()
        loop@ while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            var entryName = entry.name.replace('/', '.')
            if (!entryName.endsWith(".class")) continue
            if (!entryName.startsWith(packageName)) continue
            for (name in except) {
                if (entryName.startsWith(name)) continue@loop
            }
            entryName = entryName.substring(0, entry.name.length - 6).removeSuffix(".ankh-invoke")
            try {
                classes.add(Class.forName(entryName))
            } catch (_: NoClassDefFoundError) {
            } catch (_: UnsupportedClassVersionError) {
            } catch (error: Throwable) {
                plugin.logger.warning("error occurred while get Class $entryName")
                error.printStackTrace()
            }
        }
    }

    private fun Method.check(msg: String): PackedMethod? {
        if (!isAccessible) {
            isAccessible = true
        }
        return try {
            PackedMethod(this)
        } catch (error: Throwable) {
            plugin.logger.warning(msg + "${declaringClass.canonicalName}#${name}")
            error.printStackTrace()
            null
        }
    }

    class PackedMethod(val method: Method) {
        val instance = if (Modifier.isStatic(method.modifiers)) {
            null
        } else {
            method.declaringClass.getDeclaredField("INSTANCE").get(null).also {
                if (it == null || it.javaClass != method.declaringClass) {
                    throw UnsupportedOperationException()
                }
            }
        }

        fun invoke() {
            try {
                method.invoke(instance)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    interface CallSite {

        fun invoke(): Any?

        fun invoke(arg: Any?): Any?

        companion object {
            @JvmStatic
            fun of(method: Method): CallSite {
                val instance = if (Modifier.isStatic(method.modifiers)) null
                else method.declaringClass.getDeclaredField("INSTANCE")[null]
                return if (instance == null) StaticCallSite(method) else SingletonCallSite(instance, method)
            }
        }
    }

    class StaticCallSite(method: Method) : ConstantCallSite(findTarget(method)), CallSite {
        override fun invoke(): Any? {
            return target.invoke()
        }

        override fun invoke(arg: Any?): Any? {
            return target.invoke(arg)
        }

        companion object {
            @JvmStatic
            private fun findTarget(method: Method): MethodHandle {
                try {
                    val t: MethodType = MethodType.methodType(method.returnType, method.parameterTypes)
                    return JvmHacker.lookup().findStatic(method.declaringClass, method.name, t)
                } catch (e: Throwable) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    private class SingletonCallSite(val instance: Any?, method: Method) : ConstantCallSite(findTarget(method)),
        CallSite {
        init {
            if (instance == null || instance.javaClass != method.declaringClass) {
                throw UnsupportedOperationException()
            }
        }

        override fun invoke(): Any? {
            return target.invoke(instance)
        }

        override fun invoke(arg: Any?): Any? {
            return target.invoke(instance, arg)
        }

        companion object {
            @JvmStatic
            private fun findTarget(method: Method): MethodHandle {
                try {
                    val t: MethodType = MethodType.methodType(method.returnType, method.parameterTypes)
                    return JvmHacker.lookup().findVirtual(method.declaringClass, method.name, t)
                } catch (e: Throwable) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}