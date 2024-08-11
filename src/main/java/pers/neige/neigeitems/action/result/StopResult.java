package pers.neige.neigeitems.action.result;

import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;

public class StopResult extends ActionResult {
    @Nullable
    private final String label;

    public StopResult() {
        this.label = null;
    }

    public StopResult(@Nullable String label) {
        this.label = label;
    }

    @Override
    public ResultType getType() {
        return ResultType.STOP;
    }

    @Nullable
    public String getLabel() {
        return label;
    }
}
