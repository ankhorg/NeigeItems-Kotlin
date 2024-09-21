import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.inksnow.ankhinvoke.gradle.ApplyReferenceTask
import org.inksnow.ankhinvoke.gradle.BuildMappingsTask

plugins {
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.inksnow.ankh-invoke-gradle-plugin") version "1.0.13-SNAPSHOT"
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
    }
    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
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

    kotlin {
        sourceSets.all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
    }
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    mavenCentral()
    maven("https://r.irepo.space/maven/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.extendedclip.com/content/")
    maven("https://jitpack.io/")
    maven("https://libraries.minecraft.net")
    // mmoitems
//    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.10")
    compileOnly(fileTree("libs"))
    compileOnly("io.netty:netty-all:5.0.0.Alpha2")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.ow2.asm:asm:9.4")
    compileOnly("io.th0rgal:oraxen:1.170.0")
//    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")
//    compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.16.0")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.1")

    // ankh-invoke
    implementation("org.inksnow.cputil:logger:1.9")
    implementation("org.inksnow:ankh-invoke-bukkit:1.0.13-SNAPSHOT")

    // bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")
    // kotlin
    implementation(kotlin("stdlib"))
    // javassist
    implementation("org.javassist:javassist:3.20.0-GA")
    // openjdk-nashorn
    implementation(fileTree("libs/relocated-nashorn-15.4.jar"))
    // fastjson2
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.43")
    // multiple-string-searcher
    implementation("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
    // maven-model
    implementation("org.apache.maven:maven-model:3.9.1")
    // brigadier
    implementation("com.mojang:brigadier:1.1.8")
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("test")

        mergeServiceFiles()
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")
        exclude("org/intellij/lang/annotations/**")
        exclude("org/jetbrains/annotations/**")
        // kotlin
        relocate("kotlin.", "pers.neige.neigeitems.libs.kotlin.")
        // ankh-invoke
        relocate("org.inksnow.ankhinvoke", "pers.neige.neigeitems.libs.org.inksnow.ankhinvoke")
        // bstats
        relocate("org.bstats", "pers.neige.neigeitems.libs.bstats")
        // stringsearcher
        relocate("org.neosearch.stringsearcher", "pers.neige.neigeitems.libs.stringsearcher")
        // javassist
        relocate("javassist", "pers.neige.neigeitems.libs.javassist")
        // maven-model
        relocate("org.codehaus.plexus.util", "pers.neige.neigeitems.libs.plexus.util")
        relocate("org.apache.maven.model", "pers.neige.neigeitems.libs.maven.model")
        // fastjson2
        relocate("com.alibaba.fastjson2", "pers.neige.neigeitems.libs.fastjson2")
        // asm
        relocate("org.objectweb.asm", "pers.neige.neigeitems.libs.asm9")
        // JvmHacker
        relocate("bot.inker.acj", "pers.neige.neigeitems.libs.acj")
        // slf4j
        relocate("org.inksnow.cputil", "pers.neige.neigeitems.libs.cputil")
        relocate("org.slf4j", "pers.neige.neigeitems.libs.slf4j")
        // brigadier
        relocate("com.mojang.", "pers.neige.neigeitems.libs.com.mojang.")
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

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
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
            maven(layout.buildDirectory.file("repo").get().asFile)
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

// 将plugin.yml中的"${version}"替换为插件版本
tasks.processResources {
    filesMatching("plugin.yml") {
        filter {
            it.replace("\${version}", realVersion.toString())
        }
    }
}

tasks.create<BuildMappingsTask>("build-mappings") {
    registryName = "neigeitems"
    outputDirectory = layout.buildDirectory.file("cache/build-mappings").get().asFile
    ankhInvokePackage = "pers.neige.neigeitems.libs.org.inksnow.ankhinvoke"

//    mapping("nms", "1.21") {
//        predicates = arrayOf("craftbukkit_version:{v1_21_R1}")
//    }
    mapping("nms", "1.20.4") {
        predicates = arrayOf("craftbukkit_version:{v1_20_R3}")
    }
    mapping("nms", "1.20.2") {
        predicates = arrayOf("craftbukkit_version:{v1_20_R2}")
    }
    mapping("nms", "1.20.1") {
        predicates = arrayOf("craftbukkit_version:{v1_20_R1}")
    }
    mapping("nms", "1.19.4") {
        predicates = arrayOf("craftbukkit_version:{v1_19_R3}")
    }
    mapping("nms", "1.18.2") {
        predicates = arrayOf("craftbukkit_version:{v1_18_R2}")
    }
    mapping("nms", "1.17.1") {
        predicates = arrayOf("craftbukkit_version:{v1_17_R1}")
    }
}

tasks.processResources {
    from(tasks.getByName("build-mappings").outputs)
}

tasks.create<ApplyReferenceTask>("apply-reference") {
    dependsOn(tasks.getByName("shadowJar"))

    ankhInvokePackage = "pers.neige.neigeitems.libs.org.inksnow.ankhinvoke"
    appendReferencePackage("pers.neige.neigeitems.ref")
    inputJars = tasks.getByName("shadowJar").outputs.files
    outputJar = layout.buildDirectory.file("libs/NeigeItems-$version-shaded.jar").get().asFile
}

tasks.assemble {
    dependsOn(tasks.getByName("apply-reference"))
}
