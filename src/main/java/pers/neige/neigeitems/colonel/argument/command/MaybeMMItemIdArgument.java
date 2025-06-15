package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.colonel.arguments.impl.StringArgument;
import pers.neige.colonel.context.Context;
import pers.neige.neigeitems.manager.HookerManager;

import java.util.ArrayList;
import java.util.Collection;

public class MaybeMMItemIdArgument extends StringArgument<CommandSender, Unit> {
    public static final MaybeMMItemIdArgument INSTANCE = new MaybeMMItemIdArgument();

    private MaybeMMItemIdArgument() {
        super();
    }

    @Override
    protected @NonNull Collection<String> rawTab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        val hooker = HookerManager.INSTANCE.getMythicMobsHooker();
        if (hooker == null) return new ArrayList<>();
        return hooker.getItemIds();
    }
}
