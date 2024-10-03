package pers.neige.neigeitems.action.catcher;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;

import java.util.concurrent.CompletableFuture;

public class ChatCatcher {
    @NotNull
    public CompletableFuture<String> future = new CompletableFuture<>();
    public boolean cancel;

    public ChatCatcher(@NotNull String messageKey, boolean cancel, @NotNull ActionContext context, @NotNull CompletableFuture<ActionResult> result) {
        this.cancel = cancel;
        future.thenAccept((message) -> {
            context.getGlobal().put(messageKey, message);
            result.complete(Results.SUCCESS);
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
