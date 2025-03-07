package pers.neige.neigeitems.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.StringUtils;

import java.util.*;

public class MapConfigReader implements ConfigReader {
    @NotNull
    private final Map<String, Object> handle = new HashMap<>();

    public MapConfigReader(@NotNull Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            handle.put(entry.getKey().toString(), entry.getValue());
        }
    }

    @NotNull
    public Map<?, ?> getHandle() {
        return handle;
    }

    @Override
    @NotNull
    public Set<String> keySet() {
        return handle.keySet();
    }

    @Override
    public boolean containsKey(@NotNull String key) {
        return handle.containsKey(key);
    }

    @Override
    @Nullable
    public Object get(@NotNull String key) {
        List<String> keys = StringUtils.split(key, '.', '\\');

        Map<?, ?> currentMap = this.handle;
        Object value = null;

        for (String k : keys) {
            if (currentMap == null) {
                return null;
            }
            if (currentMap.containsKey(k)) {
                Object obj = currentMap.get(k);
                value = obj;
                if (obj instanceof Map<?, ?>) {
                    currentMap = (Map<?, ?>) obj;
                } else {
                    currentMap = null;
                }
            } else {
                return null;
            }
        }

        return value;
    }

    @Override
    public String getString(@NotNull String key) {
        return getString(key, null);
    }

    @Override
    @Nullable
    @Contract("_, !null -> !null")
    public String getString(@NotNull String key, @Nullable String def) {
        Object value = get(key);
        return value == null ? def : value.toString();
    }

    @Override
    public int getInt(@NotNull String key) {
        return getInt(key, 0);
    }

    @Override
    public int getInt(@NotNull String key, int def) {
        Object value = get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value == null) {
            return def;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    @Override
    public long getLong(@NotNull String key) {
        return getLong(key, 0);
    }

    @Override
    public long getLong(@NotNull String key, long def) {
        Object value = get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value == null) {
            return def;
        } else {
            return Long.parseLong(value.toString());
        }
    }

    @Override
    public boolean getBoolean(@NotNull String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(@NotNull String key, boolean def) {
        Object value = get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value == null) {
            return def;
        } else {
            return Boolean.parseBoolean(value.toString());
        }
    }

    @Override
    public List<String> getStringList(@NotNull String key) {
        Object value = get(key);
        if (value instanceof List<?>) {
            List<String> result = new ArrayList<>();
            for (Object object : ((List<?>) value)) {
                if (object == null) continue;
                result.add(object.toString());
            }
            return result;
        } else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public ConfigReader getConfig(@NotNull String key) {
        Object value = get(key);
        return value instanceof Map<?, ?> ? new MapConfigReader((Map<?, ?>) value) : null;
    }
}
