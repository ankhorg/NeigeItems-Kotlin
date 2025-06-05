package pers.neige.neigeitems.calculate;

import lombok.NonNull;
import lombok.val;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;

enum CalcOperator {
    PLUS('+', 1, Double::sum),
    MINUS('-', 1, (a, b) -> b - a),

    MULTIPLY('*', 2, (a, b) -> a * b),

    DIVIDE('/', 2, (a, b) -> b / a),

    REMAIN('%', 2, (a, b) -> b % a),

    POWER('^', 3, (a, b) -> Math.pow(b, a)),
    LEFT_BRACKET('(', 3),

    RIGHT_BRACKET(')', 3);

    private static final HashMap<Character, CalcOperator> bySymbol;
    private static final HashSet<Character> symbolsExceptRightBracket;

    static {
        bySymbol = new HashMap<>();
        symbolsExceptRightBracket = new HashSet<>();
        for (val operator : CalcOperator.values()) {
            bySymbol.put(operator.symbol, operator);
            if (operator != RIGHT_BRACKET) {
                symbolsExceptRightBracket.add(operator.symbol);
            }
        }
    }

    final int priority;
    final @NonNull BiFunction<Double, Double, Double> calc;
    private final char symbol;

    CalcOperator(char symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
        this.calc = (a, b) -> 0.0;
    }

    CalcOperator(char symbol, int priority, @NonNull BiFunction<Double, Double, Double> calc) {
        this.symbol = symbol;
        this.priority = priority;
        this.calc = calc;
    }

    static boolean isCalcOperator(char c) {
        return bySymbol.containsKey(c);
    }

    static boolean isCalcOperatorExceptRightBracket(char c) {
        return symbolsExceptRightBracket.contains(c);
    }

    static @NonNull CalcOperator toCalcOperator(char c) throws IllegalStateException {
        val result = bySymbol.get(c);
        if (result == null) {
            throw new IllegalStateException("No such Operator " + c);
        }
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
