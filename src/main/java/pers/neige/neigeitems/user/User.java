package pers.neige.neigeitems.user;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.catcher.ChatCatcher;
import pers.neige.neigeitems.action.catcher.SignCatcher;
import pers.neige.neigeitems.utils.cooldown.CooldownManager;
import pers.neige.neigeitems.utils.cooldown.StackableCooldownManager;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final @NonNull UUID uuid;
    private final @NonNull ArrayDeque<ChatCatcher> chatCatchers = new ArrayDeque<>();
    private final @NonNull ArrayDeque<SignCatcher> signCatchers = new ArrayDeque<>();
    private final @NonNull Map<String, Object> metadata = new ConcurrentHashMap<>();
    @Getter
    private final @NonNull CooldownManager cooldownManager = new CooldownManager();
    @Getter
    private final @NonNull StackableCooldownManager stackableCooldownManager = new StackableCooldownManager();

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
        return cooldownManager.checkCooldown(key, cooldown);
    }

    /**
     * 返回剩余冷却时间.
     *
     * @param key 冷却组ID
     * @return 剩余冷却时间
     */
    public long getCooldown(@NonNull String key) {
        return cooldownManager.getCooldown(key);
    }

    /**
     * 设置进入冷却状态.
     *
     * @param key      冷却组ID
     * @param cooldown 冷却刷新时间
     */
    public void setCooldown(@NonNull String key, long cooldown) {
        cooldownManager.setCooldown(key, cooldown);
    }
}
