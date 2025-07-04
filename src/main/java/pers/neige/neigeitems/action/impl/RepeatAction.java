package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.concurrent.CompletableFuture;

public class RepeatAction extends Action {
    private final @NonNull String globalId;
    private final @NonNull Evaluator<Integer> repeat;
    private final @NonNull Action actions;

    public RepeatAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader config
    ) {
        super(manager);
        this.globalId = config.getString("global-id", "i");
        this.repeat = Evaluator.createIntegerEvaluator(manager, config.getString("repeat"));
        this.actions = manager.compile(config.get("actions"));
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(actions::canRunInOtherThread);
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.REPEAT;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    protected @NonNull CompletableFuture<ActionResult> eval(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    public @NonNull String getGlobalId() {
        return globalId;
    }

    public @NonNull Evaluator<Integer> getRepeat() {
        return repeat;
    }

    public @NonNull Action getActions() {
        return actions;
    }
}
