package pers.neige.neigeitems.config;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigReader {
    static @NonNull ConfigReader parse(@NonNull ConfigurationSection config) {
        return new BukkitConfigReader(config);
    }

    static @NonNull ConfigReader parse(@NonNull Map<?, ?> config) {
        return new MapConfigReader(config);
    }

    static @NonNull ConfigReader parse(@NonNull String config) {
        return new MapConfigReader(new Yaml().load(config));
    }

    static @Nullable ConfigReader parse(@Nullable Object config) {
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

    int size();

    @NonNull
    Set<String> keySet();

    boolean containsKey(@NonNull String key);

    @Nullable Object get(@NonNull String key);

    @Nullable String getString(@NonNull String key);

    @Contract("_, !null -> !null")
    @Nullable String getString(@NonNull String key, @Nullable String def);

    int getInt(@NonNull String key);

    int getInt(@NonNull String key, int def);

    long getLong(@NonNull String key);

    long getLong(@NonNull String key, long def);

    double getDouble(@NonNull String key);

    double getDouble(@NonNull String key, double def);

    boolean getBoolean(@NonNull String key);

    boolean getBoolean(@NonNull String key, boolean def);

    @NonNull
    List<String> getStringList(@NonNull String key);

    @NonNull
    List<Map<?, ?>> getMapList(@NonNull String key);

    @Nullable ConfigReader getConfig(@NonNull String key);
}
