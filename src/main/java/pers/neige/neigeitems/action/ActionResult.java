package pers.neige.neigeitems.action;

import lombok.NonNull;

public abstract class ActionResult implements Comparable<ActionResult> {
    public abstract @NonNull ResultType getType();

    @Override
    public int compareTo(ActionResult o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    public int getPriority() {
        return 0;
    }

    public boolean isStop() {
        return getType() == ResultType.STOP;
    }
}
