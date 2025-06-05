package pers.neige.neigeitems.action.result;

import lombok.NonNull;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;

public class SuccessResult extends ActionResult {
    @Override
    public @NonNull ResultType getType() {
        return ResultType.SUCCESS;
    }
}
