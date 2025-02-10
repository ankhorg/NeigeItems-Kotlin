package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.DelegateAbstractMap;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.LazyLoadEntrySet;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.StringUtils;
import pers.neige.neigeitems.ref.nbt.*;

import java.util.*;

public class NbtCompound extends Nbt<RefNbtTagCompound> implements NbtComponentLike {
    private static final boolean SET_RETURN_SUPPORT = CbVersion.v1_14_R1.isSupport();
    private static final boolean LONG_ARRAY_SUPPORT = CbVersion.v1_13_R1.isSupport();
    private static final boolean PUT_BYTE_LIST_SUPPORT = CbVersion.v1_17_R1.isSupport();
    private static final boolean PUT_INT_LIST_SUPPORT = CbVersion.v1_13_R1.isSupport();

    private final Map<String, Nbt<?>> delegateMap;
    private Set<String> oldKeySet;
    private Set<Entry<String, Nbt<?>>> oldEntrySet;

    NbtCompound(RefNbtTagCompound delegate) {
        super(delegate);
        this.delegateMap = new DelegateAbstractMap<>(this);
    }

    public NbtCompound() {
        super(new RefNbtTagCompound());
        this.delegateMap = new DelegateAbstractMap<>(this);
    }

    public static NbtCompound createUnsafe(Object delegate) {
        if (delegate instanceof RefNbtTagCompound) {
            return new NbtCompound((RefNbtTagCompound) delegate);
        }
        return null;
    }

    public Set<String> getAllKeys() {
        return delegate.getKeys();
    }

    @Override
    public Nbt<?> get(String key) {
        return Nbt.fromNms(delegate.get(key));
    }

    @Override
    public Nbt<?> get(Object key) {
        return (key instanceof String) ? Nbt.fromNms(delegate.get((String) key)) : null;
    }

