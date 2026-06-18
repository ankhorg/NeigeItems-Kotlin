package pers.neige.neigeitems.action.evaluator.impl.converter;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;
import pers.neige.neigeitems.utils.NumberParser;

public class DoubleConverter extends EvaluatorConverter<Double> {
    public static final @NonNull DoubleConverter INSTANCE = new DoubleConverter();

    private DoubleConverter() {
        super(Double.class);
    }

    @Override
    public @Nullable Double convert(@NonNull String input) {
        return NumberParser.parseDouble(input);
    }

    @Override
    public @Nullable Double convert(@NonNull Object input) {
        if (input instanceof Number) {
            return ((Number) input).doubleValue();
        }
        return null;
    }
}
