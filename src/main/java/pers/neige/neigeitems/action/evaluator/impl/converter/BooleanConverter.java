package pers.neige.neigeitems.action.evaluator.impl.converter;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;

public class BooleanConverter extends EvaluatorConverter<Boolean> {
    public static final @NonNull BooleanConverter INSTANCE = new BooleanConverter();

    private BooleanConverter() {
        super(Boolean.class);
    }

    @Override
    public @Nullable Boolean convert(@NonNull String input) {
        if (input.equalsIgnoreCase("true")) {
            return true;
        } else if (input.equalsIgnoreCase("false")) {
            return false;
        }
        return null;
    }
}