    @Override
    public boolean containsKey(String key) {
        return delegate.hasKey(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof String && delegate.hasKey((String) key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegateMap.containsValue(value);
    }

    @Override
    public Nbt<?> put(String key, Nbt<?> value) {
        if (SET_RETURN_SUPPORT) {
            return Nbt.fromNms(delegate.set1(key, value.delegate));
        } else {
            Nbt<?> previous = Nbt.fromNms(delegate.get(key));
            delegate.set0(key, value.delegate);
            return previous;
        }
    }

    @Override
    public Nbt<?> remove(String key) {
        RefNbtBase oldValue = delegate.get(key);
        delegate.remove(key);
        return Nbt.fromNms(oldValue);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Nbt<?>> m) {
        delegateMap.putAll(m);
    }

    @Override
    public void clear() {
        delegateMap.clear();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Set<String> keySet() {
        return delegate.getKeys();
    }

    @NotNull
    @Override
    public Collection<Nbt<?>> values() {
        return delegateMap.values();
    }

    @Override
    public Set<Entry<String, Nbt<?>>> entrySet() {
        Set<String> newKeySet = delegate.getKeys();
        if (oldKeySet == newKeySet) {
            return oldEntrySet;
        }
        oldEntrySet = new LazyLoadEntrySet<>(this, newKeySet);
        oldKeySet = newKeySet;
        return oldEntrySet;
    }

    @Override
    public void delete(String key) {
        delegate.remove(key);
    }

    @Override
    public void set(String key, Nbt<?> value) {
        if (SET_RETURN_SUPPORT) {
            delegate.set1(key, value.delegate);
        } else {
            delegate.set0(key, value.delegate);
        }
    }

    private void set(String key, RefNbtBase value) {
        if (SET_RETURN_SUPPORT) {
            delegate.set1(key, value);
        } else {
            delegate.set0(key, value);
        }
    }

    @Override
    public void putByte(String key, byte value) {
        delegate.setByte(key, value);
    }

    @Override
    public void putShort(String key, short value) {
        delegate.setShort(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        delegate.setInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        delegate.setLong(key, value);
    }

    @Override
    public void putUUID(String key, UUID value) {
        delegate.setUUID(key, value);
    }

    @Override
    public void putFloat(String key, float value) {
        delegate.setFloat(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        delegate.setDouble(key, value);
    }

    @Override
    public void putString(String key, String value) {
        delegate.setString(key, value);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        delegate.setByteArray(key, value);
    }

    @Override
    public void putByteArray(String key, List<Byte> value) {
        if (PUT_BYTE_LIST_SUPPORT) {
            delegate.setByteArray(key, value);
        } else {
            byte[] bytes = new byte[value.size()];
            for (int i = 0; i < value.size(); i++) {
                Byte element = value.get(i);
                bytes[i] = (element == null) ? 0 : element;
            }
            delegate.setByteArray(key, bytes);
        }
    }

    @Override
    public void putIntArray(String key, int[] value) {
        delegate.setIntArray(key, value);
    }

    @Override
    public void putIntArray(String key, List<Integer> value) {
        if (PUT_INT_LIST_SUPPORT) {
            delegate.setIntArray(key, value);
        } else {
            int[] ints = new int[value.size()];
            for (int i = 0; i < value.size(); i++) {
                Integer element = value.get(i);
                ints[i] = (element == null) ? 0 : element;
            }
            delegate.setIntArray(key, ints);
        }
    }

    @Override
    public void putLongArray(String key, long[] value) {
        if (LONG_ARRAY_SUPPORT) {
            delegate.setLongArray(key, value);
        } else if (SET_RETURN_SUPPORT) {
            delegate.set1(key, new RefNbtTagLongArray(value));
        } else {
            delegate.set0(key, new RefNbtTagLongArray(value));
        }
    }

    @Override
    public void putLongArray(String key, List<Long> value) {
        if (LONG_ARRAY_SUPPORT) {
            delegate.setLongArray(key, value);
        } else if (SET_RETURN_SUPPORT) {
            delegate.set1(key, new RefNbtTagLongArray(value));
        } else {
            delegate.set0(key, new RefNbtTagLongArray(value));
        }
    }

    @Override
    public void putBoolean(String key, boolean value) {
        delegate.setBoolean(key, value);
    }

    @Override
    public byte getTagType(String key) {
        return delegate.getType(key);
    }

    @Override
    public boolean contains(String key, int value) {
        return delegate.hasKeyOfType(key, value);
    }

    @Override
    public byte getByte(@NotNull String key, byte def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asByte()
                : def;
    }

    @Override
    public @Nullable Byte getByteOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asByte()
                : null;
    }

    @Override
    public short getShort(@NotNull String key, short def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asShort()
                : def;
    }

    @Override
    public @Nullable Short getShortOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asShort()
                : null;
    }

    @Override
    public int getInt(@NotNull String key, int def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asInt()
                : def;
    }

    @Override
    public @Nullable Integer getIntOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asInt()
                : null;
    }

    @Override
    public long getLong(@NotNull String key, long def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asLong()
                : def;
    }

    @Override
    public @Nullable Long getLongOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asLong()
                : null;
    }

    @Override
    public @Nullable UUID getUUID(@NotNull String key, @Nullable UUID def) {
        RefNbtBase value = delegate.get(key);
        if (value instanceof RefNbtTagIntArray) {
            int[] ints = ((RefNbtTagIntArray) value).getInts();
            if (ints.length == 4) {
                return new UUID((long) ints[0] << 32 | (long) ints[1] & 4294967295L, (long) ints[2] << 32 | (long) ints[3] & 4294967295L);
            }
        } else {
            RefNbtBase most = delegate.get(key + "Most");
            RefNbtBase least = delegate.get(key + "Least");
            if (most instanceof RefNbtNumber && least instanceof RefNbtNumber) {
                return new UUID(
                        ((RefNbtNumber) most).asLong(),
                        ((RefNbtNumber) least).asLong()
                );
            }
        }
        return def;
    }

    @Override
    public float getFloat(@NotNull String key, float def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asFloat()
                : def;
    }

    @Override
    public @Nullable Float getFloatOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asFloat()
                : null;
    }

