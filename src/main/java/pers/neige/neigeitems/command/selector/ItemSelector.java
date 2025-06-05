package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;

public class ItemSelector extends UnquotedStringSelector<ItemGenerator> {
    public ItemSelector(@NonNull StringReader reader) {
        super(reader);
    }

    @Override
    public @Nullable ItemGenerator select(CommandContext<CommandSender> context) {
        return ItemManager.INSTANCE.getItem(text);
    }
}
