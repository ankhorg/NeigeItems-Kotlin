package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.Data;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.colonel.arguments.Argument;
import pers.neige.colonel.arguments.ParseResult;
import pers.neige.colonel.context.Context;
import pers.neige.colonel.reader.StringReader;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.manager.ActionManager;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.Collection;
import java.util.HashMap;

public class FunctionArgument extends Argument<CommandSender, FunctionArgument.FunctionContainer, Unit> {
    public static final FunctionArgument INSTANCE = new FunctionArgument();

    private FunctionArgument() {
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            val functionContainer = context.<FunctionContainer>getLastArgument();
            val params = new HashMap<String, String>();
            params.put("{function}", functionContainer.id);
            LangUtils.sendLang(sender, "Messages.invalidFunction", params);
        });
    }

    @Override
    @NonNull
    public ParseResult<FunctionContainer> parse(@NonNull StringReader input, @Nullable CommandSender source) {
        val start = input.getOffset();
        val id = input.readString();
        val function = ActionManager.INSTANCE.getFunctions().get(id);
        if (function == null) {
            input.setOffset(start);
            return new ParseResult<>(new FunctionContainer(id, null), false);
        }
        return new ParseResult<>(new FunctionContainer(id, function), true);
    }

    @Override
    protected @NonNull Collection<String> rawTab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        return ActionManager.INSTANCE.getFunctions().keySet();
    }

    @Data
    public static class FunctionContainer {
        private final @NonNull String id;
        private final @Nullable Action function;
    }
}
