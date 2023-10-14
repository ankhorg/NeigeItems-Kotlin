package pers.neige.neigeitems

import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.annotation.Schedule
import pers.neige.neigeitems.utils.ListenerUtils
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.jar.JarFile

/**
 * 插件主类
 */
object NeigeItems : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }
    /**
     * 所有插件类
     */
    private val classes = mutableListOf<Class<*>>()

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

    override fun onEnable() {
        try {
            val itemStack = ItemStack(Material.STONE)
            val nbtItemStack = NbtItemStack(itemStack)
            val nbt = nbtItemStack.orCreateTag
            nbt.putString("test", "test")
            nbt.getString("test")
        } catch (error: Throwable) {
            plugin.logger.warning("插件NBT前置库未正常加载依赖, 本插件不支持包括但不限于 Mohist/Catserver/Arclight 等混合服务端, 对于每个大版本, 本插件仅支持最新小版本, 如支持 1.19.4 但不支持 1.19.2, 请选用正确的服务端, 或卸载本插件")
            plugin.logger.warning("The plugin's NBT pre-requisite library failed to load. This plugin does not support mixed server platforms including but not limited to Mohist/Catserver/Arclight, etc. For each major version, this plugin only supports the latest minor version. For example, it supports 1.19.4 but not 1.19.2. Please use the correct server platform or uninstall this plugin.")
            val pluginManager = Bukkit.getPluginManager()
            pluginManager.getPlugin("NeigeItems")?.let {
                pluginManager.disablePlugin(it)
            }
            return
        }

        // 扫描加载所有插件类
        val pluginClassLoaderClass = Class.forName("org.bukkit.plugin.java.PluginClassLoader")
        val classLoader: ClassLoader = NeigeItems::class.java.classLoader
        if (classLoader.javaClass != pluginClassLoaderClass) {
            plugin.logger.warning("NeigeItems can only install in PluginClassLoader")
            val pluginManager = Bukkit.getPluginManager()
            pluginManager.getPlugin("NeigeItems")?.let {
                pluginManager.disablePlugin(it)
            }
            return
        }
        val jarField = pluginClassLoaderClass.getDeclaredField("jar")
        jarField.isAccessible = true
        val pluginJar = jarField[classLoader] as JarFile
        val entries = pluginJar.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            var entryName = entry.name.replace('/', '.')
            if (
                entryName.endsWith(".class")
                && !entryName.contains("$")
                && entryName.startsWith("pers.neige.neigeitems")
                && !entryName.startsWith("pers.neige.neigeitems.libs")
                && !entryName.startsWith("pers.neige.neigeitems.taboolib")
            ) {
                entryName = entryName.substring(0, entry.name.length - 6)
                try {
                    classes.add(Class.forName(entryName))
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        }

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

        listenerMethods.forEach { method ->
            val annotation = method.getAnnotation(Listener::class.java)
            val eventClass = method.parameterTypes[0]
            val eventPriority = annotation.eventPriority
            val ignoreCancelled = annotation.ignoreCancelled
            ListenerUtils.registerListener(
                eventClass as Class<Event>,
                eventPriority,
                ignoreCancelled
            ) {
                method.invokeSafe(it)
            }
        }

        enableMethods.forEach { method ->
            method.invokeSafe()
        }

        Bukkit.getScheduler().runTask(plugin, Runnable {
            activeMethods.forEach { method ->
                method.invokeSafe()
            }
        })

        scheduleMethods.forEach { method ->
            val annotation = method.getAnnotation(Schedule::class.java)
            if (annotation.async) {
                Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
                    method.invokeSafe()
                }, 0, annotation.period)
            } else {
                Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
                    method.invokeSafe()
                }, 0, annotation.period)
            }
        }
    }

    override fun onDisable() {
        disableMethods.forEach { method ->
            method.invokeSafe()
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
            invoke(declaringClass.getDeclaredField("INSTANCE").get(null), *args)
        }
    }
}