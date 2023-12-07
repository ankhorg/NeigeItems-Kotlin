import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import org.inksnow.ankhinvoke.gradle.ApplyReferenceTask
import org.inksnow.ankhinvoke.gradle.BuildMappingsTask

val taboolib_version: String by project

plugins {
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.inksnow.ankh-invoke-gradle-plugin") version "1.0.2-SNAPSHOT"
}

val realVersion = version

if (!System.getenv("CI").toBoolean()) {
    version = "dev"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")

    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/public")
        maven {
            url = uri("http://ptms.ink:8081/repository/releases/")
            isAllowInsecureProtocol = true
        }
    }
    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

        compileOnly("io.izzel.taboolib:common:$taboolib_version")
        compileOnly("io.izzel.taboolib:module-metrics:$taboolib_version")
        compileOnly("io.izzel.taboolib:platform-bukkit:$taboolib_version")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    mavenCentral()
    maven("https://r.irepo.space/maven/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/nms")
    // mmoitems
//    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
    maven {
        url = uri("http://ptms.ink:8081/repository/releases/")
        isAllowInsecureProtocol = true
    }
    maven("https://repo.tabooproject.org/storages/public/releases")
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
    compileOnly(fileTree("libs"))
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.ow2.asm:asm:9.4")
//    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")
//    compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")

    // ankh-invoke
    implementation("org.inksnow:ankh-invoke-bukkit:1.0.2-SNAPSHOT")

    // taboolib
    implementation("io.izzel.taboolib:common:$taboolib_version")
    implementation("io.izzel.taboolib:common-5:$taboolib_version")
    implementation("io.izzel.taboolib:module-chat:$taboolib_version")
    implementation("io.izzel.taboolib:module-configuration:$taboolib_version")
    implementation("io.izzel.taboolib:module-metrics:$taboolib_version")
    implementation("io.izzel.taboolib:platform-bukkit:$taboolib_version")

    // bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")
    // kotlin
    implementation(kotlin("stdlib"))
    // javassist
    implementation("org.javassist:javassist:3.20.0-GA")
    // callsite-nbt
