package pers.neige.neigeitems.action.evaluator;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.impl.*;
import pers.neige.neigeitems.action.evaluator.impl.converter.*;
import pers.neige.neigeitems.action.evaluator.impl.list.*;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;
import java.util.Locale;

@ToString
@AllArgsConstructor
public class Evaluator<T> {
    protected final @NonNull BaseActionManager manager;
    protected final @NonNull Class<T> type;

    public static @NonNull Evaluator<String> createStringEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        return createEvaluator(manager, String.class, input, StringConverter.INSTANCE);
    }

    public static @NonNull Evaluator<String> createStringEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createEvaluator(manager, String.class, input, StringConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Integer> createIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        return createEvaluator(manager, Integer.class, input, IntegerConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Integer> createIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createEvaluator(manager, Integer.class, input, IntegerConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Long> createLongEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        return createEvaluator(manager, Long.class, input, LongConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Long> createLongEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createEvaluator(manager, Long.class, input, LongConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Double> createDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        return createEvaluator(manager, Double.class, input, DoubleConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Double> createDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createEvaluator(manager, Double.class, input, DoubleConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Boolean> createBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable String input) {
        return createEvaluator(manager, Boolean.class, input, BooleanConverter.INSTANCE);
    }

    public static @NonNull Evaluator<Boolean> createBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createEvaluator(manager, Boolean.class, input, BooleanConverter.INSTANCE);
    }

    public static <T> @NonNull Evaluator<T> createEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @Nullable Object input
    ) {
        return createEvaluator(manager, type, input, new EvaluatorConverter<>(type));
    }

    public static <T> @NonNull Evaluator<T> createEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @Nullable String input,
        @NonNull EvaluatorConverter<T> converter
    ) {
        if (input == null) return manager.getNullEvaluator(type);
        val info = input.split(": ", 2);
        val key = info[0].toLowerCase(Locale.ROOT);
        val content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsEvaluator<>(manager, type, content, converter);
            case "raw":
                return new RawEvaluator<>(manager, type, content, converter);
            default:
                val maybe = converter.parseStaticValue(input);
                if (maybe != null) {
                    return new RawEvaluator<>(manager, type, maybe);
                }
                return new ParseEvaluator<>(manager, type, input, converter);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @NonNull Evaluator<T> createEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @Nullable Object input,
        @NonNull EvaluatorConverter<T> converter
    ) {
        if (input == null) return manager.getNullEvaluator(type);
        if (input instanceof Evaluator) {
            val evaluator = (Evaluator<T>) input;
            if (type.isAssignableFrom(evaluator.type)) {
                return evaluator;
            } else {
                return manager.getNullEvaluator(type);
            }
        }
        T maybe;
        if (input instanceof String) {
            return createEvaluator(manager, type, (String) input, converter);
        } else if ((maybe = converter.convert(input)) != null) {
            return new RawEvaluator<>(manager, type, maybe);
        } else if (input instanceof List<?>) {
            return new ListEvaluator<>(manager, type, (List<?>) input, (element) -> createEvaluator(manager, type, element, converter));
        }
        val config = ConfigReader.parse(input);
        if (config == null) {
            return createEvaluator(manager, type, input.toString(), converter);
        }
        val configType = config.getString("type", "").toLowerCase(Locale.ROOT);
        switch (configType) {
            case "condition": {
                return new ConditionEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
            case "condition-weight": {
                return new ConditionWeightEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
            case "contains": {
                return new ContainsEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
            case "double-tree": {
                return new DoubleTreeEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
            case "int-tree": {
                return new IntTreeEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
            case "key": {
                return new KeyEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
            case "weight": {
                return new WeightEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionEvaluator<>(manager, type, config, (element) -> createEvaluator(manager, type, element, converter));
        } else {
            // 处理有人连引号都能忘的情况('js: Math.random()'写成了js: Math.random(), 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return manager.getNullEvaluator(type);
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (value == null) return manager.getNullEvaluator(type);
            val content = value.toString();
            if ("js".equals(key)) {
                return new JsEvaluator<>(manager, type, content, converter);
            } else if ("raw".equals(key)) {
                return new RawEvaluator<>(manager, type, content, converter);
            }
        }
        return manager.getNullEvaluator(type);
    }

    public static @NonNull Evaluator<List<String>> createStringListEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createListEvaluator(manager, String.class, input, StringConverter.INSTANCE);
    }

    public static @NonNull Evaluator<List<Integer>> createIntegerListEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createListEvaluator(manager, Integer.class, input, IntegerConverter.INSTANCE);
    }

    public static @NonNull Evaluator<List<Long>> createLongListEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createListEvaluator(manager, Long.class, input, LongConverter.INSTANCE);
    }

    public static @NonNull Evaluator<List<Double>> createDoubleListEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createListEvaluator(manager, Double.class, input, DoubleConverter.INSTANCE);
    }

    public static @NonNull Evaluator<List<Boolean>> createBooleanListEvaluator(@NonNull BaseActionManager manager, @Nullable Object input) {
        return createListEvaluator(manager, Boolean.class, input, BooleanConverter.INSTANCE);
    }

    public static <T> @NonNull Evaluator<List<T>> createListEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> elementType,
        @Nullable Object input
    ) {
        return createListEvaluator(
            manager,
            elementType,
            input,
            new EvaluatorConverter<>(elementType)
        );
    }

    public static <T> @NonNull Evaluator<List<T>> createListEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> elementType,
        @Nullable Object input,
        @NonNull EvaluatorConverter<T> converter
    ) {
        return createListEvaluator(
            manager,
            input,
            (element) -> createEvaluator(manager, elementType, element, converter),
            (element) -> createListEvaluator(manager, elementType, element, converter)
        );
    }

    @SuppressWarnings("unchecked")
    private static <E> @NonNull Evaluator<List<E>> createListEvaluator(
        @NonNull BaseActionManager manager,
        @Nullable Object input,
        @NonNull EvaluatorParser<E> scalarFactory,
        @NonNull EvaluatorParser<List<E>> listFactory
    ) {
        val listType = Evaluator.<E>listType();
        if (input == null) return new Evaluator<>(manager, listType);
        if (input instanceof Evaluator) {
            val evaluator = (Evaluator<?>) input;
            if (List.class.equals(evaluator.getType())) return (Evaluator<List<E>>) input;
            return new ListWrappedEvaluator<>(manager, listType, (Evaluator<E>) input);
        }
        if (input instanceof List<?>) return new ListValueEvaluator<>(manager, listType, (List<?>) input, listFactory);
        if (input instanceof String) return new ListWrappedEvaluator<>(manager, listType, scalarFactory.parse(input));
        val config = ConfigReader.parse(input);
        if (config == null) return new ListWrappedEvaluator<>(manager, listType, scalarFactory.parse(input));
        val rawType = config.get("type");
        val hasType = config.containsKey("type");
        if (hasType && (rawType == null || "null".equalsIgnoreCase(rawType.toString()))) {
            return new NullListElementEvaluator<>(manager, listType);
        }
        val type = rawType == null ? "" : rawType.toString().toLowerCase(Locale.ROOT);
        switch (type) {
            case "condition": {
                return new ConditionListEvaluator<>(manager, listType, config, listFactory);
            }
            case "condition-weight": {
                return new ConditionWeightListEvaluator<>(manager, listType, config, listFactory);
            }
            case "contains": {
                return new ContainsListEvaluator<>(manager, listType, config, listFactory);
            }
            case "double-tree": {
                return new DoubleTreeListEvaluator<>(manager, listType, config, listFactory);
            }
            case "int-tree": {
                return new IntTreeListEvaluator<>(manager, listType, config, listFactory);
            }
            case "key": {
                return new KeyListEvaluator<>(manager, listType, config, listFactory);
            }
            case "weight": {
                return new WeightListEvaluator<>(manager, listType, config, listFactory);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionListEvaluator<>(manager, listType, config, listFactory);
        }
        return new ListWrappedEvaluator<>(manager, listType, scalarFactory.parse(input));
    }

    @SuppressWarnings("unchecked")
    private static <E> @NonNull Class<List<E>> listType() {
        return (Class<List<E>>) (Class<?>) List.class;
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
