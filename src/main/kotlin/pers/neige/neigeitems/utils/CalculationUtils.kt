package pers.neige.neigeitems.utils

import pers.neige.neigeitems.asahi.util.calculate.calculate
import java.math.BigDecimal

object CalculationUtils {
    /**
     * 计算数学公式
     *
     * @return 返回值
     */
    @JvmStatic
    fun String.calc(): BigDecimal {
        return this.calculate()
    }
}