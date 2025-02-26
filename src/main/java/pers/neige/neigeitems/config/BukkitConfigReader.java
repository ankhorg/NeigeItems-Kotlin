package pers.neige.neigeitems.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class BukkitConfigReader implements ConfigReader {
    @NotNull
    private final ConfigurationSection handle;

    public BukkitConfigReader(@NotNull ConfigurationSection config) {
        this.handle = config;
    }

    @NotNull
    public ConfigurationSection getHandle() {
        return handle;
    }

    @Override
    @NotNull
    public Set<String> keySet() {
        return handle.getKeys(false);
    }

    @Override
    public boolean containsKey(@NotNull String key) {
        return handle.contains(key);
    }

    @Override
    @Nullable
    public Object get(@NotNull String key) {
        return handle.get(key);
    }

    @Override
    public String getString(@NotNull String key) {
        return handle.getString(key);
    }

    @Override
    @Nullable
    @Contract("_, !null -> !null")
    public String getString(@NotNull String key, @Nullable String def) {
        return handle.getString(key, def);
    }

    @Override
    public int getInt(@NotNull String key) {
        return handle.getInt(key);
    }

    @Override
    public int getInt(@NotNull String key, int def) {
        return handle.getInt(key, def);
    }

    @Override
    public long getLong(@NotNull String key) {
        return handle.getLong(key);
    }

    @Override
    public long getLong(@NotNull String key, long def) {
        return handle.getLong(key, def);
    }

    @Override
    public boolean getBoolean(@NotNull String key) {
        return handle.getBoolean(key);
    }

    @Override
    public boolean getBoolean(@NotNull String key, boolean def) {
        return handle.getBoolean(key, def);
    }

    @Override
    public List<String> getStringList(@NotNull String key) {
        return handle.getStringList(key);
    }

    @Override
    public ConfigReader getConfig(@NotNull String key) {
        ConfigurationSection config = handle.getConfigurationSection(key);
        return config == null ? null : new BukkitConfigReader(config);
    }
}
