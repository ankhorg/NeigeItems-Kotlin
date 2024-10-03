package pers.neige.neigeitems.action.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.annotation.Awake;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.user.User;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SignCatcherAction extends Action {
    @NotNull
    private final String messageKey;

    public SignCatcherAction(
            @NotNull ConfigurationSection action
    ) {
        messageKey = action.getString("catch-sign", "catch-sign");
    }

    public SignCatcherAction(
            @NotNull Map<?, ?> action
    ) {
        if (action.containsKey("catch-sign")) {
            messageKey = action.get("catch-sign").toString();
        } else {
            messageKey = "catch-sign";
        }
        Object value = action.get("cancel");
    }

    @Awake
    public static void registerListener() {
        try {
            ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(
                            NeigeItems.getInstance(),
                            ListenerPriority.NORMAL,
                            PacketType.Play.Client.UPDATE_SIGN
                    ) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            Player player = event.getPlayer();
                            User user = NeigeItems.getUserManager().getIfLoaded(player.getUniqueId());
                            if (user == null) return;
                            SignCatcherAction.Catcher catcher = user.pollSignCatcher();
                            if (catcher == null) return;
                            PacketContainer packet = event.getPacket();
                            String[] texts = packet.getStringArrays().read(0);
                            catcher.future.complete(texts);
                        }
                    });
        } catch (Throwable ignored) {
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

    @NotNull
    public Catcher getCatcher(@NotNull ActionContext context, @NotNull CompletableFuture<ActionResult> result) {
        return new Catcher(this, context, result);
    }

    public static class Catcher {
        @NotNull
        public CompletableFuture<String[]> future = new CompletableFuture<>();

        private Catcher(@NotNull SignCatcherAction action, @NotNull ActionContext context, @NotNull CompletableFuture<ActionResult> result) {
            future.thenAccept((texts) -> {
                context.getGlobal().put(action.getMessageKey(), StringUtils.joinToString(texts, "\n", 0));
                context.getGlobal().put(action.getMessageKey() + ".array", texts);
                context.getGlobal().put(action.getMessageKey() + ".length", texts.length);
                for (int index = 0; index < texts.length; index++) {
                    String text = texts[index];
                    context.getGlobal().put(action.getMessageKey() + "." + index, text);
                }
                result.complete(Results.SUCCESS);
            });
        }

        @NotNull
        public CompletableFuture<String[]> getFuture() {
            return future;
        }
    }
}
