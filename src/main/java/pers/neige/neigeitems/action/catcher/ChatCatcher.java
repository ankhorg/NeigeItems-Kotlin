package pers.neige.neigeitems.action.catcher;

import lombok.NonNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public class ChatCatcher {
    public final @NonNull CompletableFuture<String> future = new CompletableFuture<>();
    public final boolean cancel;

    public ChatCatcher(
            @NonNull BaseActionManager actionManager,
            @NonNull String messageKey,
            boolean cancel,
            @NonNull ActionContext context,
            @NonNull CompletableFuture<ActionResult> result
    ) {
        this.cancel = cancel;
        future.thenAccept((message) -> {
            if (message != null) {
                context.getGlobal().put(messageKey, message);
            }
            SchedulerUtils.run(actionManager.getPlugin(), context.isSync(), () -> result.complete(Results.SUCCESS));
        });
    }

    public @NonNull CompletableFuture<String> getFuture() {
        return future;
    }

    public boolean isCancel() {
        return cancel;
    }
}
