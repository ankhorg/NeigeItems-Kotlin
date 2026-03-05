package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString(callSuper = true)
public abstract class KeyEvaluator<T> extends Evaluator<T> {
    private final @NonNull String globalId;
    private final @NonNull Evaluator<String> key;
    private final @NonNull Evaluator<T> defaultEvaluator;
    private final @NonNull Map<String, Evaluator<T>> evaluators = new HashMap<>();

    public KeyEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type);
        this.globalId = config.getString("global-id", "key");
        this.key = Evaluator.createStringEvaluator(manager, config.get("key"));
        this.defaultEvaluator = parseEvaluator(config.get("default-evaluator"));
        val evaluatorsConfig = config.getConfig("evaluators");
        if (evaluatorsConfig != null) {
            for (val key : evaluatorsConfig.keySet()) {
                this.evaluators.put(key, parseEvaluator(evaluatorsConfig.get(key)));
            }
        }
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        val key = this.key.get(context);
        context.getGlobal().put(globalId, key);
        if (key == null) {
            return defaultEvaluator.getOrDefault(context, def);
        }
        val evaluator = evaluators.getOrDefault(key, defaultEvaluator);
        return evaluator.getOrDefault(context, def);
    }
}
