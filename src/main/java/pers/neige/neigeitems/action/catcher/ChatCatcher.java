package pers.neige.neigeitems.action.catcher;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public class ChatCatcher {
    @NotNull
    public CompletableFuture<String> future = new CompletableFuture<>();
    public boolean cancel;

    public ChatCatcher(
            @NotNull BaseActionManager actionManager,
            @NotNull String messageKey,
            boolean cancel,
            @NotNull ActionContext context,
            @NotNull CompletableFuture<ActionResult> result
    ) {
        this.cancel = cancel;
        final boolean isPrimaryThread = Bukkit.isPrimaryThread();
        future.thenAccept((message) -> {
            context.getGlobal().put(messageKey, message);
            SchedulerUtils.run(actionManager.getPlugin(), isPrimaryThread, () -> result.complete(Results.SUCCESS));
        });
    }

    @NotNull
    public CompletableFuture<String> getFuture() {
        return future;
    }

    public boolean isCancel() {
        return cancel;
    }
}
