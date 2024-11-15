package pers.neige.neigeitems.action.result;

import pers.neige.neigeitems.action.ActionResult;

public class Results {
    public static final ActionResult SUCCESS = new SuccessResult();
    public static final ActionResult STOP = new StopResult();

    public static ActionResult fromBoolean(boolean result) {
        return result ? Results.SUCCESS : Results.STOP;
    }
}