    @Override
    public double getDouble(@NotNull String key, double def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asDouble()
                : def;
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asDouble()
                : null;
    }

    @Override
    public @Nullable String getString(@NotNull String key, @Nullable String def) {
        RefNbtBase value = delegate.get(key);
        return (value != null)
                ? value.asString()
                : def;
    }

    @Override
    public @Nullable byte[] getByteArray(@NotNull String key, @Nullable byte[] def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtTagByteArray)
                ? ((RefNbtTagByteArray) value).getBytes()
                : def;
    }

    @Override
    public @Nullable int[] getIntArray(@NotNull String key, @Nullable int[] def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtTagIntArray)
                ? ((RefNbtTagIntArray) value).getInts()
                : def;
    }

    @Override
    public @Nullable long[] getLongArray(@NotNull String key, @Nullable long[] def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtTagLongArray)
                ? (LONG_ARRAY_SUPPORT ? ((RefNbtTagLongArray) value).getLongs() : ((RefNbtTagLongArray) value).longs)
                : def;
    }

    @Override
    public @Nullable NbtCompound getCompound(@NotNull String key, @Nullable NbtCompound def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtTagCompound)
                ? new NbtCompound((RefNbtTagCompound) value)
                : def;
    }

    @Override
    public @NotNull NbtCompound getOrCreateCompound(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        if (!(value instanceof RefNbtTagCompound)) {
            value = new RefNbtTagCompound();
            set(key, value);
        }
        return new NbtCompound((RefNbtTagCompound) value);
    }

    @Override
    public NbtList getList(String key, int elementType) {
        return new NbtList(delegate.getList(key, elementType));
    }

    @Override
    public @Nullable NbtList getList(@NotNull String key, @Nullable NbtList def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtTagList)
                ? new NbtList((RefNbtTagList) value)
                : def;
    }

    @Override
    public @NotNull NbtList getOrCreateList(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        if (!(value instanceof RefNbtTagList)) {
            value = new RefNbtTagList();
            set(key, value);
        }
        return new NbtList((RefNbtTagList) value);
    }

    @Override
    public boolean getBoolean(@NotNull String key, boolean def) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asByte() != 0
                : def;
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(@NotNull String key) {
        RefNbtBase value = delegate.get(key);
        return (value instanceof RefNbtNumber)
                ? ((RefNbtNumber) value).asByte() != 0
                : null;
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public @NotNull NbtCompound clone() {
        return new NbtCompound(cloneNms());
    }

    private @Nullable RefNbtBase getDeepRefNbt(@NotNull String key, char separator, char escape) {
        List<String> keys = StringUtils.split(key, separator, escape);

        RefNbtTagCompound currentNbtCompound = this.delegate;
        RefNbtBase value = null;

        for (String k : keys) {
            if (currentNbtCompound == null) {
                return null;
            }
            if (currentNbtCompound.hasKey(k)) {
                RefNbtBase obj = currentNbtCompound.get(k);
                value = obj;
                if (obj instanceof RefNbtTagCompound) {
                    currentNbtCompound = (RefNbtTagCompound) obj;
                } else {
                    currentNbtCompound = null;
                }
            } else {
                return null;
            }
        }

        return value;
    }

    private @Nullable RefNbtBase getDeepRefNbt(@NotNull String key) {
        return getDeepRefNbt(key, '.', '\\');
    }

    @Override
    public @Nullable Nbt<?> getDeep(@NotNull String key, char separator, char escape) {
        return fromNms(getDeepRefNbt(key, separator, escape));
    }

    @Override
    public @Nullable Nbt<?> getDeep(@NotNull String key) {
        return getDeep(key, '.', '\\');
    }

    @Override
    public byte getDeepByte(@NotNull String key, byte def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asByte()
                : def;
    }

    @Override
    public @Nullable Byte getDeepByteOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asByte()
                : null;
    }

    @Override
    public short getDeepShort(@NotNull String key, short def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asShort()
                : def;
    }

    @Override
    public @Nullable Short getDeepShortOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asShort()
                : null;
    }

    @Override
    public int getDeepInt(@NotNull String key, int def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asInt()
                : def;
    }

    @Override
    public @Nullable Integer getDeepIntOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asInt()
                : null;
    }

    @Override
    public long getDeepLong(@NotNull String key, long def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asLong()
                : def;
    }

    @Override
    public @Nullable Long getDeepLongOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asLong()
                : null;
    }

    @Override
    public @Nullable UUID getDeepUUID(@NotNull String key, @Nullable UUID def) {
        RefNbtBase value = getDeepRefNbt(key);
        if (value instanceof RefNbtTagIntArray) {
            int[] ints = ((RefNbtTagIntArray) value).getInts();
            if (ints.length == 4) {
                return new UUID((long) ints[0] << 32 | (long) ints[1] & 4294967295L, (long) ints[2] << 32 | (long) ints[3] & 4294967295L);
            }
        } else {
            RefNbtBase most = getDeepRefNbt(key + "Most");
            RefNbtBase least = getDeepRefNbt(key + "Least");
            if (most instanceof RefNbtNumber && least instanceof RefNbtNumber) {
                return new UUID(
                        ((RefNbtNumber) most).asLong(),
                        ((RefNbtNumber) least).asLong()
                );
            }
        }
        return def;
    }

    @Override
    public float getDeepFloat(@NotNull String key, float def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asFloat()
                : def;
    }

    @Override
    public @Nullable Float getDeepFloatOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asFloat()
                : null;
    }

    @Override
    public double getDeepDouble(@NotNull String key, double def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asDouble()
                : def;
    }

    @Override
    public @Nullable Double getDeepDoubleOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asDouble()
                : null;
    }

    @Override
    public @Nullable String getDeepString(@NotNull String key, @Nullable String def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value != null
                ? value.asString()
                : def;
    }

    @Override
    public @Nullable byte[] getDeepByteArray(@NotNull String key, @Nullable byte[] def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtTagByteArray
                ? ((RefNbtTagByteArray) value).getBytes()
                : def;
    }

    @Override
    public @Nullable int[] getDeepIntArray(@NotNull String key, @Nullable int[] def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtTagIntArray
                ? ((RefNbtTagIntArray) value).getInts()
                : def;
    }

    @Override
    public @Nullable long[] getDeepLongArray(@NotNull String key, @Nullable long[] def) {
        RefNbtBase value = getDeepRefNbt(key);
        return (value instanceof RefNbtTagLongArray)
                ? (LONG_ARRAY_SUPPORT ? ((RefNbtTagLongArray) value).getLongs() : ((RefNbtTagLongArray) value).longs)
                : def;
    }

    @Override
    public @Nullable NbtCompound getDeepCompound(@NotNull String key, @Nullable NbtCompound def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtTagCompound
                ? new NbtCompound((RefNbtTagCompound) value)
                : def;
    }

    @Override
    public @Nullable NbtList getDeepList(@NotNull String key, @Nullable NbtList def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtTagList
                ? new NbtList((RefNbtTagList) value)
                : def;
    }

    @Override
    public boolean getDeepBoolean(@NotNull String key, boolean def) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asByte() != 0
                : def;
    }

    @Override
    public @Nullable Boolean getDeepBooleanOrNull(@NotNull String key) {
        RefNbtBase value = getDeepRefNbt(key);
        return value instanceof RefNbtNumber
                ? ((RefNbtNumber) value).asByte() != 0
                : null;
    }

    private void putDeepRefNbt(@NotNull String key, @NotNull RefNbtBase value, boolean force, char separator, char escape) {
        List<String> keys = StringUtils.split(key, separator, escape);

        RefNbtTagCompound currentNbtCompound = this.delegate;

        // 遍历key
        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);

            // 未达末级
            if (i != (keys.size() - 1)) {
                // 存在key
                if (currentNbtCompound.hasKey(k)) {
                    RefNbtBase obj = currentNbtCompound.get(k);
                    // 属于RefNbtTagCompound
                    if (obj instanceof RefNbtTagCompound) {
                        currentNbtCompound = (RefNbtTagCompound) obj;
                        // 不属于RefNbtTagCompound
                    } else {
                        // 如果需要强制设置
                        if (force) {
                            // new RefNbtTagCompound()并设置
                            obj = new RefNbtTagCompound();
                            if (SET_RETURN_SUPPORT) {
                                currentNbtCompound.set1(k, obj);
                            } else {
                                currentNbtCompound.set0(k, obj);
                            }
                            currentNbtCompound = (RefNbtTagCompound) obj;
                            // 不需要强制设置
                        } else {
                            // 返回null
                            return;
                        }
                    }
                    // 不存在key
                } else {
                    // 创建并设置
                    RefNbtTagCompound obj = new RefNbtTagCompound();
                    if (SET_RETURN_SUPPORT) {
                        currentNbtCompound.set1(k, obj);
                    } else {
                        currentNbtCompound.set0(k, obj);
                    }
                    currentNbtCompound = obj;
                }
                // 已达末级
            } else {
                if (SET_RETURN_SUPPORT) {
                    currentNbtCompound.set1(k, value);
                } else {
                    currentNbtCompound.set0(k, value);
                }
                return;
            }
        }
    }

    private void putDeepRefNbt(@NotNull String key, @NotNull RefNbtBase value, boolean force) {
        putDeepRefNbt(key, value, force, '.', '\\');
    }

    @Override
    public void putDeep(@NotNull String key, @NotNull Nbt<?> value, boolean force) {
        putDeepRefNbt(key, value.delegate, force);
    }

    @Override
    public void putDeepByteArray(@NotNull String key, byte[] value, boolean force) {
        putDeepRefNbt(key, new RefNbtTagByteArray(value), force);
    }

    @Override
    public void putDeepByteArray(@NotNull String key, @NotNull List<Byte> value, boolean force) {
        putDeepRefNbt(key, new RefNbtTagByteArray(value), force);
    }

    @Override
    public void putDeepIntArray(@NotNull String key, int[] value, boolean force) {
        putDeepRefNbt(key, new RefNbtTagIntArray(value), force);
    }

    @Override
    public void putDeepIntArray(@NotNull String key, @NotNull List<Integer> value, boolean force) {
        putDeepRefNbt(key, new RefNbtTagIntArray(value), force);
    }

    @Override
    public void putDeepLongArray(@NotNull String key, long[] value, boolean force) {
        putDeepRefNbt(key, new RefNbtTagLongArray(value), force);
    }

    @Override
    public void putDeepLongArray(@NotNull String key, @NotNull List<Long> value, boolean force) {
        putDeepRefNbt(key, new RefNbtTagLongArray(value), force);
    }

    /**
     * 将当前 NbtCompound 保存至对应的 ItemStack.
     *
     * @param itemStack 用于接收 NbtCompound 的 ItemStack.
     */
    public void saveTo(@Nullable ItemStack itemStack) {
        new NbtItemStack(itemStack).setTag(this);
    }

    /**
     * 使用给定的 NbtCompound 覆盖当前 NbtCompound.
     *
     * @param overlayCompound 用于提供覆盖值的 NbtCompound.
     * @return this.
     */
    public @NotNull NbtCompound coverWith(@NotNull NbtCompound overlayCompound) {
        NbtUtils.coverWith(this.delegate, overlayCompound.delegate);
        return this;
    }

    public static class Unsafe {
        public static NbtCompound of(Object nms) {
            return new NbtCompound((RefNbtTagCompound) nms);
        }
    }
}
