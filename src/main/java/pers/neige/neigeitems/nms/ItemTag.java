package pers.neige.neigeitems.nms;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemTag extends ItemTagData implements Map<String, ItemTagData> {
    private final Object nbt;

    public Object getNbt() {
        return this.nbt;
    }

    private final Map<String, Object> map;


    public ItemTag() {
        super(NMSGeneric.newItemTag());
        this.nbt = this.data;
        this.map = NMSGeneric.getNbtMap(nbt);
    }

    public ItemTag(Object nbt) {
        super(nbt);
        this.nbt = this.data;
        this.map = NMSGeneric.getNbtMap(nbt);
    }

    public void saveTo(ItemStack itemStack) {
        NMSGeneric.setItemTag(itemStack, this);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof ItemTagData) {
            return this.map.containsValue(((ItemTagData)value).data);
        } else {
            return this.map.containsValue(value);
        }
    }

    @Nullable
    @Override
    public ItemTagData get(Object key) {
        Object value = this.map.get(key);
        if (value != null) {
            return new ItemTagData(value);
        } else {
            return null;
        }
    }

    @NotNull
    public ItemTagData getOrElse(String key, @NotNull ItemTagData base) {
        Object value = this.map.get(key);
        if (value != null) {
            return new ItemTagData(value);
        } else {
            return base;
        }
    }

    @NotNull
    public ItemTagData getOrPut(String key, @NotNull ItemTagData base) {
        Object value = this.map.get(key);
        if (value != null) {
            return new ItemTagData(value);
        } else {
            return put(key, base);
        }
    }

    @Nullable
    public ItemTagData getDeep(String key) {
        Object value = NMSGeneric.getDeep(nbt, key);
        if (value != null) {
            return new ItemTagData(value);
        } else {
            return null;
        }
    }

    @NotNull
    public ItemTagData getDeepOrElse(String key, @NotNull ItemTagData base) {
        Object value = NMSGeneric.getDeep(nbt, key);
        if (value != null) {
            return new ItemTagData(value);
        } else {
            return base;
        }
    }

    @NotNull
    public ItemTagData getDeepOrPut(String key, @NotNull ItemTagData base) {
        Object value = NMSGeneric.getDeep(nbt, key);
        if (value != null) {
            return new ItemTagData(value);
        } else {
            return putDeep(key, base);
        }
    }

    @Nullable
    @Override
    public ItemTagData put(String key, @Nullable ItemTagData value) {
        if (value == null) {
            this.map.remove(key);
            return null;
        } else {
            this.map.put(key, value.data);
            return value;
        }
    }

    @Nullable
    public ItemTagData put(String key, @Nullable Object value) {
        if (value == null) {
            this.map.remove(key);
            return null;
        } else {
            Object result = NMSGeneric.asNBT(value);
            this.map.put(key, result);
            return new ItemTagData(result);
        }
    }

    @Nullable
    public ItemTagData putDeep(String key, @Nullable ItemTagData value) {
        NMSGeneric.putDeep(nbt, key, value.data);
        return value;
    }

    @Nullable
    public ItemTagData putDeep(String key, @Nullable Object value) {
        if (value == null) {
            putDeep(key, null);
            return null;
        } else {
            Object result = NMSGeneric.asNBT(value);
            NMSGeneric.putDeep(nbt, key, result);
            return new ItemTagData(result);
        }
    }

    @Override
    public ItemTagData remove(Object key) {
        Object value = this.map.remove(key);
        if (value != null) {
            return new ItemTagData(value);
        }
        return null;
    }

    public ItemTagData removeDeep(String key) {
        ItemTagData result = getDeep(key);
        putDeep(key, null);
        return result;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends ItemTagData> m) {
        m.forEach((key, value) -> this.map.put(key, value.data));
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @NotNull
    @Override
    public Collection<ItemTagData> values() {
        Set<ItemTagData> temp = new HashSet<>();
        this.map.values().forEach((value) -> temp.add(new ItemTagData(value)));
        return temp;
    }

    @NotNull
    @Override
    public Set<Entry<String, ItemTagData>> entrySet() {
        Map<String, ItemTagData> tempMap = new HashMap<>();
        this.map.forEach((key, value) -> tempMap.put(key, new ItemTagData(value)));
        return tempMap.entrySet();
    }
}