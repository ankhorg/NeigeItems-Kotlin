package pers.neige.neigeitems.action.evaluator;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.impl.bool.*;
import pers.neige.neigeitems.action.evaluator.impl.dbl.*;
import pers.neige.neigeitems.action.evaluator.impl.i32.*;
import pers.neige.neigeitems.action.evaluator.impl.i64.*;
import pers.neige.neigeitems.action.evaluator.impl.string.*;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;
import java.util.Locale;

@ToString
public class Evaluator<T> {
    protected final @NonNull BaseActionManager manager;
    protected final @NonNull Class<T> type;

    public Evaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type) {
        this.manager = manager;
        this.type = type;
    }

    public static @NonNull Evaluator<String> createStringEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        if (input == null) return manager.NULL_STRING_EVALUATOR;
        val info = input.split(": ", 2);
        val key = info[0].toLowerCase(Locale.ROOT);
        val content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsStringEvaluator(manager, content);
            case "raw":
                return new RawStringEvaluator(manager, content);
            default:
                if (input.contains("<") && input.contains(">")) {
                    return new ParseStringEvaluator(manager, input);
                } else {
                    return new RawStringEvaluator(manager, input);
                }
        }
    }

    public static @NonNull Evaluator<String> createStringEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        if (input == null) return manager.NULL_STRING_EVALUATOR;
        if (input instanceof Evaluator) return (Evaluator<String>) input;
        if (input instanceof String) {
            return createStringEvaluator(manager, (String) input);
        } else if (input instanceof List<?>) {
            return new ListStringEvaluator(manager, (List<?>) input);
        }
        val config = ConfigReader.parse(input);
        if (config == null) return manager.NULL_STRING_EVALUATOR;
        val type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionStringEvaluator(manager, config);
            }
            case "condition-weight": {
                return new ConditionWeightStringEvaluator(manager, config);
            }
            case "contains": {
                return new ContainsStringEvaluator(manager, config);
            }
            case "double-tree": {
                return new DoubleTreeStringEvaluator(manager, config);
            }
            case "int-tree": {
                return new IntTreeStringEvaluator(manager, config);
            }
            case "key": {
                return new KeyStringEvaluator(manager, config);
            }
            case "weight": {
                return new WeightStringEvaluator(manager, config);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionStringEvaluator(manager, config);
        } else {
            // 处理有人连引号都能忘的情况('js: Math.random()'写成了js: Math.random(), 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return manager.NULL_STRING_EVALUATOR;
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (!(value instanceof String)) return manager.NULL_STRING_EVALUATOR;
            String content = (String) value;
            if ("js".equals(key)) {
                return new JsStringEvaluator(manager, content);
            } else if ("raw".equals(key)) {
                return new RawStringEvaluator(manager, content);
            }
        }
        return manager.NULL_STRING_EVALUATOR;
    }

    public static @NonNull Evaluator<Integer> createIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        if (input == null) return manager.NULL_INTEGER_EVALUATOR;
        val info = input.split(": ", 2);
        val key = info[0].toLowerCase(Locale.ROOT);
        val content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsIntegerEvaluator(manager, content);
            case "raw":
                return new RawIntegerEvaluator(manager, content);
            default:
                val maybeRaw = new RawIntegerEvaluator(manager, input);
                if (maybeRaw.getValue() == null) {
                    return new ParseIntegerEvaluator(manager, input);
                } else {
                    return maybeRaw;
                }
        }
    }

    public static @NonNull Evaluator<Integer> createIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        if (input == null) return manager.NULL_INTEGER_EVALUATOR;
        if (input instanceof Evaluator) return (Evaluator<Integer>) input;
        if (input instanceof String) {
            return createIntegerEvaluator(manager, (String) input);
        } else if (input instanceof Number) {
            return new RawIntegerEvaluator(manager, ((Number) input).intValue());
        } else if (input instanceof List<?>) {
            return new ListIntegerEvaluator(manager, (List<?>) input);
        }
        val config = ConfigReader.parse(input);
        if (config == null) return manager.NULL_INTEGER_EVALUATOR;
        val type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionIntegerEvaluator(manager, config);
            }
            case "condition-weight": {
                return new ConditionWeightIntegerEvaluator(manager, config);
            }
            case "contains": {
                return new ContainsIntegerEvaluator(manager, config);
            }
            case "double-tree": {
                return new DoubleTreeIntegerEvaluator(manager, config);
            }
            case "int-tree": {
                return new IntTreeIntegerEvaluator(manager, config);
            }
            case "key": {
                return new KeyIntegerEvaluator(manager, config);
            }
            case "weight": {
                return new WeightIntegerEvaluator(manager, config);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionIntegerEvaluator(manager, config);
        } else {
            // 处理有人连引号都能忘的情况('js: Math.random()'写成了js: Math.random(), 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return manager.NULL_INTEGER_EVALUATOR;
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (!(value instanceof String)) return manager.NULL_INTEGER_EVALUATOR;
            String content = (String) value;
            if ("js".equals(key)) {
                return new JsIntegerEvaluator(manager, content);
            } else if ("raw".equals(key)) {
                return new RawIntegerEvaluator(manager, content);
            }
        }
        return manager.NULL_INTEGER_EVALUATOR;
    }

    public static @NonNull Evaluator<Long> createLongEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        if (input == null) return manager.NULL_LONG_EVALUATOR;
        val info = input.split(": ", 2);
        val key = info[0].toLowerCase(Locale.ROOT);
        val content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsLongEvaluator(manager, content);
            case "raw":
                return new RawLongEvaluator(manager, content);
            default:
                val maybeRaw = new RawLongEvaluator(manager, input);
                if (maybeRaw.getValue() == null) {
                    return new ParseLongEvaluator(manager, input);
                } else {
                    return maybeRaw;
                }
        }
    }

    public static @NonNull Evaluator<Long> createLongEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        if (input == null) return manager.NULL_LONG_EVALUATOR;
        if (input instanceof Evaluator) return (Evaluator<Long>) input;
        if (input instanceof String) {
            return createLongEvaluator(manager, (String) input);
        } else if (input instanceof Number) {
            return new RawLongEvaluator(manager, ((Number) input).longValue());
        } else if (input instanceof List<?>) {
            return new ListLongEvaluator(manager, (List<?>) input);
        }
        val config = ConfigReader.parse(input);
        if (config == null) return manager.NULL_LONG_EVALUATOR;
        val type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionLongEvaluator(manager, config);
            }
            case "condition-weight": {
                return new ConditionWeightLongEvaluator(manager, config);
            }
            case "contains": {
                return new ContainsLongEvaluator(manager, config);
            }
            case "double-tree": {
                return new DoubleTreeLongEvaluator(manager, config);
            }
            case "int-tree": {
                return new IntTreeLongEvaluator(manager, config);
            }
            case "key": {
                return new KeyLongEvaluator(manager, config);
            }
            case "weight": {
                return new WeightLongEvaluator(manager, config);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionLongEvaluator(manager, config);
        } else {
            // 处理有人连引号都能忘的情况('js: Math.random()'写成了js: Math.random(), 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return manager.NULL_LONG_EVALUATOR;
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (!(value instanceof String)) return manager.NULL_LONG_EVALUATOR;
            String content = (String) value;
            if ("js".equals(key)) {
                return new JsLongEvaluator(manager, content);
            } else if ("raw".equals(key)) {
                return new RawLongEvaluator(manager, content);
            }
        }
        return manager.NULL_LONG_EVALUATOR;
    }

    public static @NonNull Evaluator<Double> createDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        if (input == null) return manager.NULL_DOUBLE_EVALUATOR;
        val info = input.split(": ", 2);
        val key = info[0].toLowerCase(Locale.ROOT);
        val content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsDoubleEvaluator(manager, content);
            case "raw":
                return new RawDoubleEvaluator(manager, content);
            default:
                val maybeRaw = new RawDoubleEvaluator(manager, input);
                if (maybeRaw.getValue() == null) {
                    return new ParseDoubleEvaluator(manager, input);
                } else {
                    return maybeRaw;
                }
        }
    }

    public static @NonNull Evaluator<Double> createDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        if (input == null) return manager.NULL_DOUBLE_EVALUATOR;
        if (input instanceof Evaluator) return (Evaluator<Double>) input;
        if (input instanceof String) {
            return createDoubleEvaluator(manager, (String) input);
        } else if (input instanceof Number) {
            return new RawDoubleEvaluator(manager, ((Number) input).doubleValue());
        } else if (input instanceof List<?>) {
            return new ListDoubleEvaluator(manager, (List<?>) input);
        }
        val config = ConfigReader.parse(input);
        if (config == null) return manager.NULL_DOUBLE_EVALUATOR;
        val type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionDoubleEvaluator(manager, config);
            }
            case "condition-weight": {
                return new ConditionWeightDoubleEvaluator(manager, config);
            }
            case "contains": {
                return new ContainsDoubleEvaluator(manager, config);
            }
            case "double-tree": {
                return new DoubleTreeDoubleEvaluator(manager, config);
            }
            case "int-tree": {
                return new IntTreeDoubleEvaluator(manager, config);
            }
            case "key": {
                return new KeyDoubleEvaluator(manager, config);
            }
            case "weight": {
                return new WeightDoubleEvaluator(manager, config);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionDoubleEvaluator(manager, config);
        } else {
            // 处理有人连引号都能忘的情况('js: Math.random()'写成了js: Math.random(), 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return manager.NULL_DOUBLE_EVALUATOR;
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (!(value instanceof String)) return manager.NULL_DOUBLE_EVALUATOR;
            String content = (String) value;
            if ("js".equals(key)) {
                return new JsDoubleEvaluator(manager, content);
            } else if ("raw".equals(key)) {
                return new RawDoubleEvaluator(manager, content);
            }
        }
        return manager.NULL_DOUBLE_EVALUATOR;
    }

    public static @NonNull Evaluator<Boolean> createBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        if (input == null) return manager.NULL_BOOLEAN_EVALUATOR;
        val info = input.split(": ", 2);
        val key = info[0].toLowerCase(Locale.ROOT);
        val content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsBooleanEvaluator(manager, content);
            case "raw":
                return new RawBooleanEvaluator(manager, content);
            default:
                val maybeRaw = new RawBooleanEvaluator(manager, input);
                if (maybeRaw.getValue() == null) {
                    return new ParseBooleanEvaluator(manager, input);
                } else {
                    return maybeRaw;
                }
        }
    }

    public static @NonNull Evaluator<Boolean> createBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        if (input == null) return manager.NULL_BOOLEAN_EVALUATOR;
        if (input instanceof Evaluator) return (Evaluator<Boolean>) input;
        if (input instanceof String) {
            return createBooleanEvaluator(manager, (String) input);
        } else if (input instanceof Boolean) {
            return new RawBooleanEvaluator(manager, (Boolean) input);
        } else if (input instanceof List<?>) {
            return new ListBooleanEvaluator(manager, (List<?>) input);
        }
        val config = ConfigReader.parse(input);
        if (config == null) return manager.NULL_BOOLEAN_EVALUATOR;
        val type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionBooleanEvaluator(manager, config);
            }
            case "condition-weight": {
                return new ConditionWeightBooleanEvaluator(manager, config);
            }
            case "contains": {
                return new ContainsBooleanEvaluator(manager, config);
            }
            case "double-tree": {
                return new DoubleTreeBooleanEvaluator(manager, config);
            }
            case "int-tree": {
                return new IntTreeBooleanEvaluator(manager, config);
            }
            case "key": {
                return new KeyBooleanEvaluator(manager, config);
            }
            case "weight": {
                return new WeightBooleanEvaluator(manager, config);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionBooleanEvaluator(manager, config);
        } else {
            // 处理有人连引号都能忘的情况('js: Math.random()'写成了js: Math.random(), 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return manager.NULL_BOOLEAN_EVALUATOR;
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (!(value instanceof String)) return manager.NULL_BOOLEAN_EVALUATOR;
            String content = (String) value;
            if ("js".equals(key)) {
                return new JsBooleanEvaluator(manager, content);
            } else if ("raw".equals(key)) {
                return new RawBooleanEvaluator(manager, content);
            }
        }
        return manager.NULL_BOOLEAN_EVALUATOR;
    }

    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        return def;
    }

    public @Nullable T get(@NonNull ActionContext context) {
        return getOrDefault(context, null);
    }

    public @NonNull BaseActionManager getManager() {
        return manager;
    }

    public @NonNull Class<T> getType() {
        return type;
    }
}
