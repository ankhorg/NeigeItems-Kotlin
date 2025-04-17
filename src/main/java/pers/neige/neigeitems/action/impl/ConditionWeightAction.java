package pers.neige.neigeitems.action.impl;

import kotlin.Pair;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.StringUtils;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConditionWeightAction extends Action {
    private final boolean order;
    @NotNull
    private final List<Pair<Pair<Condition, Action>, Evaluator<Double>>> actions = new ArrayList<>();
    @Nullable
    private String amountScriptString = null;
    @Nullable
    private CompiledScript amountScript = null;
    private Integer amount = null;

    public ConditionWeightAction(@NotNull BaseActionManager manager, @NotNull ConfigReader action) {
        super(manager);
        this.amountScriptString = action.getString("amount");
        if (this.amountScriptString != null) {
            this.amount = StringsKt.toIntOrNull(this.amountScriptString);
            if (this.amount == null) {
                try {
                    this.amountScript = ((Compilable) manager.getEngine()).compile(amountScriptString);
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        this.order = action.getBoolean("order", false);
        initActions(manager, action.get("actions"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        for (Pair<Pair<Condition, Action>, Evaluator<Double>> action : actions) {
            if (action.getFirst().getSecond().isAsyncSafe()) return;
        }
        this.asyncSafe = false;
    }

    public void initActions(@NotNull BaseActionManager manager, @Nullable Object actions) {
        if (!(actions instanceof List<?>)) return;
        List<?> list = (List<?>) actions;
        for (Object rawAction : list) {
            if (!(rawAction instanceof Map<?, ?>)) continue;
            Map<?, ?> mapAction = (Map<?, ?>) rawAction;
            Object rawWeight = mapAction.get("weight");
            if (rawWeight == null) rawWeight = "1";
            Object conditionString = mapAction.get("condition");
            Condition condition = new Condition(manager, conditionString == null ? null : conditionString.toString());
            Action action = manager.compile(mapAction.get("actions"));
            this.actions.add(new Pair<>(new Pair<>(condition, action), Evaluator.createDoubleEvaluator(manager, rawWeight.toString())));
        }
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.CONDITION_WEIGHT;
    }

    @Nullable
    public String getAmountScriptString() {
        return amountScriptString;
    }

    @Nullable
    public CompiledScript getAmountScript() {
        return amountScript;
    }

    public boolean isOrder() {
        return order;
    }

    @NotNull
    public List<Pair<Pair<Condition, Action>, Evaluator<Double>>> getActions() {
        return actions;
    }

    @NotNull
    public List<Pair<Action, Double>> getActions(@NotNull ActionContext context) {
        List<Pair<Action, Double>> result = new ArrayList<>();
        for (Pair<Pair<Condition, Action>, Evaluator<Double>> action : this.actions) {
            Pair<Condition, Action> info = action.getFirst();
            if (info.getFirst().easyCheck(context)) {
                double weight = action.getSecond().getOrDefault(context, 0D);
                if (weight <= 0) continue;
                result.add(new Pair<>(info.getSecond(), weight));
            }
        }
        return result;
    }

    public int getAmount(@NotNull BaseActionManager manager, @NotNull ActionContext context) {
        if (this.amount != null) return this.amount;
        if (this.amountScript == null) {
            return 1;
        }
        Object result;
        try {
            result = amountScript.eval(context.getBindings());
            if (result == null) {
                return 1;
            }
        } catch (Throwable error) {
            if (this.amountScriptString != null) {
                manager.getPlugin().getLogger().warning("ConditionWeight动作数量解析异常, 数量脚本内容如下:");
                String[] lines = this.amountScriptString.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String conditionLine = lines[i];
                    manager.getPlugin().getLogger().warning((i + 1) + ". " + conditionLine);
                }
            } else {
                manager.getPlugin().getLogger().warning("ConditionWeight动作数量解析异常, 数量脚本内容未知");
            }
            error.printStackTrace();
            return 1;
        }
        if (result instanceof Number) {
            return ((Number) result).intValue();
        } else {
            return StringUtils.parseInteger(result.toString(), 1);
        }
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    protected CompletableFuture<ActionResult> eval(@NotNull BaseActionManager manager, @NotNull ActionContext context) {
        return manager.runAction(this, context);
    }
}
