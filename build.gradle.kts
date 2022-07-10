plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.40"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

taboolib {
    description {
        contributors {
            name("Neige")
        }
        dependencies {
            name("MythicMobs").with("bukkit").optional(true).loadafter(true)
            name("ProtocolLib").with("bukkit").optional(true).loadafter(true)
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
        "platform-bukkit",
        "expansion-javascript"
    )
    classifier = null
    version = "6.0.9-25"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11900:11900-minimize:mapped")
    compileOnly("ink.ptms.core:v11900:11900-minimize:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    compileOnly("com.alibaba:fastjson:+")

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