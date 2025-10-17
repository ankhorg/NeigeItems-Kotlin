package pers.neige.neigeitems.utils.cooldown;

import lombok.NonNull;
import lombok.val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    private final @NonNull Map<String, Long> cooldown = new ConcurrentHashMap<>();

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
