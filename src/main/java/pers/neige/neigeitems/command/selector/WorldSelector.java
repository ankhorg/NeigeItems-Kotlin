package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class WorldSelector extends UnquotedStringSelector<World> {
    public WorldSelector(@NonNull StringReader reader) {
        super(reader);
    }

    @Override
    public @Nullable World select(CommandContext<CommandSender> context) {
        return Bukkit.getWorld(text);
    }
}
