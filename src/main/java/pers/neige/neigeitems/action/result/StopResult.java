package pers.neige.neigeitems.action.result;

import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;

public class StopResult extends ActionResult {
    @Override
    public ResultType getType() {
        return ResultType.STOP;
    }
}
