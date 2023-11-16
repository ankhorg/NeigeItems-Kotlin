package pers.neige.neigeitems.action.result;

import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;

public class Results {
    public static ActionResult SUCCESS = new SuccessResult();
    public static ActionResult STOP = new StopResult();

    public static ActionResult fromBoolean(boolean result) {
        return result ? Results.SUCCESS : Results.STOP;
    }
}
