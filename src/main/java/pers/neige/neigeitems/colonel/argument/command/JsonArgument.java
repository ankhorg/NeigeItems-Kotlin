package pers.neige.neigeitems.colonel.argument.command;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.OffsetJSONReader;
import kotlin.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.colonel.arguments.Argument;
import pers.neige.colonel.arguments.ParseResult;
import pers.neige.colonel.context.NodeChain;
import pers.neige.colonel.reader.StringReader;

import java.util.Map;

@Getter
@AllArgsConstructor
public class JsonArgument extends Argument<CommandSender, Map<String, Object>, Unit> {
    public static final JsonArgument INSTANCE = new JsonArgument();

    @Override
    @NonNull
    public ParseResult<Map<String, Object>> parse(@NonNull NodeChain<CommandSender, Unit> nodeChain, @NonNull StringReader input, @Nullable CommandSender source) {
        val start = input.getOffset();
        try {
            val result = OffsetJSONReader.parseObject(
                input.readRemaining(),
                JSONReader.Feature.IgnoreCheckClose,
                JSONReader.Feature.AllowUnQuotedFieldNames,
                JSONReader.Feature.NonStringKeyAsString
            );
            input.setOffset(start);
            input.skip(result.endOffset);
            return new ParseResult<>(result.object, true);
        } catch (JSONException exception) {
            input.setOffset(start);
            return new ParseResult<>(null, false);
        }
    }
}
