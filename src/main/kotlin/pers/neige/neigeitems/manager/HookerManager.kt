package pers.neige.neigeitems.manager

import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.hook.mythicmobs.impl.LegacyMythicMobsHookerImpl
import pers.neige.neigeitems.hook.mythicmobs.impl.MythicMobsHookerImpl
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import pers.neige.neigeitems.hook.nashorn.impl.LegacyNashornHookerImpl
import pers.neige.neigeitems.hook.nashorn.impl.NashornHookerImpl

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
}