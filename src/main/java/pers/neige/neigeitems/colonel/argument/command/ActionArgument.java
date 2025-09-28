package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.colonel.arguments.Argument;
import pers.neige.colonel.arguments.ParseResult;
import pers.neige.colonel.context.Context;
import pers.neige.colonel.context.NodeChain;
import pers.neige.colonel.reader.StringReader;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.manager.ActionManager;

import java.util.ArrayList;
import java.util.List;

public class ActionArgument extends Argument<CommandSender, Action, Unit> {
    public static final ActionArgument INSTANCE = new ActionArgument();

    private ActionArgument() {
    }

    @Override
    @NonNull
    public ParseResult<Action> parse(@NonNull NodeChain<CommandSender, Unit> nodeChain, @NonNull StringReader input, @Nullable CommandSender source) {
        return new ParseResult<>(ActionManager.INSTANCE.compile(input.readRemaining()), true);
    }

    @Override
    public @NonNull List<String> tab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        val lowerCaseRemaining = remaining.toLowerCase();
        val result = new ArrayList<String>();
        ActionManager.INSTANCE.getActions().keySet().forEach((key) -> {
            if (key.startsWith(lowerCaseRemaining)) {
                result.add(key + ": ");
            }
        });
        return result;
    }
}
