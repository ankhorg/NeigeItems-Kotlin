package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static UnquotedStringArgumentType string() {
        return new UnquotedStringArgumentType();
    }

    @NotNull
    public static String getUnquotedString(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, String.class);
    }

    @NotNull
    @Override
    public String parse(
            @NotNull StringReader reader
    ) {
        return CommandUtils.readUnquotedString(reader);
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
