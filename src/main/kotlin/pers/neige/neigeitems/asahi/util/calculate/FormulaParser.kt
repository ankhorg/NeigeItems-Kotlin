package pers.neige.neigeitems.asahi.util.calculate

import pers.neige.neigeitems.asahi.util.calculate.CalcOperator.Companion.isCalcOperator
import pers.neige.neigeitems.asahi.util.calculate.CalcOperator.Companion.isCalcOperatorExceptRightBracket
import pers.neige.neigeitems.asahi.util.calculate.CalcOperator.Companion.toCalcOperator
import taboolib.common.platform.function.warning
import java.util.*

/**
 * @author Glom
 * @date 2023/1/16 3:33 Copyright 2023 user. All rights reserved.
 */
object FormulaParser {
    private fun String.toCalcInfix(): ArrayList<Any> {
        //将数字与运算符分开，按中缀表达式顺序排列在List中
        val infix = ArrayList<Any>()
        // 数字
        val num = StringBuilder()
        // 遍历文本
        forEachIndexed loop@{ index, char ->
            // 出现非运算符字符就跳过去
            if (!char.isCalcOperator()) {
                if (char != ' ') {
                    num.append(char)
                }
                return@loop
            }
            // 如果字符是正负号
            if (char == '+' || char == '-') {
                // 如果前面是运算符或者左括号，说明这是一个正负号前缀
                if (index == 0 || this[index - 1].isCalcOperatorExceptRightBracket()) {
                    num.append(char)
                    return@loop
                }
            }
            // 如果存在数字
            if (num.isNotEmpty()) {
                // 转Double丢进去
                infix += num.toString().toDouble()
            }
            num.setLength(0)
            // 运算符丢进去
            infix += char.toCalcOperator()
        }
        // 剩下的截一下转数字
        num.toString().toDoubleOrNull()?.let {
            // 如果剩下的确实是个数字, 就丢进去
            infix += it
        }
        return infix
    }

    private fun Stack<CalcOperator>.nextNotLessThan(calcOperator: CalcOperator): Boolean {
        return isNotEmpty() && peek() != CalcOperator.LEFT_BRACKET && peek().priority >= calcOperator.priority
    }

    fun List<Any>.toCalcSuffix(): Queue<Any> {
        // 定义一个后缀表达式序列
        val suffix = ArrayDeque<Any>()
        // 定义一个计算符堆
        val operators = Stack<CalcOperator>()
        // 遍历中缀表达式
        forEach {
            // 如果是数字, 存入后缀表达式
            if (it is Double) {
                suffix.offerLast(it)
                return@forEach
            }
            // 若是符号，则判断其与栈顶符号的优先级，是右括号或优先级不高于栈顶符号（乘除优先加减）则栈顶元素依次出栈并输出，并将当前符号进栈，一直到最终输出后缀表达式为止
            val calcOperator = it as CalcOperator
            if (calcOperator == CalcOperator.LEFT_BRACKET) {
                operators.push(calcOperator)
            } else if (calcOperator == CalcOperator.RIGHT_BRACKET) {
                while (isNotEmpty() && operators.peek() != CalcOperator.LEFT_BRACKET) {
                    suffix.offerLast(operators.pop())
                }
                if (isNotEmpty())
                    operators.pop()
            } else {
                while (operators.nextNotLessThan(calcOperator)) {
                    suffix.offerLast(operators.pop())
                }
                operators.push(calcOperator)
            }
        }
        while (!operators.isEmpty()) {
            suffix.offerLast(operators.pop())
        }
        return suffix
    }

    private fun Queue<Any>.calc(): Double {
        val calcStack: Stack<Double> = Stack()
        while (isNotEmpty()) {
            val obj = poll()
            if (obj is Double) {
                calcStack.push(obj)
                continue
            }
            val calcOperator = obj as CalcOperator
            val a = if (calcStack.isEmpty()) 0.0 else calcStack.pop()
            val b = if (calcStack.isEmpty()) 0.0 else calcStack.pop()
            calcStack.push(calcOperator.calc(a, b))
        }
        return calcStack.pop()
    }

    @JvmStatic
    fun String.calculate(): Double {
        return runCatching {
            toCalcInfix().toCalcSuffix().calc()
        }.getOrElse {
            warning("Wrong calculation formula! $this");
            0.0
        }
    }
}
