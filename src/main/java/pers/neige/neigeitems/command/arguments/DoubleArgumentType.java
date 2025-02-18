package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.command.CommandUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * 小数参数类型
 */
public class DoubleArgumentType implements ArgumentType<Double> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0.0", "123.0", "-123.0");
    private final double minimum;
    private final double maximum;

    private DoubleArgumentType(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * 非负小数类型
     */
    @NotNull
    public static DoubleArgumentType nonNegativeDouble() {
        return doubleValue(0);
    }

    /**
     * 非正小数类型
     */
    @NotNull
    public static DoubleArgumentType nonPositiveDouble() {
        return doubleValue(-Double.MAX_VALUE, 0);
    }

    /**
     * 无限制小数类型
     */
    @NotNull
    public static DoubleArgumentType doubleValue() {
        return doubleValue(-Double.MAX_VALUE);
    }

    /**
     * 限制最小值小数类型
     */
    @NotNull
    public static DoubleArgumentType doubleValue(double min) {
        return doubleValue(min, Double.MAX_VALUE);
    }

    /**
     * 限制最大值小数类型
     */
    @NotNull
    public static DoubleArgumentType doubleValue(double min, double max) {
        return new DoubleArgumentType(min, max);
    }

    public static double getDouble(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, Double.TYPE);
    }

    /**
     * 获取下限
     */
    public double getMinimum() {
        return this.minimum;
    }

    /**
     * 获取上限
     */
    public double getMaximum() {
        return this.maximum;
    }

    @NotNull
    @Override
    public Double parse(
            @NotNull StringReader reader
    ) throws CommandSyntaxException {
        Double result = CommandUtils.readDouble(reader);
        return Math.min(Math.max(result, minimum), maximum);
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
