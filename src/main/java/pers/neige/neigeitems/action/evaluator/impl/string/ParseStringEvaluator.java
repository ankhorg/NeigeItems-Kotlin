package pers.neige.neigeitems.action.evaluator.impl.string;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.Map;

public class ParseStringEvaluator extends RawStringEvaluator {
    public ParseStringEvaluator(@NotNull BaseActionManager manager, @NotNull String value) {
        super(manager, value);
    }

    @Override
    @Contract("_, !null -> !null")
    @SuppressWarnings("unchecked")
    public @Nullable String getOrDefault(@NotNull ActionContext context, @Nullable String def) {
        return value == null ? def : SectionUtils.parseSection(
                value,
                (Map<String, String>) (Object) context.getGlobal(),
                context.getPlayer(),
                manager.getSectionConfig(context)
        );
    }
}
