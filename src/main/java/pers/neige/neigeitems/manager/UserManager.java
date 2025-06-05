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

    public @NonNull User create(UUID uuid) {
        val user = new User(uuid);
        put(uuid, user);
        return user;
    }
}
