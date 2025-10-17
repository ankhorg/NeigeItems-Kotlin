package pers.neige.neigeitems.utils.cooldown;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StackableCooldownManager {
    private final @NonNull Map<String, StackableCooldown> cooldowns = new ConcurrentHashMap<>();

    public @NonNull StackableCooldown computeIfAbsent(@NonNull String key, @NonNull StackableCooldown.Config config) {
        return cooldowns.computeIfAbsent(key, (k) -> new StackableCooldown(config));
    }

    public @Nullable StackableCooldown get(@NonNull String key) {
        return cooldowns.get(key);
    }
}
