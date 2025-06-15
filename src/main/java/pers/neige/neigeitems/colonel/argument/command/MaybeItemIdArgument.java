package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import pers.neige.colonel.arguments.impl.StringArgument;
import pers.neige.colonel.context.Context;
import pers.neige.neigeitems.manager.ItemManager;

import java.util.Collection;

public class MaybeItemIdArgument extends StringArgument<CommandSender, Unit> {
    public static final MaybeItemIdArgument INSTANCE = new MaybeItemIdArgument();

    private MaybeItemIdArgument() {
        super();
    }

    @Override
    protected @NonNull Collection<String> rawTab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        return ItemManager.INSTANCE.getItems().keySet();
    }
}
