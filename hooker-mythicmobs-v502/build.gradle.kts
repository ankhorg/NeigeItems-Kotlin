import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

val taboolib_version: String by project

repositories {
    maven {
        url = uri("http://ptms.ink:8081/repository/releases/")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("io.izzel.taboolib:common:$taboolib_version")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolib_version")
    compileOnly(project(":"))
}

tasks {
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")
        // kotlin
        relocate("kotlin.", "kotlin1720.") {
            exclude("kotlin.Metadata")
        }
        // taboolib
        relocate("taboolib", "pers.neige.neigeitems.taboolib")
    }
    kotlinSourcesJar {
        // include subprojects
        rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
    }
    build {
        dependsOn(shadowJar)
    }
}

fun final() {
    val mainFile = File("${rootProject.buildDir}/libs/${rootProject.name}-${rootProject.property("version")}.jar")
    val newMainFile = File("${rootProject.buildDir}/libs/Modified${rootProject.name}-${rootProject.property("version")}.jar")
    val currentFile = File("${project.buildDir}/libs/${project.name}-${project.property("version")}.jar")

    if (!mainFile.exists()) return
    if (!currentFile.exists()) return

    JarOutputStream(FileOutputStream(newMainFile)).use { jarOutputStream ->
        JarFile(mainFile).use { jarFile ->
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()

                jarOutputStream.putNextEntry(entry)
                jarFile.getInputStream(entry).copyTo(jarOutputStream)
                jarOutputStream.closeEntry()
            }
        }
        JarFile(currentFile).use { jarFile ->
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryName = entry.name
                if (entryName.endsWith(".class")) {
                    jarOutputStream.putNextEntry(entry)
                    jarFile.getInputStream(entry).copyTo(jarOutputStream)
                    jarOutputStream.closeEntry()
                }
            }
        }
    }

    mainFile.delete()
    newMainFile.renameTo(mainFile)
}

val finalTask = tasks.register("finalTask") {
    doLast {
        final()
    }
}

tasks.getByName("build").finalizedBy(finalTask)
