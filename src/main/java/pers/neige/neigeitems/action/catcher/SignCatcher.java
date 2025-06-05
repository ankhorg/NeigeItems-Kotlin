package pers.neige.neigeitems.action.catcher;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.annotation.Awake;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.manager.UserManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public class SignCatcher {
    public final @NonNull CompletableFuture<String[]> future = new CompletableFuture<>();

    public SignCatcher(
            @NonNull BaseActionManager actionManager,
            @NonNull String messageKey,
            @NonNull ActionContext context,
            @NonNull CompletableFuture<ActionResult> result
    ) {
        future.thenAccept((texts) -> {
            if (texts != null) {
                context.getGlobal().put(messageKey, texts);
                for (int index = 0; index < texts.length; index++) {
                    val text = texts[index];
                    context.getGlobal().put(messageKey + "." + index, text);
                }
            }
            SchedulerUtils.run(actionManager.getPlugin(), context.isSync(), () -> result.complete(Results.SUCCESS));
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
                            val player = event.getPlayer();
                            val user = UserManager.INSTANCE.get(player.getUniqueId());
                            if (user == null) return;
                            val catcher = user.pollSignCatcher();
                            if (catcher == null) return;
                            val packet = event.getPacket();
                            val texts = packet.getStringArrays().read(0);
                            catcher.future.complete(texts);
                        }
                    });
        } catch (Throwable ignored) {
        }
    }

    public @NonNull CompletableFuture<String[]> getFuture() {
        return future;
    }
}
