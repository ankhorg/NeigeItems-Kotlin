package pers.neige.neigeitems.action.evaluator.impl.converter;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;

public class StringConverter extends EvaluatorConverter<String> {
    public static final @NonNull StringConverter INSTANCE = new StringConverter();

    private StringConverter() {
        super(String.class);
    }

    @Override
    public @Nullable String convert(@NonNull String input) {
        return input;
    }

    @Override
    public @Nullable String parseStaticValue(@NonNull String input) {
        return input.contains("<") && input.contains(">") ? null : super.parseStaticValue(input);
    }

    @Override
    public @Nullable String convert(@NonNull Object input) {
        return input.toString();
    }
}
