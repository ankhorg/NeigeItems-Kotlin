package pers.neige.neigeitems.action.impl;

import kotlin.Pair;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class WeightAction extends Action {
    private final boolean order;
    private final @NonNull List<Pair<Action, Double>> actions = new ArrayList<>();
    private final @NonNull Evaluator<Integer> amount;
    private double totalWeight = 0;
    private boolean equalWeight = false;

    public WeightAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader action
    ) {
        super(manager);
        this.amount = Evaluator.createIntegerEvaluator(manager, action.getString("amount"));
        this.order = action.getBoolean("order", false);
        initActions(manager, action.get("actions"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(() -> {
            for (val pair : actions) {
                if (pair.getFirst().canRunInOtherThread()) return true;
            }
            return false;
        });
    }

    public void initActions(
            @NonNull BaseActionManager manager,
            @Nullable Object actions
    ) {
        if (!(actions instanceof List<?>)) return;
        val list = (List<?>) actions;
        double total = 0;
        for (Object rawAction : list) {
            val config = ConfigReader.parse(rawAction);
            if (config == null) continue;
            val weight = config.getDouble("weight", 1);
            if (weight <= 0) continue;
            val action = manager.compile(config.get("actions"));
            this.actions.add(new Pair<>(action, weight));
            total += weight;
        }
        this.totalWeight = total;
        if (!this.actions.isEmpty()) {
            this.equalWeight = this.actions.stream().allMatch((current) -> Objects.equals(current.getSecond(), this.actions.get(0).getSecond()));
        } else {
            this.equalWeight = true;
        }
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.WEIGHT;
    }

    public boolean isOrder() {
        return order;
    }

    public @NonNull List<Pair<Action, Double>> getActions() {
        return actions;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public boolean isEqualWeight() {
        return equalWeight;
    }

    public int getAmount(@NonNull ActionContext context) {
        return this.amount.getOrDefault(context, 1);
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    protected @NonNull CompletableFuture<ActionResult> eval(@NonNull BaseActionManager manager, @NonNull ActionContext context) {
        return manager.runAction(this, context);
    }
}
