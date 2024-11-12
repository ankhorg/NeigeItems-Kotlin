package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldSelector extends UnquotedStringSelector<World> {
    public WorldSelector(@NotNull StringReader reader) {
        super(reader);
    }

    @Nullable
    @Override
    public World select(CommandContext<CommandSender> context) {
        return Bukkit.getWorld(text);
    }
}
