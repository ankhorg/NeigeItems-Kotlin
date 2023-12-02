package pers.neige.neigeitems.command.subcommand

import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtNewMethod
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ExpansionManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.script.ScriptExpansion
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrCreate
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrNull
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream


object ExpansionBuild {
    val engine = HookerManager.nashornHooker.getGlobalEngine().also { engine ->
        // 加载顶级成员
        plugin.getResource("JavaScriptLib/build.js")?.use { input ->
            InputStreamReader(input, "UTF-8").use { reader ->
                engine.eval(reader)
            }
        }
    }

    // ni expansion build [扩展路径] > 将对应扩展打包为jar插件
    val build = subCommand {
        execute<CommandSender> { sender, _, _ ->
            async {
                help(sender)
            }
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ExpansionManager.expansions.keys().toList()
            }
            execute<CommandSender> { sender, _, argument ->
                async {
                    ExpansionManager.expansions[argument]?.also { script ->
                        val pluginName =
                            script.scriptEngine.get("name") as? String ?: argument.split(File.separator).last()
                                .split(".").first()
                        val authors = script.scriptEngine.get("authors") as? ArrayList<String>
                            ?: ArrayList<String>().also { it.add("unnamed") }
                        val version = script.scriptEngine.get("version") as? String ?: "1.0.0"
                        val apiVersion = script.scriptEngine.get("apiVersion") as? String ?: "1.13"
                        val depend = (script.scriptEngine.get("depend") as? ArrayList<String>
                            ?: ArrayList()).also { it.add("NeigeItems") }
                        val softdepend = script.scriptEngine.get("softdepend") as? ArrayList<String> ?: ArrayList()

                        val pool = ClassPool.getDefault()
                        pool.insertClassPath(ClassClassPath(JavaPlugin::class.java))
                        pool.insertClassPath(ClassClassPath(ExpansionManager::class.java))
                        pool.insertClassPath(ClassClassPath(ScriptExpansion::class.java))
                        pool.insertClassPath(ClassClassPath(Objects::class.java))
                        pool.insertClassPath(ClassClassPath(UUID::class.java))
                        pool.insertClassPath(ClassClassPath(Reader::class.java))
                        runCatching {
                            val clazz = pool.get("pers.neige.${pluginName.lowercase(Locale.getDefault())}.Main")
                            if (clazz.isFrozen) clazz.defrost()
                        }
                        val ctClass = pool.makeClass("pers.neige.${pluginName.lowercase(Locale.getDefault())}.Main")
                        ctClass.superclass = pool.get("org.bukkit.plugin.java.JavaPlugin")
                        val methodCode = """
                            public void onEnable() {
                                pers.neige.neigeitems.script.ScriptExpansion script = new pers.neige.neigeitems.script.ScriptExpansion((java.io.Reader)java.util.Objects.requireNonNull(getTextResource("Main.js")));
                                String pluginName = (String)script.getScriptEngine().get("name");
                                if (pluginName == null) {
                                    pluginName = java.util.UUID.randomUUID().toString();
                                }
                                pers.neige.neigeitems.manager.ExpansionManager.INSTANCE.addPermanentExpansion(pluginName, script);
                            }""".trimIndent()
                        val ctMethod = CtNewMethod.make(methodCode, ctClass)
                        ctClass.addMethod(ctMethod)

                        val customClasses = arrayListOf<CtClass>()

                        if (HookerManager.nashornHooker.isFunction(script.scriptEngine, "loadClasses")) {
                            try {
                                val classes = script.invoke("loadClasses", null, pool)
                                if (classes is Iterable<*>) {
                                    classes.forEach { clazz ->
                                        if (clazz is CtClass) {
                                            customClasses.add(clazz)
                                        }
                                    }
                                } else if (classes is CtClass) {
                                    customClasses.add(classes)
                                }
                            } catch (error: Throwable) {
                                Bukkit.getLogger().info(
                                    ConfigManager.config.getString("Messages.expansionError")
                                        ?.replace("{expansion}", pluginName)
                                        ?.replace("{function}", "loadClasses")
                                )
                                error.printStackTrace()
                            }
                        }

                        JarOutputStream(FileOutputStream(getFileOrCreate("Build${File.separator}$pluginName-$version.jar"))).use { outputStream ->
                            outputStream.putNextEntry(JarEntry(ctClass.name.replace(".", "/") + ".class"))
                            ByteArrayInputStream(ctClass.toBytecode()).use { it.copyTo(outputStream) }
                            outputStream.closeEntry()

                            customClasses.forEach { clazz ->
                                outputStream.putNextEntry(JarEntry(clazz.name.replace(".", "/") + ".class"))
                                ByteArrayInputStream(clazz.toBytecode()).use { it.copyTo(outputStream) }
                                outputStream.closeEntry()
                            }

                            outputStream.putNextEntry(JarEntry("Main.js"))
                            getFileOrNull("Expansions${File.separator}$argument")?.let { file ->
                                FileInputStream(file).use { it.copyTo(outputStream) }
                            }
                            outputStream.closeEntry()

                            for (file in ConfigUtils.getAllFiles("Expansions${File.separator}$pluginName")) {
                                outputStream.putNextEntry(JarEntry(file.name))
                                FileInputStream(file).use { it.copyTo(outputStream) }
                                outputStream.closeEntry()
                            }

                            outputStream.putNextEntry(JarEntry("plugin.yml"))
                            YamlConfiguration().also {
                                it.set("name", pluginName)
                                it.set("main", "pers.neige.${pluginName.lowercase(Locale.getDefault())}.Main")
                                it.set("authors", authors)
                                it.set("version", version)
                                it.set("api-version", apiVersion)
                                it.set("depend", depend)
                                it.set("softdepend", softdepend)
                                ByteArrayInputStream(it.saveToString().toByteArray(StandardCharsets.UTF_8)).use {
                                    it.copyTo(outputStream)
                                }
                            }
                            outputStream.closeEntry()

                            sender.sendLang(
                                "Messages.buildExpansionMessage", mapOf(
                                    Pair("{expansion}", argument),
                                    Pair("{file}", "$pluginName-$version.jar")
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}