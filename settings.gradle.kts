rootProject.name = "NeigeItems"

//pluginManagement {
//    repositories {
//        gradlePluginPortal()
//        maven("https://r.irepo.space/maven/") {
//            content { includeGroupByRegex("^org\\.inksnow(\\..+|)\$") }
//        }
//    }
//}

include("hooker")

include("hooker-mythicmobs-v440")
include("hooker-mythicmobs-v459")
include("hooker-mythicmobs-v490")
include("hooker-mythicmobs-v502")
include("hooker-mythicmobs-v510")

include("hooker-nms-NamespacedKey")
include("hooker-nms-CustomModelData")
include("hooker-nms-HoverEvent")

include("hooker-callsitenbt")

