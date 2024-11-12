package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.command.CommandUtils;
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker;
import pers.neige.neigeitems.manager.HookerManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * MM物品ID生成器参数类型
 */
public class MMItemIDArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("SkeletonKingSword");

    private MMItemIDArgumentType() {
    }

    @NotNull
    public static MMItemIDArgumentType mmItemID() {
        return new MMItemIDArgumentType();
    }

    @NotNull
    public static String getMMItemID(
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
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        MythicMobsHooker hooker = HookerManager.INSTANCE.getMythicMobsHooker();
        if (hooker != null) {
            String lowerCaseRemaining = builder.getRemaining().toLowerCase();
            hooker.getItemIds().forEach((id) -> {
                if (id.toLowerCase().startsWith(lowerCaseRemaining)) {
                    builder.suggest(id);
                }
            });
        }
        return builder.buildFuture();
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
