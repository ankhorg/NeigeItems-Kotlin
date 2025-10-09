package pers.neige.neigeitems.manager;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.user.User;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager extends ConcurrentHashMap<UUID, User> {
    public static final @NonNull UserManager INSTANCE = new UserManager();

    private UserManager() {
    }

    public void createUser(UUID uuid) {
        if (ConfigManager.INSTANCE.getResetCooldownWhenPlayerQuit()) {
            put(uuid, new User(uuid));
        } else {
            computeIfAbsent(uuid, User::new);
        }
    }

    public void removeUser(UUID uuid) {
        if (ConfigManager.INSTANCE.getResetCooldownWhenPlayerQuit()) {
            remove(uuid);
        }
    }
}
