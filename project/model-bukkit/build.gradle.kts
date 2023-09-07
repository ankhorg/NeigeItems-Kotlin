val taboolib_version: String by project

plugins {
    id("io.izzel.taboolib") version "1.56"
}

taboolib {
    description {
        name(rootProject.name)
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
    options("skip-minimize", "keep-kotlin-module", "skip-taboolib-relocate")
    classifier = null
    version = taboolib_version
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
