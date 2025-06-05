package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.manager.ActionManager;

public class FunctionSelector extends UnquotedStringSelector<Action> {
    public FunctionSelector(@NonNull StringReader reader) {
        super(reader);
    }

    @Override
    public @Nullable Action select(CommandContext<CommandSender> context) {
        return ActionManager.INSTANCE.getFunctions().get(text);
    }
}
