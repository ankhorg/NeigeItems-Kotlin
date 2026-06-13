package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import pers.neige.neigeitems.calculate.FormulaParser;
import pers.neige.neigeitems.manager.BaseActionManager;

public class FastCalcParser extends CalcParser {
    public FastCalcParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "fastcalc";
    }

    @Override
    protected double calc(@NonNull String formula) {
        return FormulaParser.calculate(formula);
    }
}
