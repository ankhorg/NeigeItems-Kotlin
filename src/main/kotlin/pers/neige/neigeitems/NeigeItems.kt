package pers.neige.neigeitems

import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.SimplePluginManager
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin

/**
 * 插件主类
 */
object NeigeItems : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }

    val bukkitScheduler by lazy { Bukkit.getScheduler() }

    override fun onEnable() {
        try {
            Class.forName("com.alibaba.fastjson2.filter.Filter")
        } catch (error: Throwable) {
            plugin.logger.warning("插件未正常加载依赖, 请前往 https://github.com/ankhorg/NeigeItems-Kotlin/releases 下载-all后缀版本的插件, 该版本插件已将依赖打包至本体")
            plugin.logger.warning("The plugin failed to load its dependencies properly. Please visit https://github.com/ankhorg/NeigeItems-Kotlin/releases and download the plugin with the \"-all\" suffix version. This version of the plugin includes the dependencies packaged within the main body of the plugin.")
            val pluginManager = Bukkit.getPluginManager()
            pluginManager.getPlugin("NeigeItems")?.let {
                pluginManager.disablePlugin(it)
            }
            return
        }
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
    }
}