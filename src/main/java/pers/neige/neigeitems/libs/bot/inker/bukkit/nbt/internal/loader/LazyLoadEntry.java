package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader;

import java.util.Map;

public final class LazyLoadEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private final Map<K, V> loader;

    public LazyLoadEntry(K key, Map<K, V> loader) {
        this.key = key;
        this.loader = loader;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return loader.get(key);
    }

    @Override
    public V setValue(V value) {
        return loader.put(key, value);
    }
}