package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import pers.neige.neigeitems.command.CommandUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * 字符串参数类型
 */
public class UnquotedStringArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("test");

    private UnquotedStringArgumentType() {
    }

    public static @NonNull UnquotedStringArgumentType string() {
        return new UnquotedStringArgumentType();
    }

    public static @NonNull String getUnquotedString(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, String.class);
    }

    @Override
    public @NonNull String parse(
        @NonNull StringReader reader
    ) {
        return CommandUtils.readUnquotedString(reader);
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
