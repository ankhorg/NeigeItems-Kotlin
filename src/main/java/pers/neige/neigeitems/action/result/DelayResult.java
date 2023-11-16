package pers.neige.neigeitems.action.result;

import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;

public class DelayResult extends ActionResult {
    private final int delay;

    public DelayResult(int delay) {
        this.delay = delay;
    }

    @Override
    public ResultType getType() {
        return ResultType.DELAY;
    }

    public int getDelay() {
        return delay;
    }
}
