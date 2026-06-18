package pers.neige.neigeitems.action.evaluator;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class EvaluatorConverter<T> {
    protected final @NonNull Class<T> type;

    public @Nullable T convert(@NonNull String input) {
        return convert((Object) input);
    }

    public @Nullable T parseStaticValue(@NonNull String input) {
        return convert(input);
    }

    public @Nullable T convert(@NonNull Object input) {
        if (type.isInstance(input)) {
            return type.cast(input);
        }
        return null;
    }
}
