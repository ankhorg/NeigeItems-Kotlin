package pers.neige.neigeitems.user;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.catcher.ChatCatcher;
import pers.neige.neigeitems.action.catcher.SignCatcher;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final @NonNull UUID uuid;
    private final @NonNull ArrayDeque<ChatCatcher> chatCatchers = new ArrayDeque<>();
    private final @NonNull ArrayDeque<SignCatcher> signCatchers = new ArrayDeque<>();
    private final @NonNull Map<String, Object> metadata = new ConcurrentHashMap<>();
    private final @NonNull Map<String, Long> cooldown = new ConcurrentHashMap<>();

    public User(
            @NonNull UUID uuid
    ) {
        this.uuid = uuid;
    }

    public @NonNull UUID getUUID() {
        return uuid;
    }

    public void addChatCatcher(@NonNull ChatCatcher catcher) {
        synchronized (chatCatchers) {
            chatCatchers.add(catcher);
        }
    }

    public @Nullable ChatCatcher pollChatCatcher() {
        synchronized (chatCatchers) {
            return chatCatchers.poll();
        }
    }

    public void clearChatCatcher() {
        synchronized (chatCatchers) {
            ChatCatcher catcher;
            while ((catcher = chatCatchers.poll()) != null) {
                catcher.future.complete(null);
            }
        }
    }

    public void addSignCatcher(@NonNull SignCatcher catcher) {
        synchronized (signCatchers) {
            signCatchers.add(catcher);
        }
    }

    public @Nullable SignCatcher pollSignCatcher() {
        synchronized (signCatchers) {
            return signCatchers.poll();
        }
    }

    public void clearSignCatcher() {
        synchronized (signCatchers) {
            SignCatcher catcher;
            while ((catcher = signCatchers.poll()) != null) {
                catcher.future.complete(null);
            }
        }
    }

    public @NonNull Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * 检测冷却状态.<br>
     * 冷却完成则重新设置冷却并返回0.<br>
     * 冷却未完成则返回剩余时间.<br>
     *
     * @param key      冷却组ID
     * @param cooldown 冷却刷新时间
     * @return 剩余冷却时间
     */
    public long checkCooldown(@NonNull String key, long cooldown) {
        if (cooldown <= 0) return 0;
        val time = System.currentTimeMillis();
        val lastTime = this.cooldown.getOrDefault(key, 0L);
        if (lastTime > time) {
            return lastTime - time;
        } else {
            this.cooldown.put(key, time + cooldown);
            return 0;
        }
    }

    /**
     * 返回剩余冷却时间.
     *
     * @param key 冷却组ID
     * @return 剩余冷却时间
     */
    public long getCooldown(@NonNull String key) {
        val time = System.currentTimeMillis();
        val lastTime = this.cooldown.getOrDefault(key, 0L);
        if (lastTime > time) {
            return lastTime - time;
        } else {
            return 0;
        }
    }

    /**
     * 设置进入冷却状态.
     *
     * @param key      冷却组ID
     * @param cooldown 冷却刷新时间
     */
    public void setCooldown(@NonNull String key, long cooldown) {
        val time = System.currentTimeMillis();
        this.cooldown.put(key, time + cooldown);
    }
}
