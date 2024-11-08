package pers.neige.neigeitems.action.container;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ActionContainer {
    @NotNull
    public final Map<String, ActionTrigger> triggers = new HashMap<>();
    @NotNull
    private final String id;

    public ActionContainer(
            @NotNull BaseActionManager actionManager,
            @NotNull String id,
            @Nullable ConfigurationSection config
    ) {
        this.id = id;
        if (config != null) {
            config.getKeys(false).forEach((trigger) -> {
                ConfigurationSection it = config.getConfigurationSection(trigger);
                if (it != null) {
                    triggers.put(trigger.toLowerCase(), new ActionTrigger(actionManager, trigger, it));
                }
            });
        }
    }

    @NotNull
    public String getId() {
        return id;
    }

    public CompletableFuture<ActionResult> run(
            @Nullable String triggerId,
            @NotNull ActionContext context
    ) {
        if (triggerId != null) {
            ActionTrigger trigger = triggers.get(triggerId.toLowerCase());
            if (trigger != null) {
                return trigger.run(context);
            }
        }
        return CompletableFuture.completedFuture(Results.SUCCESS);
    }
}