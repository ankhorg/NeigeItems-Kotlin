package pers.neige.neigeitems.action.container;

import lombok.NonNull;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ActionContainer {
    public final @NonNull Map<String, ActionTrigger> triggers = new HashMap<>();
    private final @NonNull String id;

    public ActionContainer(
        @NonNull BaseActionManager actionManager,
        @NonNull String id,
        @Nullable ConfigurationSection config
    ) {
        this.id = id;
        if (config != null) {
            config.getKeys(false).forEach((trigger) -> {
                val it = config.getConfigurationSection(trigger);
                if (it != null) {
                    triggers.put(trigger.toLowerCase(), new ActionTrigger(actionManager, trigger, it));
                }
            });
        }
    }

    public @NonNull String getId() {
        return id;
    }

    public CompletableFuture<ActionResult> run(
        @Nullable String triggerId,
        @NonNull ActionContext context
    ) {
        if (triggerId != null) {
            val trigger = triggers.get(triggerId.toLowerCase());
            if (trigger != null) {
                return trigger.run(context);
            }
        }
        return CompletableFuture.completedFuture(Results.SUCCESS);
    }
}