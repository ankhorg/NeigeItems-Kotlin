package pers.neige.neigeitems

import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.annotations.Listener
import pers.neige.neigeitems.utils.ListenerUtils
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin
import java.util.jar.JarFile

/**
 * 插件主类
 */
object NeigeItems : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }

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

        val classes = mutableListOf<Class<*>>()

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
                classes.add(Class.forName(entryName))
            }
        }

        classes.forEach{ clazz ->
            val methods = try {
                clazz.declaredMethods
            } catch (error: Throwable) {
                null
            }
            methods?.forEach { method ->
                if (method.isAnnotationPresent(Listener::class.java)) {
                    if (method.parameterCount == 1 && Event::class.java.isAssignableFrom(method.parameterTypes[0])) {
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
                            method.invoke(null, it)
                        }
                    }
                }
            }
        }
    }
}