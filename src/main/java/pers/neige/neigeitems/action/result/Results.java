package pers.neige.neigeitems.action.result;

import lombok.NonNull;
import pers.neige.neigeitems.action.ActionResult;

public class Results {
    public static final @NonNull ActionResult SUCCESS = new SuccessResult();
    public static final @NonNull ActionResult STOP = new StopResult();

    public static @NonNull ActionResult fromBoolean(boolean result) {
        return result ? Results.SUCCESS : Results.STOP;
    }
}
