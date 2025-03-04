package pers.neige.neigeitems.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigReader {
    @NotNull
    static ConfigReader parse(@NotNull ConfigurationSection config) {
        return new BukkitConfigReader(config);
    }

    @NotNull
    static ConfigReader parse(@NotNull Map<?, ?> config) {
        return new MapConfigReader(config);
    }

    @NotNull
    static ConfigReader parse(@NotNull String config) {
        return new MapConfigReader(new Yaml().load(config));
    }

    @Nullable
    static ConfigReader parse(@Nullable Object config) {
        if (config instanceof ConfigReader) {
            return (ConfigReader) config;
        } else if (config instanceof ConfigurationSection) {
            return new BukkitConfigReader((ConfigurationSection) config);
        } else if (config instanceof Map<?, ?>) {
            return new MapConfigReader((Map<?, ?>) config);
        } else if (config instanceof String) {
            Yaml yaml = new Yaml();
            return new MapConfigReader(yaml.load((String) config));
        }
        return null;
    }

    @NotNull
    Set<String> keySet();

    boolean containsKey(@NotNull String key);

    @Nullable
    Object get(@NotNull String key);

    @Nullable
    String getString(@NotNull String key);

    @Nullable
    @Contract("_, !null -> !null")
    String getString(@NotNull String key, @Nullable String def);

    int getInt(@NotNull String key);

    int getInt(@NotNull String key, int def);

    long getLong(@NotNull String key);

    long getLong(@NotNull String key, long def);

    boolean getBoolean(@NotNull String key);

    boolean getBoolean(@NotNull String key, boolean def);

    List<String> getStringList(@NotNull String key);

    ConfigReader getConfig(@NotNull String key);
}
