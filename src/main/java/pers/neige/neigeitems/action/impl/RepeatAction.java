package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class RepeatAction extends Action {
    private final @NotNull String globalId;
    private final @NotNull Evaluator<Integer> repeat;
    private final @NotNull Action actions;

    public RepeatAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader config
    ) {
        super(manager);
        this.globalId = config.getString("global-id", "i");
        this.repeat = Evaluator.createIntegerEvaluator(manager, config.getString("repeat"));
        this.actions = manager.compile(config.get("actions"));
        this.asyncSafe = actions.isAsyncSafe();
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.REPEAT;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    protected CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    public @NotNull String getGlobalId() {
        return globalId;
    }

    public @NotNull Evaluator<Integer> getRepeat() {
        return repeat;
    }

    public @NotNull Action getActions() {
        return actions;
    }
}
