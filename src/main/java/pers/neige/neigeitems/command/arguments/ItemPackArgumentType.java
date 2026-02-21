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

    public static @NonNull ItemPackArgumentType pack() {
        return new ItemPackArgumentType();
    }

    public static @Nullable ItemPack getPack(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return getItemPackSelector(context, name).select(context);
    }

    public static @NonNull ItemPackSelector getItemPackSelector(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, ItemPackSelector.class);
    }

    @Override
    public @NonNull ItemPackSelector parse(
        @NonNull StringReader reader
    ) {
        return new ItemPackSelector(reader);
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
        @NonNull CommandContext<S> context,
        @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ItemPackManager.INSTANCE.getItemPacks().keySet().forEach((id) -> {
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
