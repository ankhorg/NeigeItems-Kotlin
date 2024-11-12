package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;

public class ItemSelector extends UnquotedStringSelector<ItemGenerator> {
    public ItemSelector(@NotNull StringReader reader) {
        super(reader);
    }

    @Nullable
    @Override
    public ItemGenerator select(CommandContext<CommandSender> context) {
        return ItemManager.INSTANCE.getItem(text);
    }
}
