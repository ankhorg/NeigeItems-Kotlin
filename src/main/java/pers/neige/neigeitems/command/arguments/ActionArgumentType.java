package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.manager.ActionManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static pers.neige.neigeitems.command.CommandUtils.readAllString;

/**
 * 动作参数类型
 */
public class ActionArgumentType implements ArgumentType<Action> {
    private static final Collection<String> EXAMPLES = Arrays.asList("tell: 123", "console: say 123");

    private ActionArgumentType() {
    }

    @NotNull
    public static ActionArgumentType action() {
        return new ActionArgumentType();
    }

    @NotNull
    public static Action getAction(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, Action.class);
    }

    @NotNull
    @Override
    public Action parse(
            @NotNull StringReader reader
    ) {
        return ActionManager.INSTANCE.compile(readAllString(reader));
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        ActionManager.INSTANCE.getActions().keySet().forEach((key) -> {
            if (key.startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(key + ": ");
            }
        });
        return builder.buildFuture();
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
