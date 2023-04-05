package pers.neige.neigeitems.asahi.util.calculate

import kotlin.math.pow

/**
 * @author Glom
 * @date 2023/1/16 3:33 Copyright 2023 user. All rights reserved.
 */
internal enum class CalcOperator(
    private val symbol: Char,
    val priority: Int,
    val calc: (Double, Double) -> Double = { _, _ -> 0.0 },
) {
    PLUS('+', 1, { a, b -> a + b }),
    MINUS('-', 1, { a, b -> b - a }),

    MULTIPLY('*', 2, { a, b -> a * b }),

    DIVIDE('/', 2, { a, b -> b / a }),

    REMAIN('%', 2, { a, b -> b % a }),

    POWER('^', 3, { a, b -> b.pow(a) }),
    LEFT_BRACKET('(', 3),

    RIGHT_BRACKET(')', 3);

    override fun toString(): String {
        return symbol.toString()
    }

    companion object {
        private val symbols = CalcOperator.values().map { it.symbol }.toHashSet()
        private val symbolsExceptRightBracket = CalcOperator.values().map { it.symbol }.filter { it != ')' }.toHashSet()
        fun Char.isCalcOperator(): Boolean = this in symbols
        fun Char.isCalcOperatorExceptRightBracket(): Boolean = this in symbolsExceptRightBracket
        fun Char.toCalcOperator(): CalcOperator {
            return when (this) {
                '+' -> PLUS
                '-' -> MINUS
                '*' -> MULTIPLY
                '/' -> DIVIDE
                '%' -> REMAIN
                '^' -> POWER
                '(' -> LEFT_BRACKET
                ')' -> RIGHT_BRACKET
                else -> error("No such Operator $this")
            }
        }
    }
}