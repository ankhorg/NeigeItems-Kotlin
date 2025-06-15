import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.inksnow.ankhinvoke.gradle.ApplyReferenceTask
import org.inksnow.ankhinvoke.gradle.BuildMappingsTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.13.1"
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    kotlin("plugin.lombok") version "2.1.20"
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.gradleup.shadow") version "8.3.5"
    id("org.inksnow.ankh-invoke-gradle-plugin") version "1.0.21-SNAPSHOT"
}

val realVersion = version

if (!System.getenv("CI").toBoolean()) {
    version = "dev"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "io.freefair.lombok")

    val projectName = this.name

    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/public")
    }
    dependencies {
        compileOnly(kotlin("stdlib"))
        if (projectName.contains("v1_21")) return@dependencies
        compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
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
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://jitpack.io/")
    maven("https://libraries.minecraft.net")
    // mmoitems
//    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:2.0.0")
    compileOnly(fileTree("libs"))
    compileOnly("io.netty:netty-all:5.0.0.Alpha2")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.ow2.asm:asm:9.4")
    compileOnly("io.th0rgal:oraxen:1.170.0")
//    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")
//    compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.16.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.16.0")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.1")

    compileOnly(project(":fake-api"))

    // ankh-invoke
    implementation("org.inksnow.cputil:logger:1.9")
    implementation("org.inksnow:ankh-invoke-bukkit:1.0.21-SNAPSHOT")

    // bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")
    // kotlin
    implementation(kotlin("stdlib"))
    // javassist
    implementation("org.javassist:javassist:3.20.0-GA")
    // openjdk-nashorn
    implementation(fileTree("libs/relocated-nashorn-15.4.jar"))
    // fastjson2
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.53")
    // multiple-string-searcher
    implementation("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
    // maven-model
    implementation("org.apache.maven:maven-model:3.9.1")
    // brigadier
    implementation("com.mojang:brigadier:1.3.10")
    // snakeyaml
    implementation("org.yaml:snakeyaml:2.3")
    // colonel
    implementation("pers.neige.colonel:colonel-common:+") {
        exclude(group = "org.neosearch.stringsearcher")
    }
    implementation("pers.neige.colonel:colonel-kotlin:+")
    implementation("pers.neige.colonel:colonel-bukkit:+")

    // junit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
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
        relocate("kotlin.", "${rootProject.group}.libs.kotlin.")
        // ankh-invoke
        relocate("org.inksnow.ankhinvoke", "${rootProject.group}.libs.org.inksnow.ankhinvoke")
        // bstats
        relocate("org.bstats", "${rootProject.group}.libs.bstats")
        // stringsearcher
        relocate("org.neosearch.stringsearcher", "${rootProject.group}.libs.stringsearcher")
        // javassist
        relocate("javassist", "${rootProject.group}.libs.javassist")
        // maven-model
        relocate("org.codehaus.plexus.util", "${rootProject.group}.libs.plexus.util")
        relocate("org.apache.maven.model", "${rootProject.group}.libs.maven.model")
        // fastjson2
        relocate("com.alibaba.fastjson2", "${rootProject.group}.libs.fastjson2")
        // asm
        relocate("org.objectweb.asm", "${rootProject.group}.libs.asm9")
        // JvmHacker
        relocate("bot.inker.acj", "${rootProject.group}.libs.acj")
        // slf4j
        relocate("org.inksnow.cputil", "${rootProject.group}.libs.cputil")
        relocate("org.slf4j", "${rootProject.group}.libs.slf4j")
        // brigadier
        relocate("com.mojang.brigadier.", "${rootProject.group}.libs.com.mojang.brigadier.")
        // snakeyaml
        relocate("org.yaml.snakeyaml", "${rootProject.group}.libs.snakeyaml")
        // colonel
        relocate("pers.neige.colonel", "${rootProject.group}.libs.colonel")
    }
    kotlinSourcesJar {
        // include subprojects
        rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
    }
    build {
        dependsOn(shadowJar)
    }
    test {
        useJUnitPlatform()
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
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
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
            maven("https://s0.blobs.irepo.space/maven/") {
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

tasks.withType<ProcessResources> {
    val properties = mapOf(
        "version" to realVersion.toString(),
        "group" to rootProject.group,
        "name" to rootProject.name,
    )
    inputs.properties(properties)
    filesMatching(listOf("plugin.yml")) {
        expand(properties)
    }
}

tasks.create<BuildMappingsTask>("build-mappings") {
    registryName = "neigeitems"
    outputDirectory = layout.buildDirectory.file("cache/build-mappings").get().asFile
    ankhInvokePackage = "${rootProject.group}.libs.org.inksnow.ankhinvoke"

    mapping("nms", "1.21.4") {
        predicates = arrayOf("craftbukkit_version:{v1_21_R3}")
    }
    mapping("nms", "1.21.3") {
        predicates = arrayOf("craftbukkit_version:{v1_21_R2}")
    }
    mapping("nms", "1.21.1") {
        predicates = arrayOf("craftbukkit_version:{v1_21_R1}")
    }
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
}

tasks.processResources {
    from(tasks.getByName("build-mappings").outputs)
}

tasks.create<ApplyReferenceTask>("apply-reference") {
    dependsOn(tasks.getByName("shadowJar"))

    ankhInvokePackage = "${rootProject.group}.libs.org.inksnow.ankhinvoke"
    appendReferencePackage("${rootProject.group}.ref")
    inputJars = tasks.getByName("shadowJar").outputs.files
    outputJar = layout.buildDirectory.file("libs/NeigeItems-$version-shaded.jar").get().asFile
}

tasks.assemble {
    dependsOn(tasks.getByName("apply-reference"))
}
