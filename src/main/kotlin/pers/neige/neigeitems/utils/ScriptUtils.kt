package pers.neige.neigeitems.utils

import org.bukkit.Bukkit
import pers.neige.neigeitems.manager.ScriptManager.scriptEngine
import pers.neige.neigeitems.utils.LangUtils.sendLang
import java.math.RoundingMode
import java.util.*

/**
 * 脚本相关工具类
 */
object ScriptUtils {
    /**
     * 执行JS脚本
     *
     * @return 返回值
     */
    @JvmStatic
    fun String.eval(): Any {
        return scriptEngine.eval(this)
    }

    /**
     * 通过nashorn引擎计算数学公式
     *
     * @return 公式计算结果
     */
    @JvmStatic
    fun String.calculate(): Double {
        // 执行结果
        val result =
            when (val evalResult = runCatching { this.eval() }.getOrNull()) {
                is Double -> evalResult
                is Number -> evalResult.toDouble()
                is String -> evalResult.toDoubleOrNull()
                null -> null
                else -> runCatching { evalResult.toString().toDoubleOrNull() }.getOrNull()
            }
        if (result == null) {
            Bukkit.getConsoleSender().sendLang("Messages.invalidFormula", mapOf(
                Pair("{formula}", this)
            ))
        }
        return result ?: 0.0
    }

    fun String?.toRoundingMode(): RoundingMode {
        return when (this) {
            null -> RoundingMode.HALF_UP
            "" -> RoundingMode.HALF_UP
            "UP" -> RoundingMode.UP
            "DOWN" -> RoundingMode.DOWN
            "CEILING" -> RoundingMode.CEILING
            "FLOOR" -> RoundingMode.FLOOR
            "HALF_UP" -> RoundingMode.HALF_UP
            "HALF_DOWN" -> RoundingMode.HALF_DOWN
            "HALF_EVEN" -> RoundingMode.HALF_EVEN
            else -> {
                Bukkit.getConsoleSender().sendLang("Messages.invalidRoundingMode", mapOf(
                    Pair("{mode}", this.uppercase(Locale.getDefault()))
                ))
                RoundingMode.HALF_UP
            }
        }
    }
}