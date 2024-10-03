package pers.neige.neigeitems.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.catcher.ChatCatcher;
import pers.neige.neigeitems.action.catcher.SignCatcher;

import java.util.ArrayDeque;
import java.util.UUID;

public class User {
    @NotNull
    private final UUID uuid;
    @NotNull
    private final ArrayDeque<ChatCatcher> chatCatchers = new ArrayDeque<>();
    @NotNull
    private final ArrayDeque<SignCatcher> signCatchers = new ArrayDeque<>();

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
    public ArrayDeque<ChatCatcher> getChatCatchers() {
        return chatCatchers;
    }

    public void addChatCatcher(ChatCatcher catcher) {
        synchronized (this) {
            chatCatchers.add(catcher);
        }
    }

    @Nullable
    public ChatCatcher pollChatCatcher() {
        synchronized (this) {
            return chatCatchers.poll();
        }
    }

    @NotNull
    public ArrayDeque<SignCatcher> getSignCatchers() {
        return signCatchers;
    }

    public void addSignCatcher(SignCatcher catcher) {
        synchronized (this) {
            signCatchers.add(catcher);
        }
    }

    @Nullable
    public SignCatcher pollSignCatcher() {
        synchronized (this) {
            return signCatchers.poll();
        }
    }
}
