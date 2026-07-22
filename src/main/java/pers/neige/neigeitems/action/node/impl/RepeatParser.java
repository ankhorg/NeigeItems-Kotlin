package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.script.CompiledScript;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 对齐旧 section RepeatParser：支持 transform；非法/缺省 repeat 默认 1；无上限。
 */
public class RepeatParser extends NodeParser {
    /**
     * 用于 repeat 节点的已编译 transform 脚本缓存（与旧 section 一致）。
     */
    private static final ConcurrentHashMap<String, CompiledScript> compiledScripts = new ConcurrentHashMap<>();

    public RepeatParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "repeat";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            context,
            getParsedValue(context, params, "content"),
            getParsedValue(context, params, "repeat"),
            getParsedValue(context, params, "separator"),
            getParsedValue(context, params, "prefix"),
            getParsedValue(context, params, "postfix"),
            // transform 文本本身不做节点解析，对齐旧 section
            params.getString("transform")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\', 3);
        if (paramsList.isEmpty()) {
            warning("缺少 content 参数");
            return null;
        }
        return handle(
            context,
            paramsList.get(0),
            ListUtils.getOrNull(paramsList, 1),
            ListUtils.getOrNull(paramsList, 2),
            null,
            null,
            null
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable String content,
        @Nullable String repeatText,
        @Nullable String separator,
        @Nullable String prefix,
        @Nullable String postfix,
        @Nullable String rawTransform
    ) {
        // 旧：content 默认 ""
        val realContent = content == null ? "" : content;
        // 旧：toIntOrNull 失败 → 默认 1；coerceAtLeast(0)
        int length = 1;
        if (repeatText != null && !repeatText.isEmpty()) {
            val parsed = NumberParser.parseInteger(repeatText);
            if (parsed != null) {
                length = Math.max(parsed, 0);
            }
        }

        CompiledScript transform = null;
        if (rawTransform != null && !rawTransform.isEmpty()) {
            transform = compiledScripts.computeIfAbsent(rawTransform, script -> new CompiledScript(
                "function main() {\n" +
                    script + "\n" +
                    "}"
            ));
        }

        val result = new StringBuilder();
        if (prefix != null) {
            result.append(prefix);
        }

        val map = new HashMap<String, Object>();
        if (transform != null) {
            val caster = context.getCaster();
            if (caster instanceof OfflinePlayer) {
                map.put("player", caster);
            }
            map.put("it", realContent);
            map.put("vars", (Function<String, String>) string -> this.manager.parseNode(string, context));
        }

        for (int index = 0; index < length; index++) {
            var element = realContent;
            if (transform != null) {
                map.put("index", index);
                val transformed = transform.invoke("main", map);
                element = transformed == null ? "" : transformed.toString();
            }
            result.append(element);
            if (separator != null && index != length - 1) {
                result.append(separator);
            }
        }
        if (postfix != null) {
            result.append(postfix);
        }
        return result.toString();
    }
}
