package pers.neige.neigeitems.action.catcher;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.annotation.Awake;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.user.User;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public class SignCatcher {
    @NotNull
    public CompletableFuture<String[]> future = new CompletableFuture<>();

    public SignCatcher(
            @NotNull BaseActionManager actionManager,
            @NotNull String messageKey,
            @NotNull ActionContext context,
            @NotNull CompletableFuture<ActionResult> result
    ) {
        final boolean isPrimaryThread = Bukkit.isPrimaryThread();
        future.thenAccept((texts) -> {
            context.getGlobal().put(messageKey, texts);
            for (int index = 0; index < texts.length; index++) {
                String text = texts[index];
                context.getGlobal().put(messageKey + "." + index, text);
            }
            SchedulerUtils.run(actionManager.getPlugin(), isPrimaryThread, () -> result.complete(Results.SUCCESS));
        });
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
                            SignCatcher catcher = user.pollSignCatcher();
                            if (catcher == null) return;
                            PacketContainer packet = event.getPacket();
                            String[] texts = packet.getStringArrays().read(0);
                            catcher.future.complete(texts);
                        }
                    });
        } catch (Throwable ignored) {
        }
    }

    @NotNull
    public CompletableFuture<String[]> getFuture() {
        return future;
    }
}
