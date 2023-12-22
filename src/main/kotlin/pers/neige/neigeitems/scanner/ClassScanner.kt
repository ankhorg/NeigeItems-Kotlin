package pers.neige.neigeitems.scanner

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.annotation.Schedule
import pers.neige.neigeitems.utils.ListenerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.*
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.jar.JarFile

class ClassScanner(
    private val plugin: Plugin,
    private val packageName: String = plugin.javaClass.`package`.name,
    private val except: MutableSet<String>,
) {
    /**
     * 所有插件类
     */
    val classes = mutableListOf<Class<*>>()

    /**
     * 待加载监听器方法
     */
    private val listenerMethods = mutableListOf<Method>()

    /**
     * ENABLE 加载方法
     */
    private val enableMethods = mutableListOf<Method>()

    /**
     * ACTIVE 加载方法
     */
    private val activeMethods = mutableListOf<Method>()

    /**
     * DISABLE 加载方法
     */
    private val disableMethods = mutableListOf<Method>()

    /**
     * 周期触发方法
     */
    private val scheduleMethods = mutableListOf<Method>()

    /**
     * enable阶段调用
     */
    fun onEnable() {
        loadClasses(plugin, packageName, except)
        loadMethods()
        listenerMethods.forEach { method ->
            val annotation = method.getAnnotation(Listener::class.java)
            val eventClass = method.parameterTypes[0]
            val eventPriority = annotation.eventPriority
            val ignoreCancelled = annotation.ignoreCancelled
            ListenerUtils.registerListener(
                eventClass as Class<Event>,
                eventPriority,
                plugin,
                ignoreCancelled
            ) {
                method.invokeSafe(it)
            }
        }

        enableMethods.forEach { method ->
            try {
                method.invokeSafe()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }

        sync(plugin) {
            activeMethods.forEach { method ->
                try {
                    method.invokeSafe()
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        }

        scheduleMethods.forEach { method ->
            val annotation = method.getAnnotation(Schedule::class.java)
            if (annotation.async) {
                asyncTimer(plugin, 0, annotation.period) {
                    method.invokeSafe()
                }
            } else {
                syncTimer(plugin, 0, annotation.period) {
                    method.invokeSafe()
                }
            }
        }
    }

    /**
     * disable阶段调用
     */
    fun onDisable() {
        disableMethods.forEach { method ->
            try {
                method.invokeSafe()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
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
                if (method.isAnnotationPresent(Listener::class.java)) {
                    if (method.parameterCount == 1 && Event::class.java.isAssignableFrom(method.parameterTypes[0])) {
                        if (method.check("错误的监听器注解")) {
                            listenerMethods.add(method)
                        }
                    }
                }
                // 生命周期方法
                if (method.isAnnotationPresent(Awake::class.java)) {
                    if (method.parameterCount == 0) {
                        if (method.check("错误的生命周期注解")) {
                            val annotation = method.getAnnotation(Awake::class.java)
                            when (annotation.lifeCycle) {
                                Awake.LifeCycle.ENABLE -> enableMethods.add(method)
                                Awake.LifeCycle.ACTIVE -> activeMethods.add(method)
                                Awake.LifeCycle.DISABLE -> disableMethods.add(method)
                            }
                        }
                    }
                }
                // 周期触发方法
                if (method.isAnnotationPresent(Schedule::class.java)) {
                    if (method.parameterCount == 0) {
                        if (method.check("错误的周期触发注解")) {
                            scheduleMethods.add(method)
                        }
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
            entryName = entryName.substring(0, entry.name.length - 6)
            try {
                classes.add(Class.forName(entryName))
            } catch (error: NoClassDefFoundError) {
            } catch (error: Throwable) {
                plugin.logger.warning("error occurred while get Class $entryName")
                error.printStackTrace()
            }
        }
    }

    private fun Method.check(msg: String): Boolean {
        if (!isAccessible) {
            isAccessible = true
        }
        return if (!Modifier.isStatic(modifiers)) {
            try {
                declaringClass.getDeclaredField("INSTANCE").type == declaringClass
            } catch (error: Throwable) {
                plugin.logger.warning(msg)
                error.printStackTrace()
                false
            }
        } else {
            true
        }
    }

    private fun Method.invokeSafe(vararg args: Any) {
        if (Modifier.isStatic(modifiers)) {
            invoke(null, *args)
        } else {
            val instance = try {
                declaringClass.getDeclaredField("INSTANCE").get(null)
            } catch (error: Throwable) {
                plugin.logger.warning("$declaringClass 不是kotlin单例, 无法执行非静态方法 $name")
                error.printStackTrace()
                null
            }
            if (instance != null) {
                invoke(instance, *args)
            }
        }
    }
}