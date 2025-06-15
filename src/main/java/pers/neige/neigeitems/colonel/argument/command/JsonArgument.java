package pers.neige.neigeitems.colonel.argument.command;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import kotlin.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.colonel.arguments.Argument;
import pers.neige.colonel.arguments.ParseResult;
import pers.neige.colonel.reader.StringReader;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class JsonArgument<A> extends Argument<CommandSender, A, Unit> {
    public static final JsonArgument<HashMap<?, ?>> HASH_MAP = new JsonArgument(HashMap.class);
    private final @NonNull Class<A> type;

    @Override
    @NonNull
    public ParseResult<A> parse(@NonNull StringReader input, @Nullable CommandSender source) {
        val start = input.getOffset();
        try {
            A result = JSON.parseObject(input.readRemaining(), type);
            return new ParseResult<>(result, true);
        } catch (JSONException exception) {
            input.setOffset(start);
            return new ParseResult<>(null, false);
        }
    }
}
