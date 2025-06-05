package pers.neige.neigeitems.action.impl;

import kotlin.Pair;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConditionWeightAction extends Action {
    private final boolean order;
    private final @NonNull List<Pair<Pair<Condition, Action>, Evaluator<Double>>> actions = new ArrayList<>();
    private final @Nullable String amountScriptString;
    private @Nullable CompiledScript amountScript = null;
    private Integer amount = null;

    public ConditionWeightAction(@NonNull BaseActionManager manager, @NonNull ConfigReader action) {
        super(manager);
        this.amountScriptString = action.getString("amount");
        if (this.amountScriptString != null) {
            this.amount = NumberParser.parseInteger(this.amountScriptString);
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
        for (val action : actions) {
            if (action.getFirst().getSecond().isAsyncSafe()) return;
        }
        this.asyncSafe = false;
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

    public @Nullable String getAmountScriptString() {
        return amountScriptString;
    }

    public @Nullable CompiledScript getAmountScript() {
        return amountScript;
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

    public int getAmount(@NonNull BaseActionManager manager, @NonNull ActionContext context) {
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
                val lines = this.amountScriptString.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    val conditionLine = lines[i];
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
    protected @NonNull CompletableFuture<ActionResult> eval(@NonNull BaseActionManager manager, @NonNull ActionContext context) {
        return manager.runAction(this, context);
    }
}
