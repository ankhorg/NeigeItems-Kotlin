package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.selector.WorldSelector;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * 世界参数类型
 */
public class WorldArgumentType implements ArgumentType<WorldSelector> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("world");

    private WorldArgumentType() {
    }

    @NotNull
    public static WorldArgumentType world() {
        return new WorldArgumentType();
    }

    @Nullable
    public static World getWorld(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return getWorldSelector(context, name).select(context);
    }

    @NotNull
    public static WorldSelector getWorldSelector(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, WorldSelector.class);
    }

    @NotNull
    @Override
    public WorldSelector parse(
            @NotNull StringReader reader
    ) {
        return new WorldSelector(reader);
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String lowerCaseRemaining = builder.getRemaining().toLowerCase();
        Bukkit.getWorlds().forEach((world) -> {
            String name = world.getName();
            if (name.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(name);
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
