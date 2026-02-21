package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.command.selector.FunctionSelector;
import pers.neige.neigeitems.manager.ActionManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class FunctionArgumentType implements ArgumentType<FunctionSelector> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("test");

    private FunctionArgumentType() {
    }

    public static @NonNull FunctionArgumentType function() {
        return new FunctionArgumentType();
    }

    public static @Nullable Action getFunction(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return getFunctionSelector(context, name).select(context);
    }

    public static @NonNull FunctionSelector getFunctionSelector(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, FunctionSelector.class);
    }

    @Override
    public @NonNull FunctionSelector parse(
        @NonNull StringReader reader
    ) {
        return new FunctionSelector(reader);
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
        @NonNull CommandContext<S> context,
        @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ActionManager.INSTANCE.getFunctions().keySet().forEach((id) -> {
            if (id.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(id);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
