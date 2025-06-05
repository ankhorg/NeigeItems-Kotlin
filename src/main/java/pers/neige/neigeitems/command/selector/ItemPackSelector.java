package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemPack;
import pers.neige.neigeitems.manager.ItemPackManager;

public class ItemPackSelector extends UnquotedStringSelector<ItemPack> {
    public ItemPackSelector(@NonNull StringReader reader) {
        super(reader);
    }

    @Override
    public @Nullable ItemPack select(CommandContext<CommandSender> context) {
        return ItemPackManager.INSTANCE.getItemPack(text);
    }
}
