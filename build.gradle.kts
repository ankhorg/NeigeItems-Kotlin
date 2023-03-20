plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
}

val api: String? by project
val lib: String? by project

task("versionCheck") {
    if (api != null) {
        val origin = project.version.toString()
        project.version = "$origin-api"
    } else if (lib != null) {
        val origin = project.version.toString()
        project.version = "$origin-lib"
    }
}

taboolib {
    if (project.version.toString().contains("-api")) {
        options("skip-kotlin-relocate")
    }
//    relocate("org.openjdk.nashorn","pers.neige.neigeitems.nashorn")
    description {
        contributors {
            name("Neige")
        }
        dependencies {
            name("ProtocolLib").with("bukkit").optional(true)
            name("PlaceholderAPI").with("bukkit").optional(true)
            name("MythicMobs").with("bukkit").optional(true)
            name("Vault").with("bukkit").optional(true)
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
    version = "6.0.10-98"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/public") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://jitpack.io") }
    // taboo的仓库有时候github自动构建连不上, 丢到最后防止自动构建发生意外
    maven { url = uri("https://repo.tabooproject.org/storages/public/releases") }
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
    compileOnly(fileTree("libs"))
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    if (project.version.toString().contains("-lib")) {
        taboo(kotlin("stdlib"))
        taboo("org.openjdk.nashorn:nashorn-core:15.4")
        taboo("com.alibaba.fastjson2:fastjson2-kotlin:2.0.9")
        taboo("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
//        taboo("com.google.guava:guava:31.1-jre")
    } else {
        compileOnly(kotlin("stdlib"))
        compileOnly("org.openjdk.nashorn:nashorn-core:15.4")
        compileOnly("com.alibaba.fastjson2:fastjson2-kotlin:2.0.9")
        compileOnly("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
//        compileOnly("com.google.guava:guava:31.1-jre")
    }
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