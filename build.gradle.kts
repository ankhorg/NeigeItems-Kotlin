plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
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
    version = "6.0.10-113"
}

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
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
    compileOnly(fileTree("libs"))
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    taboo(kotlin("stdlib"))
    taboo("org.openjdk.nashorn:nashorn-core:15.4")
    taboo("com.alibaba.fastjson2:fastjson2-kotlin:2.0.9")
    taboo("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
//        taboo("com.google.guava:guava:31.1-jre")
    taboo("org.apache.maven:maven-model:3.9.1")
}

java{
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

tasks.create("apiJar", Jar::class){
    dependsOn(tasks.compileJava, tasks.compileKotlin)
    from(tasks.compileJava)
    from(tasks.compileKotlin)

    // clean no-class file
    include { it.isDirectory or it.name.endsWith(".class") }
    includeEmptyDirs = false

    archiveClassifier.set("api")
}

tasks.assemble{
    dependsOn(tasks["apiJar"])
}

publishing {
    repositories {
        if(!System.getenv("CI").toBoolean()){
            maven(buildDir.resolve("repo"))
        }else if(!version.toString().endsWith("-SNAPSHOT")){
            maven("https://s0.blobs.inksnow.org/maven/"){
                credentials {
                    username = System.getenv("IREPO_USERNAME")
                    password = System.getenv("IREPO_PASSWORD")
                }
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            artifact(tasks["apiJar"]){
                classifier = null
            }
            artifactId = "NeigeItems"
        }
    }
}