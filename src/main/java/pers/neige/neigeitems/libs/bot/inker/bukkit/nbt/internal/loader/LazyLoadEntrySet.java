package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class LazyLoadEntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {
    private final Map<K, V> loader;
    private final Set<K> keySet;

    public LazyLoadEntrySet(Map<K, V> loader, Set<K> keySet) {
        this.loader = loader;
        this.keySet = keySet;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Iterator<Map.Entry<K, V>>() {
            private final Iterator<K> i = keySet.iterator();
            private K latestKey;

            public boolean hasNext() {
                return i.hasNext();
            }

            public Map.Entry<K, V> next() {
                K next = i.next();
                latestKey = next;
                return new LazyLoadEntry<>(next, loader);
            }

            public void remove() {
                loader.remove(latestKey);
            }
        };
    }

    public int size() {
        return loader.size();
    }

    public boolean isEmpty() {
        return loader.isEmpty();
    }

    public void clear() {
        loader.clear();
    }

    public boolean contains(Object k) {
        return loader.containsKey(k);
    }
}
