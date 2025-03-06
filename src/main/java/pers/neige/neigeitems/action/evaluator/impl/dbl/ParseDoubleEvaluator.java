package pers.neige.neigeitems.action.evaluator.impl.dbl;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.Map;

public class ParseDoubleEvaluator extends Evaluator<Double> {
    protected final @Nullable String value;

    public ParseDoubleEvaluator(@NotNull BaseActionManager manager, @Nullable String value) {
        super(manager);
        this.value = value;
    }

    public @Nullable String getValue() {
        return value;
    }

    @Override
    @Contract("_, !null -> !null")
    @SuppressWarnings("unchecked")
    public @Nullable Double getOrDefault(@NotNull ActionContext context, @Nullable Double def) {
        if (value == null) return def;
        Double result = StringsKt.toDoubleOrNull(
                SectionUtils.parseSection(
                        value,
                        (Map<String, String>) (Object) context.getGlobal(),
                        context.getPlayer(),
                        manager.getSectionConfig(context)
                )
        );
        return result == null ? def : result;
    }
}
