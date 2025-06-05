package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader;

import lombok.NonNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public final class DelegateAbstractMap<K, V> extends AbstractMap<K, V> {
    private final Map<K, V> delegateMap;

    public DelegateAbstractMap(Map<K, V> delegateMap) {
        this.delegateMap = delegateMap;
    }

    @Override
    public V get(Object key) {
        return delegateMap.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return delegateMap.containsKey(key);
    }

    @Override
    public V put(K key, V value) {
        return delegateMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return delegateMap.remove(key);
    }

    @Override
    public int size() {
        return delegateMap.size();
    }

    @Override
    public @NonNull Set<K> keySet() {
        return delegateMap.keySet();
    }

    @Override
    public @NonNull Set<Entry<K, V>> entrySet() {
        return delegateMap.entrySet();
    }
}
