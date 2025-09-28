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
import pers.neige.neigeitems.item.ItemPack;
import pers.neige.neigeitems.manager.ItemPackManager;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.Collection;
import java.util.HashMap;

public class ItemPackArgument extends Argument<CommandSender, ItemPackArgument.ItemPackContainer, Unit> {
    public static final ItemPackArgument INSTANCE = new ItemPackArgument();

    private ItemPackArgument() {
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            val itemPackContainer = context.<ItemPackContainer>getLastArgument();
            val params = new HashMap<String, String>();
            params.put("{packID}", itemPackContainer.id);
            LangUtils.sendLang(sender, "Messages.unknownItemPack", params);
        });
    }

    @Override
    @NonNull
    public ParseResult<ItemPackContainer> parse(@NonNull NodeChain<CommandSender, Unit> nodeChain, @NonNull StringReader input, @Nullable CommandSender source) {
        val start = input.getOffset();
        val id = input.readString();
        val item = ItemPackManager.INSTANCE.getItemPack(id);
        if (item == null) {
            input.setOffset(start);
            return new ParseResult<>(new ItemPackContainer(id, null), false);
        }
        return new ParseResult<>(new ItemPackContainer(id, item), true);
    }

    @Override
    protected @NonNull Collection<String> rawTab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        return ItemPackManager.INSTANCE.getItemPacks().keySet();
    }

    @Data
    public static class ItemPackContainer {
        private final @NonNull String id;
        private final @Nullable ItemPack itemPack;
    }
}
