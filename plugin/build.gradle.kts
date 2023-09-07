import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

val taboolib_version: String by project

plugins {
    id("org.jetbrains.dokka") version "1.7.20"
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")

    implementation("io.izzel.taboolib:common:$taboolib_version")
    implementation("io.izzel.taboolib:common-5:$taboolib_version")
    implementation("io.izzel.taboolib:module-chat:$taboolib_version")
    implementation("io.izzel.taboolib:module-configuration:$taboolib_version")
    implementation("io.izzel.taboolib:module-nms:$taboolib_version")
    implementation("io.izzel.taboolib:module-nms-util:$taboolib_version")
    implementation("io.izzel.taboolib:module-metrics:$taboolib_version")
    implementation("io.izzel.taboolib:platform-bukkit:$taboolib_version")

    implementation("org.javassist:javassist:3.20.0-GA")
    implementation(fileTree("libs/callsite-nbt-1.0-dev-SNAPSHOT-fat.jar"))
    implementation(kotlin("stdlib"))
    implementation("org.openjdk.nashorn:nashorn-core:15.4")
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.25")
    implementation("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
    implementation("org.apache.maven:maven-model:3.9.1")

    implementation(project(":project:model-bukkit"))
    implementation(project(":project:common"))
    compileOnly(project(":project:hook-mythicmobs"))
    compileOnly(project(":project:hook-mythicmobs-impl-v459"))
    compileOnly(project(":project:hook-mythicmobs-impl-v490"))
    compileOnly(project(":project:hook-mythicmobs-impl-v502"))
    compileOnly(project(":project:hook-mythicmobs-impl-v510"))
}

if(!System.getenv("CI").toBoolean()){
    version = "dev"
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")
        // javassist
        relocate("javassist","pers.neige.neigeitems.libs.javassist")
        // callsitenbt
        relocate("bot.inker.bukkit.nbt","pers.neige.neigeitems.libs.bot.inker.bukkit.nbt")
        // taboolib
        relocate("taboolib", "pers.neige.neigeitems.taboolib")
        // kotlin
        relocate("kotlin.", "kotlin1710.") {
            exclude("kotlin.Metadata")
        }
    }
    kotlinSourcesJar {
        // include subprojects
        rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
    }
    build {
        dependsOn(shadowJar)
    }
}

//publishing {
//    repositories {
//        if(!System.getenv("CI").toBoolean()){
//            maven(buildDir.resolve("repo"))
//        }else if(!version.toString().endsWith("-SNAPSHOT")){
//            maven("https://s0.blobs.inksnow.org/maven/"){
//                credentials {
//                    username = System.getenv("IREPO_USERNAME")
//                    password = System.getenv("IREPO_PASSWORD")
//                }
//            }
//        }
//    }
//    publications {
//        create<MavenPublication>("library") {
//            artifact(tasks["apiJar"]){
//                classifier = null
//            }
//        }
//    }
//}
//
//tasks.create("apiJar", Jar::class){
//    dependsOn(tasks.compileJava, tasks.compileKotlin)
//    from(tasks.compileJava, tasks.compileKotlin)
//
//    // clean no-class file
//    include { it.isDirectory or it.name.endsWith(".class") }
//    includeEmptyDirs = false
//
//    archiveClassifier.set("api")
//}
//
//tasks.assemble{
//    dependsOn(tasks["apiJar"])
//}

val fuckYouTask = tasks.register("fuckYouTask") {
    doLast {
        val oldFile = File("$buildDir/libs/plugin-${project.property("version")}.jar")
        val newFile = File("$buildDir/libs/NeigeItems-${project.property("version")}.jar")

        if (!oldFile.exists()) return@doLast

        JarOutputStream(FileOutputStream(newFile)).use { jarOutputStream ->
            JarFile(oldFile).use { jarFile ->
                val entries = jarFile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val entryName = entry.name

                    if (entryName == "pers/neige/neigeitems/taboolib/platform/BukkitPlugin.class") {
                        val targetClassBytes = jarFile.getInputStream(entry).readBytes()

                        val classReader = org.objectweb.asm.ClassReader(targetClassBytes)
                        val classWriter = org.objectweb.asm.ClassWriter(classReader, org.objectweb.asm.ClassWriter.COMPUTE_MAXS)
                        val classVisitor = object : org.objectweb.asm.ClassVisitor(org.objectweb.asm.Opcodes.ASM9, classWriter) {
                            override fun visitMethod(
                                access: Int,
                                name: String?,
                                descriptor: String?,
                                signature: String?,
                                exceptions: Array<out String>?
                            ): org.objectweb.asm.MethodVisitor? {
                                val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
                                if (name == "<clinit>") {
                                    return object : org.objectweb.asm.MethodVisitor(org.objectweb.asm.Opcodes.ASM9, methodVisitor) {
                                        override fun visitCode() {
                                            visitLdcInsn(org.objectweb.asm.Type.getType("Lpers/neige/neigeitems/taboolib/platform/BukkitPlugin;"))
                                            visitMethodInsn(
                                                org.objectweb.asm.Opcodes.INVOKESTATIC,
                                                "pers/neige/neigeitems/libs/bot/inker/bukkit/nbt/loader/CallSiteNbt",
                                                "install",
                                                "(Ljava/lang/Class;)V",
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
                        jarOutputStream.putNextEntry(entry)
                        jarFile.getInputStream(entry).copyTo(jarOutputStream)
                        jarOutputStream.closeEntry()
                    }
                }
            }
        }
        oldFile.delete()
    }
}

tasks.getByName("build").finalizedBy(fuckYouTask)
