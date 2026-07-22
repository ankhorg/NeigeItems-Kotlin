package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对齐旧 section WhenParser：条件在独立 ActionContext 中求值，
 * 父 context 的 section cache 临时写入 value 供 result 解析，finally 恢复。
 */
public class WhenParser extends NodeParser {
    public WhenParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "when";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            context,
            getParsedValue(context, params, "value"),
            params.get("conditions")
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable String value,
        @Nullable Object conditions
    ) {
        if (!(conditions instanceof List<?>)) {
            warning("缺少 conditions 参数或类型不正确");
            return null;
        }
        val conditionList = (List<?>) conditions;
        val cache = context.getSectionCache();
        val previousCacheValue = cache.get("value");
        val hasValue = value != null;
        if (hasValue) {
            cache.put("value", value);
        }
        try {
            for (val info : conditionList) {
                if (info instanceof Map<?, ?>) {
                    val map = (Map<?, ?>) info;
                    val conditionObject = map.get("condition");
                    // 旧实现：condition !is String? 时跳过该分支
                    if (conditionObject != null && !(conditionObject instanceof String)) {
                        continue;
                    }
                    val condition = conditionObject == null ? null : conditionObject.toString();
                    if (!checkCondition(context, value, condition)) {
                        continue;
                    }
                    // 旧实现：result 为 null 时 result.toString() → "null"
                    val resultObject = map.get("result");
                    return this.manager.parseNode(String.valueOf(resultObject), context);
                }
                // 非 Map（含 null）视为默认分支；null.toString() 在 Kotlin 为 "null"
                return this.manager.parseNode(String.valueOf(info), context);
            }
            return null;
        } finally {
            restore("value", cache, previousCacheValue, hasValue);
        }
    }

    private boolean checkCondition(
        @NonNull ActionContext context,
        @Nullable String value,
        @Nullable String condition
    ) {
        // 与旧 WhenParser 一致：子 ActionContext(player, params, params)
        // params 含 value / cache / sections，条件脚本只见这套绑定
        val params = new HashMap<String, Object>();
        params.put("value", value);
        params.put("cache", context.getSectionCache());
        params.put("sections", context.get(ContextKeys.SECTIONS));
        val child = new ActionContext(context.getPlayer(), params, params);
        return !this.manager.parseCondition(condition, child).isStop();
    }

    private void restore(
        @NonNull String key,
        @NonNull Map<String, ?> map,
        @Nullable Object previous,
        boolean wrote
    ) {
        if (!wrote) return;
        @SuppressWarnings("unchecked")
        val mutable = (Map<String, Object>) map;
        if (previous == null) {
            mutable.remove(key);
        } else {
            mutable.put(key, previous);
        }
    }
}
