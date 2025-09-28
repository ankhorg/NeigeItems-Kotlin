package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.Data;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.colonel.arguments.Argument;
import pers.neige.colonel.arguments.ParseResult;
import pers.neige.colonel.context.Context;
import pers.neige.colonel.context.NodeChain;
import pers.neige.colonel.reader.StringReader;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.Collection;
import java.util.HashMap;

public class ItemArgument extends Argument<CommandSender, ItemArgument.ItemContainer, Unit> {
    public static final ItemArgument INSTANCE = new ItemArgument();

    private ItemArgument() {
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            val itemContainer = context.<ItemContainer>getLastArgument();
            val params = new HashMap<String, String>();
            params.put("{itemID}", itemContainer.id);
            LangUtils.sendLang(sender, "Messages.unknownItem", params);
        });
    }

    @Override
    @NonNull
    public ParseResult<ItemContainer> parse(@NonNull NodeChain<CommandSender, Unit> nodeChain, @NonNull StringReader input, @Nullable CommandSender source) {
        val start = input.getOffset();
        val id = input.readString();
        val item = ItemManager.INSTANCE.getItem(id);
        if (item == null) {
            input.setOffset(start);
            return new ParseResult<>(new ItemContainer(id, null), false);
        }
        return new ParseResult<>(new ItemContainer(id, item), true);
    }

    @Override
    protected @NonNull Collection<String> rawTab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        return ItemManager.INSTANCE.getItems().keySet();
    }

    @Data
    public static class ItemContainer {
        private final @NonNull String id;
        private final @Nullable ItemGenerator itemGenerator;
    }
}
