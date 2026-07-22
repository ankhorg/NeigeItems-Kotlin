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
import pers.neige.neigeitems.utils.NumberParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * 对齐旧 section JoinParser：支持 transform / 空列表拼前后缀 /
 * 非法 limit 视为无限制 / 非法 shuffled 视为 false / 先 shuffle 再逐项解析。
 */
public class JoinParser extends NodeParser {
    /**
     * 用于 join 节点的已编译 transform 脚本缓存（与旧 section 一致）。
     */
    private static final ConcurrentHashMap<String, CompiledScript> compiledScripts = new ConcurrentHashMap<>();

    public JoinParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "join";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        // 优先 values（现代别名），否则 list（旧键）；仅当值不是 List 时视为 null
        val valuesObject = params.containsKey("values")
            ? params.get("values")
            : params.get("list");
        List<String> values = null;
        if (valuesObject instanceof List<?>) {
            values = new ArrayList<>();
            for (val object : (List<?>) valuesObject) {
                if (object == null) continue;
                values.add(object.toString());
            }
        }
        return handle(
            context,
            values,
            getParsedValue(context, params, "separator"),
            getParsedValue(context, params, "prefix"),
            getParsedValue(context, params, "postfix"),
            getParsedValue(context, params, "limit"),
            getParsedValue(context, params, "truncated"),
            // transform 文本本身不做节点解析，对齐旧 section
            params.getString("transform"),
            getParsedValue(context, params, "shuffled")
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable List<String> list,
        @Nullable String rawSeparator,
        @Nullable String rawPrefix,
        @Nullable String rawPostfix,
        @Nullable String rawLimit,
        @Nullable String truncated,
        @Nullable String rawTransform,
        @Nullable String rawShuffled
    ) {
        // 旧实现：list 为 null 才返回 null；空列表继续拼 prefix+postfix
        if (list == null) return null;

        val separator = rawSeparator == null ? ", " : rawSeparator;
        val prefix = rawPrefix == null ? "" : rawPrefix;
        val postfix = rawPostfix == null ? "" : rawPostfix;

        // 非法 limit → 无限制（对齐 toIntOrNull 失败）
        Integer limit = null;
        if (rawLimit != null && !rawLimit.isEmpty()) {
            val parsedLimit = NumberParser.parseInteger(rawLimit);
            if (parsedLimit != null) {
                if (parsedLimit < 0) {
                    limit = 0;
                } else if (parsedLimit < list.size()) {
                    limit = parsedLimit;
                }
            }
        }

        // toBooleanStrictOrNull：仅精确 "true"/"false"（大小写敏感）
        boolean shuffled = "true".equals(rawShuffled);

        List<String> realList = list;
        if (shuffled) {
            realList = new ArrayList<>(list);
            Collections.shuffle(realList, ThreadLocalRandom.current());
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
        result.append(prefix);
        val length = limit == null ? realList.size() : limit;

        val map = new HashMap<String, Object>();
        if (transform != null) {
            val caster = context.getCaster();
            if (caster instanceof OfflinePlayer) {
                map.put("player", caster);
            }
            map.put("list", realList);
            map.put("vars", (Function<String, String>) string -> this.manager.parseNode(string, context));
        }

        for (int index = 0; index < length; index++) {
            // 先取原始串再 parse，对齐旧「shuffle 原始 → 循环内 parse」
            var element = this.manager.parseNode(realList.get(index), context);
            if (transform != null) {
                map.put("it", element);
                map.put("index", index);
                val transformed = transform.invoke("main", map);
                element = transformed == null ? "" : transformed.toString();
            }
            result.append(element);
            if (index != length - 1 || (limit != null && truncated != null)) {
                result.append(separator);
            }
        }
        if (limit != null && truncated != null) {
            result.append(truncated);
        }
        result.append(postfix);
        return result.toString();
    }
}
