package pers.neige.neigeitems.action.impl;

import kotlin.Pair;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConditionWeightAction extends Action {
    private final boolean order;
    private final @NonNull List<Pair<Pair<Condition, Action>, Evaluator<Double>>> actions = new ArrayList<>();
    private final @NonNull Evaluator<Integer> amount;

    public ConditionWeightAction(@NonNull BaseActionManager manager, @NonNull ConfigReader action) {
        super(manager);
        this.amount = Evaluator.createIntegerEvaluator(manager, action.getString("amount"));
        this.order = action.getBoolean("order", false);
        initActions(manager, action.get("actions"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(() -> {
            for (val action : actions) {
                if (action.getFirst().getSecond().canRunInOtherThread()) return true;
            }
            return false;
        });
    }

    public void initActions(@NonNull BaseActionManager manager, @Nullable Object actions) {
        if (!(actions instanceof List<?>)) return;
        val list = (List<?>) actions;
        for (val rawAction : list) {
            val config = ConfigReader.parse(rawAction);
            if (config == null) continue;
            val rawWeight = config.getString("weight", "1");
            val condition = new Condition(manager, config.getString("condition"));
            val action = manager.compile(config.get("actions"));
            this.actions.add(new Pair<>(new Pair<>(condition, action), Evaluator.createDoubleEvaluator(manager, rawWeight)));
        }
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.CONDITION_WEIGHT;
    }

    public boolean isOrder() {
        return order;
    }

    public @NonNull List<Pair<Pair<Condition, Action>, Evaluator<Double>>> getActions() {
        return actions;
    }

    public @NonNull List<Pair<Action, Double>> getActions(@NonNull ActionContext context) {
        val result = new ArrayList<Pair<Action, Double>>();
        for (val action : this.actions) {
            val info = action.getFirst();
            if (info.getFirst().easyCheck(context)) {
                val weight = action.getSecond().getOrDefault(context, 0D);
                if (weight <= 0) continue;
                result.add(new Pair<>(info.getSecond(), weight));
            }
        }
        return result;
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
