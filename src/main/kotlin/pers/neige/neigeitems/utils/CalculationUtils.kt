package pers.neige.neigeitems.utils

import com.expression.parser.Parser

object CalculationUtils {
    /**
     * 计算数学公式
     *
     * @return 返回值
     */
    @JvmStatic
    fun String.calc(): Double {
        return Parser.simpleEval(this)
    }
}