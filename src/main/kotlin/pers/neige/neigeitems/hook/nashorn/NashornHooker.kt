package pers.neige.neigeitems.hook.nashorn

import javax.script.ScriptEngine

abstract class NashornHooker {
    abstract fun getNashornEngine(): ScriptEngine
}