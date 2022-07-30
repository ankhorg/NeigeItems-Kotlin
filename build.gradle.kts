plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
}

taboolib {
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
        "module-kether",
        "module-metrics",
        "module-nms",
        "module-nms-util",
        "module-metrics",
        "platform-bukkit",
    )
    classifier = null
    version = "6.0.9-31"
}

repositories {
    mavenLocal()
    maven { url = uri("https://repo.tabooproject.org/storages/public/releases") }
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/public") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://mvn.lumine.io/repository/maven-public") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11900:11900-minimize:mapped")
    compileOnly("ink.ptms.core:v11900:11900-minimize:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly("org.openjdk.nashorn:nashorn-core:15.4")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("io.lumine:Mythic-Dist:5.1.0-SNAPSHOT")
    compileOnly("com.alibaba.fastjson2:fastjson2-kotlin:2.0.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
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