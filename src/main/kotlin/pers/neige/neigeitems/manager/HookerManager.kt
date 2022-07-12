package pers.neige.neigeitems.manager

import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.hook.mythicmobs.impl.LegacyMythicMobsHookerImpl
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import pers.neige.neigeitems.hook.nashorn.impl.LegacyNashornHookerImpl
import pers.neige.neigeitems.hook.nashorn.impl.NashornHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker
import pers.neige.neigeitems.hook.placeholderapi.impl.LegacyPapiHookerImpl
import pers.neige.neigeitems.hook.placeholderapi.impl.PapiHookerImpl

object HookerManager {
    val nashornHooker: NashornHooker =
        try {
            LegacyNashornHookerImpl()
        } catch (error: Throwable) {
            NashornHookerImpl()
        }

    val mythicMobsHooker: MythicMobsHooker =
        try {
            LegacyMythicMobsHookerImpl()
        } catch (error: Throwable) {
            MythicMobsHookerImpl()
        }

    // papi中间有一些兼容版本存在, 先构建新版的Hooker实现, 解析效率更高
    val papiHooker: PapiHooker =
        try {
            PapiHookerImpl()
        } catch (error: Throwable) {
            LegacyPapiHookerImpl()
        }
}