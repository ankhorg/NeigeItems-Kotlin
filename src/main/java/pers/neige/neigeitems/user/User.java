package pers.neige.neigeitems.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.catcher.ChatCatcher;
import pers.neige.neigeitems.action.catcher.SignCatcher;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    @NotNull
    private final UUID uuid;
    @NotNull
    private final ArrayDeque<ChatCatcher> chatCatchers = new ArrayDeque<>();
    @NotNull
    private final ArrayDeque<SignCatcher> signCatchers = new ArrayDeque<>();
    @NotNull
    private final Map<String, Object> metadata = new ConcurrentHashMap<>();

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
        synchronized (chatCatchers) {
            chatCatchers.add(catcher);
        }
    }

    @Nullable
    public ChatCatcher pollChatCatcher() {
        synchronized (chatCatchers) {
            return chatCatchers.poll();
        }
    }

    @NotNull
    public ArrayDeque<SignCatcher> getSignCatchers() {
        return signCatchers;
    }

    public void addSignCatcher(SignCatcher catcher) {
        synchronized (signCatchers) {
            signCatchers.add(catcher);
        }
    }

    @Nullable
    public SignCatcher pollSignCatcher() {
        synchronized (signCatchers) {
            return signCatchers.poll();
        }
    }

    @NotNull
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
