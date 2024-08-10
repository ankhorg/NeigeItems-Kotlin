package pers.neige.neigeitems.action.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChatCatcherAction extends Action {
    @NotNull
    private final String messageKey;
    private final boolean cancel;

    public ChatCatcherAction(BaseActionManager manager, ConfigurationSection action) {
        messageKey = action.getString("catch", "catch");
        cancel = action.getBoolean("cancel", true);
    }

    public ChatCatcherAction(BaseActionManager manager, Map<?, ?> action) {
        if (action.containsKey("catch")) {
            messageKey = action.get("catch").toString();
        } else {
            messageKey = "catch";
        }
        Object value = action.get("cancel");
        if (value instanceof Boolean) {
            cancel = (boolean) value;
        } else {
            cancel = true;
        }
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.CHAT_CATCHER;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    public CompletableFuture<ActionResult> eval(@NotNull BaseActionManager manager, @NotNull ActionContext context) {
        return manager.runAction(this, context);
    }

    @NotNull
    public String getMessageKey() {
        return messageKey;
    }

    public boolean isCancel() {
        return cancel;
    }

    @NotNull
    public Catcher getCatcher(@NotNull ActionContext context, @NotNull CompletableFuture<ActionResult> result) {
        return new Catcher(this, context, result);
    }

    public static class Catcher {
        @NotNull
        public CompletableFuture<String> future = new CompletableFuture<>();
        public boolean cancel;

        private Catcher(@NotNull ChatCatcherAction action, @NotNull ActionContext context, @NotNull CompletableFuture<ActionResult> result) {
            this.cancel = action.cancel;
            future.thenAccept((message) -> {
                context.getGlobal().put(action.getMessageKey(), message);
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
}
