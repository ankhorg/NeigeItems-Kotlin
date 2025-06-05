package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke.InvokeUtil;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.DelegateAbstractMap;
import pers.neige.neigeitems.ref.nbt.RefCraftMetaItem;
import pers.neige.neigeitems.ref.nbt.RefNbtBase;

import java.util.*;

public class NbtBukkitItemComponent implements NbtComponentLike {
    private static final Logger logger = LoggerFactory.getLogger(NbtBukkitItemComponent.class.getSimpleName());

    private static final Set<String> HANDLED_TAGS = ImmutableSet.copyOf(RefCraftMetaItem.HANDLED_TAGS);

    private final Map<String, Nbt<?>> delegateMap;
    private final ItemStack itemStack;
    private final RefCraftMetaItem refItemMeta;
    private Set<Entry<String, Nbt<?>>> entrySet;

    NbtBukkitItemComponent(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.delegateMap = new DelegateAbstractMap<>(this);
        ItemMeta meta = InvokeUtil.getItemMeta(itemStack);
        if (meta == null) {
            meta = itemStack.getItemMeta();
            InvokeUtil.setItemMeta(itemStack, meta);
        }
        refItemMeta = (RefCraftMetaItem) (Object) meta;
    }

    @Override
    public Nbt<?> get(String key) {
        if (HANDLED_TAGS.contains(key)) {
            logger.warn("key {} isn't support for direct access", key);
            return null;
        } else {
            return Nbt.fromNms(refItemMeta.unhandledTags.get(key));
        }
    }

    @Override
    public boolean containsKey(String key) {
        if (HANDLED_TAGS.contains(key)) {
            logger.warn("key {} isn't support for direct access", key);
            return false;
        } else {
            return refItemMeta.unhandledTags.containsKey(key);
        }
    }

    @Override
    public Nbt<?> put(String key, Nbt<?> value) {
        if (HANDLED_TAGS.contains(key)) {
            logger.warn("key {} isn't support for direct access", key);
            return null;
        } else {
            return Nbt.fromNms(refItemMeta.unhandledTags.put(key, value.delegate));
        }
    }

    @Override
    public Nbt<?> remove(String key) {
        if (HANDLED_TAGS.contains(key)) {
            logger.warn("key {} isn't support for direct access", key);
            return null;
        } else {
            return Nbt.fromNms(refItemMeta.unhandledTags.remove(key));
        }
    }

    @Override
    public @NonNull NbtBukkitItemComponent clone() {
        return new NbtBukkitItemComponent(NbtUtils.asBukkitCopy(itemStack));
    }

    @Override
    public int size() {
        return refItemMeta.unhandledTags.size();
    }

    @Override
    public byte getId() {
        return NbtType.TAG_COMPOUND;
    }

    @Override
    public String getAsString() {
        return itemStack.toString();
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends Nbt<?>> m) {
        delegateMap.putAll(m);
    }

    @Override
    public void clear() {
        delegateMap.clear();
    }

    @Override
    public boolean isEmpty() {
        return delegateMap.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return delegateMap.containsValue(value);
    }

    @Override
    public @NonNull Collection<Nbt<?>> values() {
        return delegateMap.values();
    }

    @Override
    public @NonNull Set<String> keySet() {
        return refItemMeta.unhandledTags.keySet();
    }

    @Override
    public @NonNull Set<Entry<String, Nbt<?>>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<String, Nbt<?>>>() {
                @Override
                public @NonNull Iterator<Entry<String, Nbt<?>>> iterator() {
                    return new DelegateIterator(refItemMeta.unhandledTags.entrySet().iterator());
                }

                @Override
                public int size() {
                    return refItemMeta.unhandledTags.size();
                }
            };
        }
        return entrySet;
    }

    private static class DelegateIterator implements Iterator<Entry<String, Nbt<?>>> {
        private final Iterator<Entry<String, RefNbtBase>> delegate;

        private DelegateIterator(Iterator<Entry<String, RefNbtBase>> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public Entry<String, Nbt<?>> next() {
            return new DelegateEntry(delegate.next());
        }

        @Override
        public void remove() {
            delegate.remove();
        }
    }

    private static class DelegateEntry implements Entry<String, Nbt<?>> {
        private final Entry<String, RefNbtBase> delegate;

        private DelegateEntry(Entry<String, RefNbtBase> delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getKey() {
            return delegate.getKey();
        }

        @Override
        public Nbt<?> getValue() {
            return Nbt.fromNms(delegate.getValue());
        }

        @Override
        public Nbt<?> setValue(Nbt<?> value) {
            return Nbt.fromNms(delegate.setValue(value.delegate));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DelegateEntry that = (DelegateEntry) o;

            return delegate.equals(that.delegate);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }
    }
}
