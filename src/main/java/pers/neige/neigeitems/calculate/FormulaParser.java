package pers.neige.neigeitems.calculate;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.*;

import static pers.neige.neigeitems.calculate.CalcOperator.*;

public class FormulaParser {
    private static @NonNull ArrayList<Object> toCalcInfix(@NonNull String formula) {
        //将数字与运算符分开，按中缀表达式顺序排列在List中
        val infix = new ArrayList<>();
        // 数字
        val num = new StringBuilder();
        // 遍历文本
        for (int index = 0; index < formula.length(); index++) {
            val c = formula.charAt(index);
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

    private static @NonNull ArrayList<Object> toCalcInfix(@NonNull String formula, @NonNull Map<String, Double> args) {
        val infix = new ArrayList<>();
        val num = new StringBuilder();
        for (int index = 0; index < formula.length(); index++) {
            val c = formula.charAt(index);
            if (!isCalcOperator(c)) {
                if (c != ' ') {
                    num.append(c);
                }
                continue;
            }
            if (c == '+' || c == '-') {
                if (index == 0 || isCalcOperatorExceptRightBracket(formula.charAt(index - 1))) {
                    num.append(c);
                    continue;
                }
            }
            if (num.length() != 0) {
                infix.add(args.computeIfAbsent(num.toString(), Double::parseDouble));
            }
            num.setLength(0);
            infix.add(toCalcOperator(c));
        }
        if (num.length() != 0) {
            infix.add(args.computeIfAbsent(num.toString(), Double::parseDouble));
        }
        return infix;
    }

    private static boolean nextNotLessThan(@NonNull Stack<CalcOperator> stack, @NonNull CalcOperator calcOperator) {
        if (stack.isEmpty()) return false;
        val peek = stack.peek();
        return peek != CalcOperator.LEFT_BRACKET && peek.priority >= calcOperator.priority;
    }

    private static @NonNull Queue<Object> toCalcSuffix(@NonNull List<Object> infix) {
        // 定义一个后缀表达式序列
        val suffix = new ArrayDeque<>();
        // 定义一个计算符堆
        val operators = new Stack<CalcOperator>();
        // 遍历中缀表达式
        for (val it : infix) {
            // 如果是数字, 存入后缀表达式
            if (it instanceof Double) {
                suffix.offerLast(it);
                continue;
            }
            // 若是符号，则判断其与栈顶符号的优先级，是右括号或优先级不高于栈顶符号（乘除优先加减）则栈顶元素依次出栈并输出，并将当前符号进栈，一直到最终输出后缀表达式为止
            val calcOperator = (CalcOperator) it;
            // 幂运算在大多数编程语言中为右结合运算，即2 ^ 2 ^ 3 == 2 ^ (2 ^ 3)
            if (calcOperator == CalcOperator.LEFT_BRACKET || calcOperator == CalcOperator.POWER) {
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

    private static double calc(@NonNull Queue<Object> suffix) {
        val calcStack = new Stack<Double>();
        while (!suffix.isEmpty()) {
            val obj = suffix.poll();
            if (obj instanceof Double) {
                calcStack.push((double) obj);
                continue;
            }
            val calcOperator = (CalcOperator) obj;
            val a = calcStack.isEmpty() ? 0.0 : calcStack.pop();
            val b = calcStack.isEmpty() ? 0.0 : calcStack.pop();
            calcStack.push(calcOperator.calc.apply(a, b));
        }
        return calcStack.pop();
    }

    public static double calculate(@NonNull String formula) {
        try {
            return calc(toCalcSuffix(toCalcInfix(formula)));
        } catch (Throwable throwable) {
            val params = new HashMap<String, String>();
            params.put("{formula}", formula);
            LangUtils.sendLang(Bukkit.getConsoleSender(), "Messages.invalidFormula", params);
            return 0.0;
        }
    }

    public static double calculate(@NonNull String formula, @NonNull Map<String, Double> args) {
        try {
            return calc(toCalcSuffix(toCalcInfix(formula, args)));
        } catch (Throwable throwable) {
            val params = new HashMap<String, String>();
            params.put("{formula}", formula);
            LangUtils.sendLang(Bukkit.getConsoleSender(), "Messages.invalidFormula", params);
            return 0.0;
        }
    }
}
