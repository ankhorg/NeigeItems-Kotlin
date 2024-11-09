package pers.neige.neigeitems.calculate;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.*;

import static pers.neige.neigeitems.calculate.CalcOperator.*;

public class FormulaParser {
    @NotNull
    private static ArrayList<Object> toCalcInfix(@NotNull String formula) {
        //将数字与运算符分开，按中缀表达式顺序排列在List中
        ArrayList<Object> infix = new ArrayList<>();
        // 数字
        StringBuilder num = new StringBuilder();
        // 遍历文本
        for (int index = 0; index < formula.length(); index++) {
            char c = formula.charAt(index);
            // 出现非运算符字符就跳过去
            if (!isCalcOperator(c)) {
                if (c != ' ') {
                    num.append(c);
                }
                continue;
            }
            // 如果字符是正负号
            if (c == '+' || c == '-') {
                // 如果前面是运算符或者左括号，说明这是一个正负号前缀
                if (index == 0 || isCalcOperatorExceptRightBracket(formula.charAt(index - 1))) {
                    num.append(c);
                    continue;
                }
            }
            // 如果存在数字
            if (num.length() != 0) {
                // 转Double丢进去
                infix.add(Double.parseDouble(num.toString()));
            }
            num.setLength(0);
            // 运算符丢进去
            infix.add(toCalcOperator(c));
        }
        // 剩下的截一下转数字
        if (num.length() != 0) {
            infix.add(Double.parseDouble(num.toString()));
        }
        return infix;
    }

    private static boolean nextNotLessThan(@NotNull Stack<CalcOperator> stack, @NotNull CalcOperator calcOperator) {
        if (stack.isEmpty()) return false;
        CalcOperator peek = stack.peek();
        return peek != CalcOperator.LEFT_BRACKET && peek.priority >= calcOperator.priority;
    }

    @NotNull
    private static Queue<Object> toCalcSuffix(@NotNull List<Object> infix) {
        // 定义一个后缀表达式序列
        ArrayDeque<Object> suffix = new ArrayDeque<>();
        // 定义一个计算符堆
        Stack<CalcOperator> operators = new Stack<>();
        // 遍历中缀表达式
        for (Object it : infix) {
            // 如果是数字, 存入后缀表达式
            if (it instanceof Double) {
                suffix.offerLast(it);
                continue;
            }
            // 若是符号，则判断其与栈顶符号的优先级，是右括号或优先级不高于栈顶符号（乘除优先加减）则栈顶元素依次出栈并输出，并将当前符号进栈，一直到最终输出后缀表达式为止
            CalcOperator calcOperator = (CalcOperator) it;
            if (calcOperator == CalcOperator.LEFT_BRACKET) {
                operators.push(calcOperator);
            } else if (calcOperator == CalcOperator.RIGHT_BRACKET) {
                while (!infix.isEmpty() && operators.peek() != CalcOperator.LEFT_BRACKET) {
                    suffix.offerLast(operators.pop());
                }
                if (!infix.isEmpty())
                    operators.pop();
            } else {
                while (nextNotLessThan(operators, calcOperator)) {
                    suffix.offerLast(operators.pop());
                }
                operators.push(calcOperator);
            }
        }
        while (!operators.isEmpty()) {
            suffix.offerLast(operators.pop());
        }
        return suffix;
    }

    private static double calc(@NotNull Queue<Object> suffix) {
        Stack<Double> calcStack = new Stack<>();
        while (!suffix.isEmpty()) {
            Object obj = suffix.poll();
            if (obj instanceof Double) {
                calcStack.push((double) obj);
                continue;
            }
            CalcOperator calcOperator = (CalcOperator) obj;
            double a = calcStack.isEmpty() ? 0.0 : calcStack.pop();
            double b = calcStack.isEmpty() ? 0.0 : calcStack.pop();
            calcStack.push(calcOperator.calc.apply(a, b));
        }
        return calcStack.pop();
    }

    public static double calculate(@NotNull String formula) {
        try {
            return calc(toCalcSuffix(toCalcInfix(formula)));
        } catch (Throwable throwable) {
            Map<String, String> params = new HashMap<>();
            params.put("{formula}", formula);
            LangUtils.sendLang(Bukkit.getConsoleSender(), "Messages.invalidFormula", params);
            return 0.0;
        }
    }
}
