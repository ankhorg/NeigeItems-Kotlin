package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;

public class ItemSelector {
    @NotNull
    private final String id;

    public ItemSelector(
            @NotNull String id
    ) {
        this.id = id;
    }

    @Nullable
    public ItemGenerator getItem(CommandContext<CommandSender> context) {
        return ItemManager.INSTANCE.getItem(id);
    }

    @NotNull
    public String getId() {
        return id;
    }
}
