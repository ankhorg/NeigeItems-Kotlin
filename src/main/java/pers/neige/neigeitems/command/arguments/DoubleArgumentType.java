package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
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
    public static @NonNull DoubleArgumentType nonNegativeDouble() {
        return doubleValue(0);
    }

    /**
     * 非正小数类型
     */
    public static @NonNull DoubleArgumentType nonPositiveDouble() {
        return doubleValue(-Double.MAX_VALUE, 0);
    }

    /**
     * 无限制小数类型
     */
    public static @NonNull DoubleArgumentType doubleValue() {
        return doubleValue(-Double.MAX_VALUE);
    }

    /**
     * 限制最小值小数类型
     */
    public static @NonNull DoubleArgumentType doubleValue(double min) {
        return doubleValue(min, Double.MAX_VALUE);
    }

    /**
     * 限制最大值小数类型
     */
    public static @NonNull DoubleArgumentType doubleValue(double min, double max) {
        return new DoubleArgumentType(min, max);
    }

    public static double getDouble(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
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

    @Override
    public @NonNull Double parse(
        @NonNull StringReader reader
    ) throws CommandSyntaxException {
        val result = CommandUtils.readDouble(reader);
        return Math.min(Math.max(result, minimum), maximum);
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
