package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemPack;
import pers.neige.neigeitems.manager.ItemPackManager;

public class ItemPackSelector {
    @NotNull
    private final String id;

    public ItemPackSelector(
            @NotNull String id
    ) {
        this.id = id;
    }

    @Nullable
    public ItemPack getPack(CommandContext<CommandSender> context) {
        return ItemPackManager.INSTANCE.getItemPack(id);
    }

    @NotNull
    public String getId() {
        return id;
    }
}
