rootProject.name = "NeigeItems"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://r.irepo.space/maven/") {
            content { includeGroupByRegex("^org\\.inksnow(\\..+|)\$") }
        }
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include("fake-api")

include("hooker")

include("hooker:mythicmobs:v440")
include("hooker:mythicmobs:v459")
include("hooker:mythicmobs:v490")
include("hooker:mythicmobs:v502")
include("hooker:mythicmobs:v510")
include("hooker:mythicmobs:v560")

include("hooker:nms:v1_12_R1")
include("hooker:nms:v1_14_R1")
include("hooker:nms:v1_16_R2")
include("hooker:nms:v1_21+")
