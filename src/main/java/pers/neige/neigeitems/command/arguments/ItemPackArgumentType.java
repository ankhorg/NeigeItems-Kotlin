package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.selector.ItemPackSelector;
import pers.neige.neigeitems.item.ItemPack;
import pers.neige.neigeitems.manager.ItemPackManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * NI物品包生成器参数类型
 */
public class ItemPackArgumentType implements ArgumentType<ItemPackSelector> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("ExampleItemPack");

    private ItemPackArgumentType() {
    }

    @NotNull
    public static ItemPackArgumentType pack() {
        return new ItemPackArgumentType();
    }

    @Nullable
    public static ItemPack getPack(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return getItemPackSelector(context, name).select(context);
    }

    @NotNull
    public static ItemPackSelector getItemPackSelector(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, ItemPackSelector.class);
    }

    @NotNull
    @Override
    public ItemPackSelector parse(
            @NotNull StringReader reader
    ) {
        return new ItemPackSelector(reader);
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ItemPackManager.INSTANCE.getItemPacks().keySet().forEach((id) -> {
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
