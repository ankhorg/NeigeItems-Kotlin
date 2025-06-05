package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
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

    public static @NonNull WorldArgumentType world() {
        return new WorldArgumentType();
    }

    public static @Nullable World getWorld(
            @NonNull CommandContext<CommandSender> context,
            @NonNull String name
    ) {
        return getWorldSelector(context, name).select(context);
    }

    public static @NonNull WorldSelector getWorldSelector(
            @NonNull CommandContext<CommandSender> context,
            @NonNull String name
    ) {
        return context.getArgument(name, WorldSelector.class);
    }

    @Override
    public @NonNull WorldSelector parse(
            @NonNull StringReader reader
    ) {
        return new WorldSelector(reader);
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
            @NonNull CommandContext<S> context,
            @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        Bukkit.getWorlds().forEach((world) -> {
            val name = world.getName();
            if (name.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(name);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
