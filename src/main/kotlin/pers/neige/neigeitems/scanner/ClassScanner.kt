package pers.neige.neigeitems.scanner

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.annotation.*
import pers.neige.neigeitems.annotation.Awake.LifeCycle
import pers.neige.neigeitems.annotation.Awake.LifeCycle.*
import pers.neige.neigeitems.utils.ListenerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.asyncTimer
import pers.neige.neigeitems.utils.SchedulerUtils.syncTimer
import java.lang.reflect.Field
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
    private val listenerMethods = ArrayList<EasyMethod>()
    private val scheduleMethods = ArrayList<EasyMethod>()
    private val awakeMethods = ConcurrentHashMap<LifeCycle, EnumMap<EventPriority, MutableList<EasyMethod>>>()
    private val customTaskMethods = ConcurrentHashMap<String, EnumMap<EventPriority, MutableList<EasyMethod>>>()
    private val customFields = ConcurrentHashMap<String, MutableList<EasyField>>()

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
        listenerMethods.forEach { easyMethod ->
            val method = easyMethod.method
            val annotation = method.getAnnotation(Listener::class.java)
            ListenerUtils.registerListener(
                method.parameterTypes[0].asSubclass(Event::class.java),
                annotation.eventPriority,
                plugin,
                annotation.ignoreCancelled
            ) {
                easyMethod.invoke(it)
            }
        }
        runAwakeTask(ENABLE)
        Bukkit.getScheduler().runTask(plugin, Runnable {
            runAwakeTask(ACTIVE)
        })
        scheduleMethods.forEach { easyMethod ->
            try {
                val annotation = easyMethod.method.getAnnotation(Schedule::class.java)
                if (annotation.async) {
                    asyncTimer(plugin, 0, annotation.period) {
                        easyMethod.invoke()
                    }
                } else {
                    syncTimer(plugin, 0, annotation.period) {
                        easyMethod.invoke()
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

    fun <T> getCustomFields(fieldType: String, type: Class<out T>, allowNull: Boolean = false): MutableList<Any?> {
        val results = arrayListOf<Any?>()
        customFields[fieldType]?.forEach {
            val value = it.get()
            if (value == null) {
                if (allowNull) results.add(null)
            } else if (type.isInstance(value)) {
                results.add(it.get())
            }
        }
        return results
    }

    private fun Collection<List<EasyMethod>>.runMethods() {
        forEach { it -> it.forEach { it.invoke() } }
    }

    private fun loadMethods() {
        // 扫描注解
        classes.forEach { clazz ->
            val methods = try {
                clazz.declaredMethods
            } catch (_: Throwable) {
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
            val fields = try {
                clazz.declaredFields
            } catch (_: Throwable) {
                null
            }
            fields?.forEach { field ->
                // 自定义任务方法
                if (field.isAnnotationPresent(CustomField::class.java)) {
                    val finalField = field.check("错误的自定义字段注解")
                    if (finalField != null) {
                        val annotation = field.getAnnotation(CustomField::class.java)
                        customFields.computeIfAbsent(annotation.fieldType) { ArrayList() }.add(finalField)
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
            } catch (_: NoSuchMethodError) {
            } catch (error: Throwable) {
                plugin.logger.warning("error occurred while get Class $entryName")
                error.printStackTrace()
            }
        }
    }

    private fun Method.check(msg: String): EasyMethod? {
        if (!isAccessible) {
            isAccessible = true
        }
        return try {
            EasyMethod.parse(this)
        } catch (error: Throwable) {
            plugin.logger.warning(msg + "${declaringClass.canonicalName}#${name}")
            error.printStackTrace()
            null
        }
    }

    private fun Field.check(msg: String): EasyField? {
        if (!isAccessible) {
            isAccessible = true
        }
        return try {
            EasyField.parse(this)
        } catch (error: Throwable) {
            plugin.logger.warning(msg + "${declaringClass.canonicalName}#${name}")
            error.printStackTrace()
            null
        }
    }

    class EasyMethod(val method: Method, val instance: Any?) {
        companion object {
            @JvmStatic
            fun parse(method: Method): EasyMethod? {
                // kotlin伴生类
                if (method.declaringClass.canonicalName.endsWith(".Companion")) {
                    // 伴生类静态方法, 将被复制至主类, 伴生类方法可忽略, 不进行操作
                    if (method.isAnnotationPresent(JvmStatic::class.java)) {
                        return null
                    } else {
                        // 伴生类非静态方法, 单例为主类的静态Companion字段
                        val instance = Class.forName(method.declaringClass.canonicalName.removeSuffix(".Companion"))
                            .getDeclaredField("Companion").get(null)
                        if (instance == null || instance.javaClass != method.declaringClass) {
                            throw UnsupportedOperationException()
                        }
                        return EasyMethod(method, instance)
                    }
                } else {
                    // 普通静态方法
                    if (Modifier.isStatic(method.modifiers)) {
                        return EasyMethod(method, null)
                    } else {
                        // 普通的kotlin单例方法, 单例为当前类的静态INSTANCE字段
                        val instance = method.declaringClass.getDeclaredField("INSTANCE").get(null)
                        if (instance == null || instance.javaClass != method.declaringClass) {
                            throw UnsupportedOperationException()
                        }
                        return EasyMethod(method, instance)
                    }
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

        fun invoke(vararg args: Any?) {
            try {
                method.invoke(instance, *args)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    class EasyField(val field: Field, val instance: Any?) {
        companion object {
            @JvmStatic
            fun parse(field: Field): EasyField? {
                // 普通静态字段
                if (Modifier.isStatic(field.modifiers)) {
                    return EasyField(field, null)
                } else {
                    // 普通的kotlin单例字段, 单例为当前类的静态INSTANCE字段
                    val instance = field.declaringClass.getDeclaredField("INSTANCE").get(null)
                    if (instance == null || instance.javaClass != field.declaringClass) {
                        throw UnsupportedOperationException()
                    }
                    return EasyField(field, instance)
                }
            }
        }

        fun get(): Any? {
            return field.get(instance)
        }
    }
}