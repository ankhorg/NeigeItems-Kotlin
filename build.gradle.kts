plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.51"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
}

val api: String? by project

task("versionAddAPI") {
    if (api == null) return@task
    val origin = project.version.toString()
    project.version = "$origin-api"
}

taboolib {
    if (project.version.toString().contains("-api")) {
        options("skip-kotlin-relocate")
    }
    description {
        contributors {
            name("Neige")
        }
        dependencies {
            name("ProtocolLib").with("bukkit").optional(true).loadafter(true)
            name("PlaceholderAPI").with("bukkit").optional(true).loadafter(true)
            name("MythicMobs").with("bukkit").optional(true).loadafter(true)
            name("Vault").with("bukkit").optional(true).loadafter(true)
        }
    }
    install(
        "common",
        "common-5",
        "module-chat",
        "module-configuration",
        "module-nms",
        "module-nms-util",
        "module-metrics",
        "platform-bukkit",
    )
    classifier = null
    version = "6.0.10-70"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/public") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://mvn.lumine.io/repository/maven-public") }
    maven { url = uri("https://jitpack.io") }
    // taboo的仓库有时候github自动构建连不上, 丢到最后防止自动构建发生意外
    maven { url = uri("https://repo.tabooproject.org/storages/public/releases") }
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
    compileOnly(fileTree("libs"))
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11900:11900-minimize:mapped")
    compileOnly("ink.ptms.core:v11900:11900-minimize:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly("org.openjdk.nashorn:nashorn-core:15.4")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("io.lumine:Mythic-Dist:5.1.0")
    compileOnly("com.alibaba.fastjson2:fastjson2-kotlin:2.0.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}