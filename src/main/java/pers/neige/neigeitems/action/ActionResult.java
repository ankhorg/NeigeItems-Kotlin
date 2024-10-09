package pers.neige.neigeitems.action;

public abstract class ActionResult implements Comparable<ActionResult> {
    public abstract ResultType getType();

    @Override
    public int compareTo(ActionResult o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    public int getPriority() {
        return 0;
    }
}
