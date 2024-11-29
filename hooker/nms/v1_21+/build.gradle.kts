import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

plugins {
    id("io.papermc.paperweight.userdev") version "1.7.3"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")
    compileOnly(project(":"))
}

tasks.assemble {
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
    dependsOn(tasks.reobfJar)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

fun final() {
    val mainFile =
        rootProject.layout.buildDirectory.file("libs/${rootProject.name}-${rootProject.property("version")}-temp.jar")
            .get().asFile
    val newMainFile =
        rootProject.layout.buildDirectory.file("libs/${rootProject.name}-${rootProject.property("version")}.jar")
            .get().asFile
    val currentFile =
        project.layout.buildDirectory.file("libs/${project.name}-${project.property("version")}.jar").get().asFile

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
}

val finalTask = tasks.register("finalTask") {
    doLast {
        final()
    }
}

tasks.getByName("build").finalizedBy(finalTask)
