package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.manager.ActionManager;

public class FunctionSelector extends UnquotedStringSelector<Action> {
    public FunctionSelector(@NotNull StringReader reader) {
        super(reader);
    }

    @Nullable
    @Override
    public Action select(CommandContext<CommandSender> context) {
        return ActionManager.INSTANCE.getFunctions().get(text);
    }
}
