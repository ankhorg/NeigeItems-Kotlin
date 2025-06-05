package pers.neige.neigeitems.action.evaluator;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.impl.dbl.JsDoubleEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.dbl.ParseDoubleEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.dbl.RawDoubleEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.integer.JsIntegerEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.integer.ParseIntegerEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.integer.RawIntegerEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.string.JsStringEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.string.ParseStringEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.string.RawStringEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

import java.util.Locale;

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
                return new ParseStringEvaluator(manager, input);
        }
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
                val maybe = NumberParser.parseInteger(input);
                if (maybe == null) {
                    return new ParseIntegerEvaluator(manager, input);
                } else {
                    return new RawIntegerEvaluator(manager, maybe);
                }
        }
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
                val maybe = NumberParser.parseDouble(input);
                if (maybe == null) {
                    return new ParseDoubleEvaluator(manager, input);
                } else {
                    return new RawDoubleEvaluator(manager, maybe);
                }
        }
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
