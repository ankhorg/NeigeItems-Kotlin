package pers.neige.neigeitems.action.evaluator;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface EvaluatorParser<T> {
    @NonNull
    Evaluator<T> parse(@Nullable Object input);
}
