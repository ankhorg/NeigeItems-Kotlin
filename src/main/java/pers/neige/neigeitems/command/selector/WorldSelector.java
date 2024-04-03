package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldSelector {
    @NotNull
    private final String name;

    public WorldSelector(
            @NotNull String name
    ) {
        this.name = name;
    }

    @Nullable
    public World getWorld(CommandContext<CommandSender> context) {
        return Bukkit.getWorld(name);
    }

    @NotNull
    public String getName() {
        return name;
    }
}
