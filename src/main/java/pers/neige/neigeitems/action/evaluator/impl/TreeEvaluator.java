package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.impl.TreeAction;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@Getter
@ToString(callSuper = true)
public abstract class TreeEvaluator<K extends Comparable<?>, T> extends Evaluator<T> {
    protected final @NonNull Class<K> keyType;
    private final @NonNull TreeAction.TreeActionType actionType;
    private final @NonNull String globalId;
    private final @NonNull Evaluator<T> defaultEvaluator;
    private final @NonNull TreeMap<K, Evaluator<T>> evaluators = new TreeMap<>();

    public TreeEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config,
        @NonNull Class<K> keyType
    ) {
        super(manager, type);
        this.keyType = keyType;
        val rawActionType = config.get("action-type");
        TreeAction.TreeActionType actionType;
        try {
            if (rawActionType instanceof String) {
                actionType = TreeAction.TreeActionType.valueOf(((String) rawActionType).toUpperCase(Locale.ROOT));
            } else if (rawActionType instanceof Integer) {
                actionType = TreeAction.TreeActionType.values()[(int) rawActionType];
            } else {
                actionType = TreeAction.TreeActionType.LOWER;
            }
        } catch (Throwable throwable) {
            manager.getPlugin().getLogger().warning(rawActionType + " is not a valid tree action type.");
            actionType = TreeAction.TreeActionType.LOWER;
        }
        this.actionType = actionType;
        this.globalId = config.getString("global-id", "key");
        this.defaultEvaluator = parseEvaluator(config.get("default-evaluator"));
        val evaluatorsConfig = config.getConfig("evaluators");
        if (evaluatorsConfig != null) {
            for (val stringKey : evaluatorsConfig.keySet()) {
                val key = cast(stringKey);
                if (key == null) {
                    manager.getPlugin().getLogger().warning(stringKey + " is not a valid tree action key.");
                    continue;
                }
                this.evaluators.put(key, parseEvaluator(evaluatorsConfig.get(stringKey)));
            }
        }
    }

    public @Nullable K cast(@NonNull Object result) {
        return keyType.isInstance(result) ? keyType.cast(result) : null;
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    public abstract @NonNull Evaluator<K> getValue();

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        val value = getValue().get(context);
        context.getGlobal().put(getGlobalId(), value);
        if (value == null) {
            return defaultEvaluator.getOrDefault(context, def);
        }
        Map.Entry<K, Evaluator<T>> entry = null;
        switch (getActionType()) {
            case LOWER:
                entry = evaluators.lowerEntry(value);
                break;
            case FLOOR:
                entry = evaluators.floorEntry(value);
                break;
            case HIGHER:
                entry = evaluators.higherEntry(value);
                break;
            case CEILING:
                entry = evaluators.ceilingEntry(value);
                break;
        }
        if (entry == null) {
            return defaultEvaluator.getOrDefault(context, def);
        }
        return entry.getValue().getOrDefault(context, def);
    }
}
