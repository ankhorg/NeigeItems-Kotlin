package pers.neige.neigeitems.action.result;

import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;

public class StopResult extends ActionResult {
    @Nullable
    private final String label;
    private int priority = 1;

    public StopResult() {
        this.label = null;
    }

    public StopResult(int priority) {
        this.label = null;
        this.priority = priority;
    }

    public StopResult(@Nullable String label) {
        this.label = label;
    }

    public StopResult(@Nullable String label, int priority) {
        this.label = label;
        this.priority = priority;
    }

    @Override
    public ResultType getType() {
        return ResultType.STOP;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Nullable
    public String getLabel() {
        return label;
    }
}
