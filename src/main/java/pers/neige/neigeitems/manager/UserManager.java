package pers.neige.neigeitems.manager;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.user.User;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager extends ConcurrentHashMap<UUID, User> {
    public static UserManager INSTANCE = new UserManager();

    private UserManager() {
    }

    @NotNull
    public User create(UUID uuid) {
        User user = new User(uuid);
        put(uuid, user);
        return user;
    }
}
