package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
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

    public static @NonNull ActionArgumentType action() {
        return new ActionArgumentType();
    }

    public static @NonNull Action getAction(
            @NonNull CommandContext<CommandSender> context,
            @NonNull String name
    ) {
        return context.getArgument(name, Action.class);
    }

    @Override
    public @NonNull Action parse(
            @NonNull StringReader reader
    ) {
        return ActionManager.INSTANCE.compile(readAllString(reader));
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
            @NonNull CommandContext<S> context,
            @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ActionManager.INSTANCE.getActions().keySet().forEach((key) -> {
            if (key.startsWith(lowerCaseRemaining)) {
                builder.suggest(key + ": ");
            }
        });
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
