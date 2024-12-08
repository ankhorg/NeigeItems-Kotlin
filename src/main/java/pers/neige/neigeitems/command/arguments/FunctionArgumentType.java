package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static FunctionArgumentType function() {
        return new FunctionArgumentType();
    }

    @Nullable
    public static Action getFunction(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return getFunctionSelector(context, name).select(context);
    }

    @NotNull
    public static FunctionSelector getFunctionSelector(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, FunctionSelector.class);
    }

    @NotNull
    @Override
    public FunctionSelector parse(
            @NotNull StringReader reader
    ) {
        return new FunctionSelector(reader);
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ActionManager.INSTANCE.getFunctions().keySet().forEach((id) -> {
            if (id.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(id);
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
