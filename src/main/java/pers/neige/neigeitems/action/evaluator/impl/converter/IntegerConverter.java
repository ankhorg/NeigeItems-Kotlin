package pers.neige.neigeitems.action.evaluator.impl.converter;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;
import pers.neige.neigeitems.utils.NumberParser;

public class IntegerConverter extends EvaluatorConverter<Integer> {
    public static final @NonNull IntegerConverter INSTANCE = new IntegerConverter();

    private IntegerConverter() {
        super(Integer.class);
    }

    @Override
    public @Nullable Integer convert(@NonNull String input) {
        val maybe = NumberParser.parseInteger(input);
        if (maybe != null) return maybe;
        val maybeDouble = NumberParser.parseDouble(input);
        if (maybeDouble != null) return maybeDouble.intValue();
        return null;
    }

    @Override
    public @Nullable Integer convert(@NonNull Object input) {
        if (input instanceof Number) {
            return ((Number) input).intValue();
        }
        return null;
    }
}
