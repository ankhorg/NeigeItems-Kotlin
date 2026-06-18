package pers.neige.neigeitems.action.evaluator.impl.converter;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;
import pers.neige.neigeitems.utils.NumberParser;

public class LongConverter extends EvaluatorConverter<Long> {
    public static final @NonNull LongConverter INSTANCE = new LongConverter();

    private LongConverter() {
        super(Long.class);
    }

    @Override
    public @Nullable Long convert(@NonNull String input) {
        val maybe = NumberParser.parseLong(input);
        if (maybe != null) return maybe;
        val maybeDouble = NumberParser.parseDouble(input);
        if (maybeDouble != null) return maybeDouble.longValue();
        return null;
    }

    @Override
    public @Nullable Long convert(@NonNull Object input) {
        if (input instanceof Number) {
            return ((Number) input).longValue();
        }
        return null;
    }
}
