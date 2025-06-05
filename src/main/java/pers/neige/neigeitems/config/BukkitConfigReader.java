package pers.neige.neigeitems.config;

import lombok.NonNull;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BukkitConfigReader implements ConfigReader {
    private final @NonNull ConfigurationSection handle;

    public BukkitConfigReader(@NonNull ConfigurationSection config) {
        this.handle = config;
    }

    public @NonNull ConfigurationSection getHandle() {
        return handle;
    }

    @Override
    public int size() {
        return handle.getKeys(false).size();
    }

    @Override
    public @NonNull Set<String> keySet() {
        return handle.getKeys(false);
    }

    @Override
    public boolean containsKey(@NonNull String key) {
        return handle.contains(key);
    }

    @Override
    public @Nullable Object get(@NonNull String key) {
        return handle.get(key);
    }

    @Override
    public String getString(@NonNull String key) {
        return handle.getString(key);
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable String getString(@NonNull String key, @Nullable String def) {
        return handle.getString(key, def);
    }

    @Override
    public int getInt(@NonNull String key) {
        return handle.getInt(key);
    }

    @Override
    public int getInt(@NonNull String key, int def) {
        return handle.getInt(key, def);
    }

    @Override
    public long getLong(@NonNull String key) {
        return handle.getLong(key);
    }

    @Override
    public long getLong(@NonNull String key, long def) {
        return handle.getLong(key, def);
    }

    @Override
    public double getDouble(@NonNull String key) {
        return handle.getDouble(key);
    }

    @Override
    public double getDouble(@NonNull String key, double def) {
        return handle.getDouble(key, def);
    }

    @Override
    public boolean getBoolean(@NonNull String key) {
        return handle.getBoolean(key);
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean def) {
        return handle.getBoolean(key, def);
    }

    @Override
    public @NonNull List<String> getStringList(@NonNull String key) {
        return handle.getStringList(key);
    }

    @Override
    public @NonNull List<Map<?, ?>> getMapList(@NonNull String key) {
        return handle.getMapList(key);
    }

    @Override
    public @Nullable ConfigReader getConfig(@NonNull String key) {
        val config = handle.getConfigurationSection(key);
        return config == null ? null : new BukkitConfigReader(config);
    }
}
