package pers.neige.neigeitems.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.impl.ChatCatcherAction;

import java.util.ArrayDeque;
import java.util.UUID;

public class User {
    @NotNull
    private final UUID uuid;
    @NotNull
    private final ArrayDeque<ChatCatcherAction.Catcher> chatCatchers = new ArrayDeque<>();

    public User(
            @NotNull UUID uuid
    ) {
        this.uuid = uuid;
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @NotNull
    public ArrayDeque<ChatCatcherAction.Catcher> getChatCatchers() {
        return chatCatchers;
    }

    public void addChatCatcher(ChatCatcherAction.Catcher catcher) {
        synchronized (this) {
            chatCatchers.add(catcher);
        }
    }

    @Nullable
    public ChatCatcherAction.Catcher pollChatCatcher() {
        synchronized (this) {
            return chatCatchers.poll();
        }
    }
}
