val taboolib_version: String by project

plugins {
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/public")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.dmulloy2.net/repository/public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
        // taboo的仓库有时候github自动构建连不上, 丢到最后防止自动构建发生意外
        maven("https://repo.tabooproject.org/storages/public/releases")
        maven {
            url = uri("http://ptms.ink:8081/repository/releases/")
            isAllowInsecureProtocol = true
        }
    }
    dependencies {
        compileOnly(kotlin("stdlib"))
        // taboolib
        compileOnly("io.izzel.taboolib:common:$taboolib_version")
        compileOnly("io.izzel.taboolib:common-5:$taboolib_version")
        compileOnly("io.izzel.taboolib:module-chat:$taboolib_version")
        compileOnly("io.izzel.taboolib:module-configuration:$taboolib_version")
        compileOnly("io.izzel.taboolib:module-nms:$taboolib_version")
        compileOnly("io.izzel.taboolib:module-nms-util:$taboolib_version")
        compileOnly("io.izzel.taboolib:module-metrics:$taboolib_version")
        compileOnly("io.izzel.taboolib:platform-bukkit:$taboolib_version")

        compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
        compileOnly("ink.ptms:nms-all:1.0.0")
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
    java{
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
