package pers.neige.neigeitems.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.user.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    @NotNull
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    @NotNull
    public Map<UUID, User> getUsers() {
        return users;
    }

    @NotNull
    public User getOrMake(UUID uuid) {
        return users.computeIfAbsent(uuid, (key) -> new User(uuid));
    }

    @Nullable
    public User getIfLoaded(UUID uuid) {
        return users.get(uuid);
    }

    public boolean contains(UUID uuid) {
        return users.containsKey(uuid);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
    }
}
