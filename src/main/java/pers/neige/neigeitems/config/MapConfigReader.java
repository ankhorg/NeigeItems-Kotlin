package pers.neige.neigeitems.config;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.StringUtils;

import java.util.*;

public class MapConfigReader implements ConfigReader {
    private final @NonNull Map<String, Object> handle = new HashMap<>();

    public MapConfigReader(@NonNull Map<?, ?> map) {
        for (val entry : map.entrySet()) {
            handle.put(entry.getKey().toString(), entry.getValue());
        }
    }

    public @NonNull Map<?, ?> getHandle() {
        return handle;
    }

    @Override
    public int size() {
        return handle.size();
    }

    @Override
    public @NonNull Set<String> keySet() {
        return handle.keySet();
    }

    @Override
    public boolean containsKey(@NonNull String key) {
        return handle.containsKey(key);
    }

    @Override
    public @Nullable Object get(@NonNull String key) {
        val keys = StringUtils.split(key, '.', '\\');

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
    public String getString(@NonNull String key) {
        return getString(key, null);
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable String getString(@NonNull String key, @Nullable String def) {
        val value = get(key);
        return value == null ? def : value.toString();
    }

    @Override
    public int getInt(@NonNull String key) {
        return getInt(key, 0);
    }

    @Override
    public int getInt(@NonNull String key, int def) {
        val value = get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value == null) {
            return def;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    @Override
    public long getLong(@NonNull String key) {
        return getLong(key, 0);
    }

    @Override
    public long getLong(@NonNull String key, long def) {
        val value = get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value == null) {
            return def;
        } else {
            return Long.parseLong(value.toString());
        }
    }

    @Override
    public double getDouble(@NonNull String key) {
        return getDouble(key, 0);
    }

    @Override
    public double getDouble(@NonNull String key, double def) {
        val value = get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value == null) {
            return def;
        } else {
            return Double.parseDouble(value.toString());
        }
    }

    @Override
    public boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean def) {
        val value = get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value == null) {
            return def;
        } else {
            return Boolean.parseBoolean(value.toString());
        }
    }

    @Override
    public @NonNull List<String> getStringList(@NonNull String key) {
        val value = get(key);
        if (value instanceof List<?>) {
            val result = new ArrayList<String>();
            for (val object : ((List<?>) value)) {
                if (object == null) continue;
                result.add(object.toString());
            }
            return result;
        } else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public @NonNull List<Map<?, ?>> getMapList(@NonNull String key) {
        val value = get(key);
        if (value instanceof List<?>) {
            val result = new ArrayList<Map<?, ?>>();
            for (val object : ((List<?>) value)) {
                if (!(object instanceof Map)) continue;
                result.add((Map<?, ?>) object);
            }
            return result;
        } else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public @Nullable ConfigReader getConfig(@NonNull String key) {
        val value = get(key);
        return value instanceof Map<?, ?> ? new MapConfigReader((Map<?, ?>) value) : null;
    }
}