//    implementation(fileTree("libs/callsite-nbt-1.0-dev-SNAPSHOT-fat.jar"))
//    implementation(project(":hooker-callsitenbt"))
    // openjdk-nashorn
    implementation(fileTree("libs/relocated-nashorn-15.4.jar"))
    // fastjson2
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.25")
    // multiple-string-searcher
    implementation("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
    // maven-model
    implementation("org.apache.maven:maven-model:3.9.1")
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")
        // kotlin
        relocate("kotlin.", "pers.neige.neigeitems.libs.kotlin.")
        // taboolib
        relocate("taboolib", "pers.neige.neigeitems.taboolib")
        // bstats
        relocate("org.bstats", "pers.neige.neigeitems.libs.bstats")
        // stringsearcher
        relocate("org.neosearch.stringsearcher", "pers.neige.neigeitems.libs.stringsearcher")
        // javassist
        relocate("javassist", "pers.neige.neigeitems.libs.javassist")
        // callsitenbt
        relocate("bot.inker.bukkit.nbt", "pers.neige.neigeitems.libs.bot.inker.bukkit.nbt")
        // maven-model
        relocate("org.codehaus.plexus.util", "pers.neige.neigeitems.libs.plexus.util")
        relocate("org.apache.maven.model", "pers.neige.neigeitems.libs.maven.model")
        // fastjson2
        relocate("com.alibaba.fastjson2", "pers.neige.neigeitems.libs.fastjson2")
        // asm
        relocate("org.objectweb.asm", "pers.neige.neigeitems.libs.asm9")
        // JvmHacker
        relocate("bot.inker.acj", "pers.neige.neigeitems.libs.acj")
    }
    kotlinSourcesJar {
        // include subprojects
        rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
    }
    build {
        dependsOn(shadowJar)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

tasks.create("apiJar", Jar::class) {
    dependsOn(tasks.compileJava, tasks.compileKotlin)
    from(tasks.compileJava, tasks.compileKotlin)

    // clean no-class file
    include { it.isDirectory or it.name.endsWith(".class") }
    includeEmptyDirs = false

    archiveClassifier.set("api")
}

tasks.assemble {
    dependsOn(tasks["apiJar"])
}

publishing {
    repositories {
        if (!System.getenv("CI").toBoolean()) {
            maven(buildDir.resolve("repo"))
        } else if (!version.toString().endsWith("-SNAPSHOT")) {
            maven("https://s0.blobs.inksnow.org/maven/") {
                credentials {
                    username = System.getenv("IREPO_USERNAME")
                    password = System.getenv("IREPO_PASSWORD")
                }
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            artifact(tasks["apiJar"]) {
                classifier = null
            }
        }
    }
}

// 向BukkitPlugin的<clinit>块开头插入CallSiteNbt.install(this.getClass())
// 删除被fastjson2连带打包的annotations类
fun final(name: String) {
//    val oldFile = File("$buildDir/libs/$name")
//    val newFile = File("$buildDir/libs/Modified$name")
    File("$buildDir/libs/$name.jar").delete()
    val oldFile = File("$buildDir/libs/$name-shaded.jar")
    val newFile = File("$buildDir/libs/Modified$name.jar")

    if (!oldFile.exists()) return

    JarOutputStream(FileOutputStream(newFile)).use { jarOutputStream ->
        JarFile(oldFile).use { jarFile ->
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryName = entry.name

                if (entryName == "pers/neige/neigeitems/taboolib/platform/BukkitPlugin.class") {
                    val targetClassBytes = jarFile.getInputStream(entry).readBytes()

                    val classReader = org.objectweb.asm.ClassReader(targetClassBytes)
                    val classWriter =
                        org.objectweb.asm.ClassWriter(classReader, org.objectweb.asm.ClassWriter.COMPUTE_MAXS)
                    val classVisitor =
                        object : org.objectweb.asm.ClassVisitor(org.objectweb.asm.Opcodes.ASM9, classWriter) {
                            override fun visitMethod(
                                access: Int,
                                name: String?,
                                descriptor: String?,
                                signature: String?,
                                exceptions: Array<out String>?
                            ): org.objectweb.asm.MethodVisitor? {
                                val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
                                if (name == "<clinit>") {
                                    return object :
                                        org.objectweb.asm.MethodVisitor(org.objectweb.asm.Opcodes.ASM9, methodVisitor) {
                                        override fun visitCode() {
//                                            visitLdcInsn(org.objectweb.asm.Type.getType("Lpers/neige/neigeitems/taboolib/platform/BukkitPlugin;"))
//                                            visitMethodInsn(
//                                                org.objectweb.asm.Opcodes.INVOKESTATIC,
//                                                "pers/neige/neigeitems/libs/bot/inker/bukkit/nbt/loader/CallSiteNbt",
//                                                "install",
//                                                "(Ljava/lang/Class;)V",
//                                                false
//                                            )
//                                            visitMethodInsn(
//                                                org.objectweb.asm.Opcodes.INVOKESTATIC,
//                                                "org/inksnow/ankhinvoke/example/ExampleMain",
//                                                "init",
//                                                "()V",
//                                                false
//                                            )
                                            visitMethodInsn(
                                                org.objectweb.asm.Opcodes.INVOKESTATIC,
                                                "pers/neige/neigeitems/NeigeItems",
                                                "init",
                                                "()V",
                                                false
                                            )
                                            super.visitCode()
                                        }
                                    }
                                }
                                return methodVisitor
                            }
                        }
                    classReader.accept(classVisitor, org.objectweb.asm.ClassReader.EXPAND_FRAMES)

                    jarOutputStream.putNextEntry(JarEntry(entryName))
                    jarOutputStream.write(classWriter.toByteArray())
                    jarOutputStream.closeEntry()
                } else {
                    if (!(entryName.startsWith("org/intellij/lang/annotations")
                                || entryName.startsWith("org/jetbrains/annotations"))
                    ) {
                        jarOutputStream.putNextEntry(entry)
                        jarFile.getInputStream(entry).copyTo(jarOutputStream)
                        jarOutputStream.closeEntry()
                    }
                }
            }
        }
    }
    oldFile.delete()
//    newFile.renameTo(oldFile)
    newFile.renameTo(File("$buildDir/libs/$name.jar"))
}

val finalTask = tasks.register("finalTask") {
    doLast {
//        final("${rootProject.name}-${project.property("version")}.jar")
        final("${rootProject.name}-${project.property("version")}")
    }
}

tasks.getByName("build").finalizedBy(finalTask)

// 将plugin.yml中的"${version}"替换为插件版本
tasks.register("replaceVersionInPluginYml") {
    doLast {
        val inputFile = File("src/main/resources/plugin.yml")
        val outputFile = File("build/resources/main/plugin.yml")

        val inputText = inputFile.readText()

        val projectVersion = realVersion.toString()
        val replacedText = inputText.replace("\${version}", projectVersion)

        outputFile.writeText(replacedText)
    }
}

tasks.named("assemble") {
    dependsOn("replaceVersionInPluginYml")
}

tasks.create<BuildMappingsTask>("build-mappings") {
    registryName = "neigeitems"
    outputDirectory = buildDir.resolve("cache/build-mappings")

    mapping("nms", "1.20.2"){
        predicates = arrayOf("craftbukkit_version:{v1_20_R2}")
    }
    mapping("nms", "1.20.1"){
        predicates = arrayOf("craftbukkit_version:{v1_20_R1}")
    }
    mapping("nms", "1.19.4"){
        predicates = arrayOf("craftbukkit_version:{v1_19_R3}")
    }
    mapping("nms", "1.18.2"){
        predicates = arrayOf("craftbukkit_version:{v1_18_R2}")
    }
    mapping("nms", "1.17.1"){
        predicates = arrayOf("craftbukkit_version:{v1_17_R1}")
    }
}

tasks.processResources {
    from(tasks.getByName("build-mappings").outputs)
}

tasks.create<ApplyReferenceTask>("apply-reference") {
    dependsOn(tasks.getByName("shadowJar"))

    appendReferencePackage("pers.neige.neigeitems.ref")
    inputJars = tasks.getByName("shadowJar").outputs.files
    outputJar = buildDir.resolve("libs/NeigeItems-$version-shaded.jar")
}

tasks.assemble {
    dependsOn(tasks.getByName("apply-reference"))
}
