package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.*;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.ArrayUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NbtComponentLike extends NbtLike, Map<String, Nbt<?>> {
    Nbt<?> get(String key);

    default Nbt<?> get(Object key) {
        return (key instanceof String) ? get((String) key) : null;
    }

    boolean containsKey(String key);

    default boolean containsKey(Object key) {
        return key instanceof String && containsKey((String) key);
    }

    Nbt<?> remove(String key);

    default Nbt<?> remove(Object key) {
        return (key instanceof String)
                ? remove((String) key)
                : null;
    }

    default Nbt<?> removeDeep(@NotNull String key) {
        String[] keys = StringUtils.split(key, '.');

        NbtComponentLike currentNbtCompound = this;
        for (int i = 0; i < (keys.length - 1); i++) {
            String k = keys[i];
            if (currentNbtCompound == null) {
                return null;
            }
            if (currentNbtCompound.containsKey(k)) {
                Nbt<?> obj = currentNbtCompound.get(k);
                if (obj instanceof NbtComponentLike) {
                    currentNbtCompound = (NbtComponentLike) obj;
                } else {
                    currentNbtCompound = null;
                }
            } else {
                return null;
            }
        }
        if (currentNbtCompound != null) {
            return currentNbtCompound.remove(keys[keys.length - 1]);
        }
        return null;
    }

    default boolean contains(Object key) {
        return key instanceof String && containsKey(key);
    }

    default boolean contains(String key) {
        return containsKey(key);
    }

    default void delete(Object key) {
        if (key != null) {
            delete((String) key);
        }
    }

    default void delete(String key) {
        remove(key);
    }

    default void set(String key, Nbt<?> value) {
        put(key, value);
    }

    default void putByte(String key, byte value) {
        set(key, NbtByte.valueOf(value));
    }

    default void putShort(String key, short value) {
        set(key, NbtShort.valueOf(value));
    }

    default void putInt(String key, int value) {
        set(key, NbtInt.valueOf(value));
    }

    default void putLong(String key, long value) {
        set(key, NbtLong.valueOf(value));
    }

    default void putUUID(String key, UUID value) {
        set(key + "Most", NbtLong.valueOf(value.getMostSignificantBits()));
        set(key + "Least", NbtLong.valueOf(value.getLeastSignificantBits()));
    }

    default void putFloat(String key, float value) {
        set(key, NbtFloat.valueOf(value));
    }

    default void putDouble(String key, double value) {
        set(key, NbtDouble.valueOf(value));
    }

    default void putString(String key, String value) {
        set(key, NbtString.valueOf(value));
    }

    default void putByteArray(String key, byte[] value) {
        set(key, new NbtByteArray(value));
    }

    default void putByteArray(String key, List<Byte> value) {
        set(key, new NbtByteArray(value));
    }

    default void putIntArray(String key, int[] value) {
        set(key, new NbtIntArray(value));
    }

    default void putIntArray(String key, List<Integer> value) {
        set(key, new NbtIntArray(value));
    }

    default void putLongArray(String key, long[] value) {
        set(key, new NbtLongArray(value));
    }

    default void putLongArray(String key, List<Long> value) {
        set(key, new NbtLongArray(value));
    }

    default void putBoolean(String key, boolean value) {
        set(key, NbtByte.valueOf(value));
    }

    default byte getTagType(String key) {
        return get(key).getId();
    }

    default boolean contains(String key, int value) {
        Nbt<?> nbtValue = get(key);
        return nbtValue != null && nbtValue.getId() == value;
    }

    /**
     * 根据 NBT键 获取对应的 byte, 如果没有找到对应的 byte 则返回 0.
     *
     * @param key 要获取 byte 的 NBT键.
     * @return 待查找的 byte.
     */
    default byte getByte(@NotNull String key) {
        return getByte(key, (byte) 0);
    }

    /**
     * 根据 NBT键 获取对应的 byte, 如果没有找到对应的 byte 则返回默认值.
     *
     * @param key 要获取 byte 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 byte.
     */
    default byte getByte(@NotNull String key, byte def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 short, 如果没有找到对应的 short 则返回 0.
     *
     * @param key 要获取 short 的 NBT键.
     * @return 待查找的 short.
     */
    default short getShort(@NotNull String key) {
        return getShort(key, (short) 0);
    }

    /**
     * 根据 NBT键 获取对应的 short, 如果没有找到对应的 short 则返回默认值.
     *
     * @param key 要获取 short 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 short.
     */
    default short getShort(@NotNull String key, short def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsShort()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 int, 如果没有找到对应的 int 则返回 0.
     *
     * @param key 要获取 int 的 NBT键.
     * @return 待查找的 int.
     */
    default int getInt(@NotNull String key) {
        return getInt(key, 0);
    }

    /**
     * 根据 NBT键 获取对应的 int, 如果没有找到对应的 int 则返回默认值.
     *
     * @param key 要获取 int 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 int.
     */
    default int getInt(@NotNull String key, int def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsInt()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 long, 如果没有找到对应的 long 则返回 0.
     *
     * @param key 要获取 long 的 NBT键.
     * @return 待查找的 long.
     */
    default long getLong(@NotNull String key) {
        return getLong(key, 0);
    }

    /**
     * 根据 NBT键 获取对应的 long, 如果没有找到对应的 long 则返回默认值.
     *
     * @param key 要获取 long 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 long.
     */
    default long getLong(@NotNull String key, long def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsLong()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 UUID, 如果没有找到对应的 UUID 则返回 null.
     *
     * @param key 要获取 UUID 的 NBT键.
     * @return 待查找的 UUID.
     */
    default @Nullable UUID getUUID(@NotNull String key) {
        return getUUID(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 UUID, 如果没有找到对应的 UUID 则返回默认值.
     *
     * @param key 要获取 UUID 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 UUID, 则返回的默认值.
     * @return 待查找的 UUID.
     */
    default @Nullable UUID getUUID(@NotNull String key, @Nullable UUID def) {
        Nbt<?> value = get(key);
        if (value instanceof NbtIntArray) {
            int[] ints = ((NbtIntArray) value).getAsIntArray();
            if (ints.length == 4) {
                return new UUID((long) ints[0] << 32 | (long) ints[1] & 4294967295L, (long) ints[2] << 32 | (long) ints[3] & 4294967295L);
            }
        } else {
            Nbt<?> most = get(key + "Most");
            Nbt<?> least = get(key + "Least");
            if (most instanceof NbtNumeric<?> && least instanceof NbtNumeric<?>) {
                return new UUID(
                        ((NbtNumeric<?>) most).getAsLong(),
                        ((NbtNumeric<?>) least).getAsLong()
                );
            }
        }
        return def;
    }

    /**
     * 根据 NBT键 获取对应的 float, 如果没有找到对应的 float 则返回 0.
     *
     * @param key 要获取 float 的 NBT键.
     * @return 待查找的 float.
     */
    default float getFloat(@NotNull String key) {
        return getFloat(key, 0);
    }

    /**
     * 根据 NBT键 获取对应的 float, 如果没有找到对应的 float 则返回默认值.
     *
     * @param key 要获取 float 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 float.
     */
    default float getFloat(@NotNull String key, float def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsFloat()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 double, 如果没有找到对应的 double 则返回 0.
     *
     * @param key 要获取 double 的 NBT键.
     * @return 待查找的 double.
     */
    default double getDouble(@NotNull String key) {
        return getDouble(key, 0);
    }

    /**
     * 根据 NBT键 获取对应的 double, 如果没有找到对应的 double 则返回默认值.
     *
     * @param key 要获取 double 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 double.
     */
    default double getDouble(@NotNull String key, double def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsDouble()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 String, 如果没有找到对应的 String 则返回 null.
     *
     * @param key 要获取 String 的 NBT键.
     * @return 待查找的 String.
     */
    default @Nullable String getString(@NotNull String key) {
        return getString(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 String, 如果没有找到对应的 String 则返回默认值.
     *
     * @param key 要获取 String 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不存在, 则返回的默认值.
     * @return 待查找的 String.
     */
    default @Nullable String getString(@NotNull String key, @Nullable String def) {
        Nbt<?> value = get(key);
        return (value != null)
                ? value.getAsString()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 byte[], 如果没有找到对应的 byte[] 则返回空数组.
     *
     * @param key 要获取 byte[] 的 NBT键.
     * @return 待查找的 byte[].
     */
    default byte[] getByteArray(@NotNull String key) {
        return getByteArray(key, ArrayUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * 根据 NBT键 获取对应的 byte[], 如果没有找到对应的 byte[] 则返回默认值.
     *
     * @param key 要获取 byte[] 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 byte[], 则返回的默认值.
     * @return 待查找的 byte[].
     */
    default byte[] getByteArray(@NotNull String key, byte[] def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtByteArray)
                ? ((NbtByteArray) value).getAsByteArray()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 int[], 如果没有找到对应的 int[] 则返回空数组.
     *
     * @param key 要获取 int[] 的 NBT键.
     * @return 待查找的 int[].
     */
    default int[] getIntArray(@NotNull String key) {
        return getIntArray(key, ArrayUtils.EMPTY_INT_ARRAY);
    }

    /**
     * 根据 NBT键 获取对应的 int[], 如果没有找到对应的 int[] 则返回默认值.
     *
     * @param key 要获取 int[] 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 int[], 则返回的默认值.
     * @return 待查找的 int[].
     */
    default int[] getIntArray(@NotNull String key, int[] def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtIntArray)
                ? ((NbtIntArray) value).getAsIntArray()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 long[], 如果没有找到对应的 long[] 则返回空数组.
     *
     * @param key 要获取 long[] 的 NBT键.
     * @return 待查找的 long[].
     */
    default long[] getLongArray(@NotNull String key) {
        return getLongArray(key, ArrayUtils.EMPTY_LONG_ARRAY);
    }

    /**
     * 根据 NBT键 获取对应的 long[], 如果没有找到对应的 long[] 则返回默认值.
     *
     * @param key 要获取 long[] 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 long[], 则返回的默认值.
     * @return 待查找的 long[].
     */
    default long[] getLongArray(@NotNull String key, long[] def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtLongArray)
                ? ((NbtLongArray) value).getAsLongArray()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 NbtCompound, 如果没有找到对应的 NbtCompound 则返回 null.
     *
     * @param key 要获取 NbtCompound 的 NBT键.
     * @return 待查找的 NbtCompound.
     */
    default @Nullable NbtCompound getCompound(@NotNull String key) {
        return getCompound(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 NbtCompound, 如果没有找到对应的 NbtCompound 则返回默认值.
     *
     * @param key 要获取 NbtCompound 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 NbtCompound, 则返回的默认值.
     * @return 待查找的 NbtCompound.
     */
    default @Nullable NbtCompound getCompound(@NotNull String key, @Nullable NbtCompound def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtCompound)
                ? (NbtCompound) value
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 NbtCompound, 如果没有找到对应的 NbtCompound 则设置并返回一个空 NbtCompound.
     *
     * @param key 要获取 NbtCompound 的 NBT键.
     * @return 待查找的 NbtCompound.
     */
    default @NotNull NbtCompound getOrCreateCompound(@NotNull String key) {
        Nbt<?> value = get(key);
        if (!(value instanceof NbtCompound)) {
            value = new NbtCompound();
            put(key, value);
        }
        return (NbtCompound) value;
    }

    /**
     * 根据 NBT键 获取对应的 NbtList, 如果没有找到对应的 NbtList 则返回 null.
     *
     * @param key 要获取 NbtList 的 NBT键.
     * @return 待查找的 NbtList.
     */
    default @Nullable NbtList getList(@NotNull String key) {
        return getList(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 NbtList, 如果没有找到对应的 NbtList 则返回默认值.
     *
     * @param key 要获取 NbtList 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 NbtList, 则返回的默认值.
     * @return 待查找的 NbtList.
     */
    default @Nullable NbtList getList(@NotNull String key, @Nullable NbtList def) {
        Nbt<?> value = get(key);
        return (value instanceof NbtList)
                ? (NbtList) value
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 NbtList, 如果没有找到对应的 NbtList 则设置并返回一个空 NbtList.
     *
     * @param key 要获取 NbtList 的 NBT键.
     * @return 待查找的 NbtList.
     */
    default @NotNull NbtList getOrCreateList(@NotNull String key) {
        Nbt<?> value = get(key);
        if (!(value instanceof NbtList)) {
            value = new NbtList();
            put(key, value);
        }
        return (NbtList) value;
    }

    default NbtList getList(String key, int elementType) {
        NbtList result = (NbtList) get(key);
        return (result.size() > 0 && result.get(0).getId() != elementType) ? new NbtList() : result;
    }

    /**
     * 根据 NBT键 获取对应的 boolean, 如果没有找到对应的 boolean 则返回 false.
     *
     * @param key 要获取 boolean 的 NBT键.
     * @return 待查找的 boolean.
     */
    default boolean getBoolean(@NotNull String key) {
        return getBoolean(key, false);
    }

    /**
     * 根据 NBT键 获取对应的 boolean, 如果没有找到对应的 boolean 则返回默认值.
     *
     * @param key 要获取 boolean 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 boolean, 则返回的默认值.
     * @return 待查找的 boolean.
     */
    default boolean getBoolean(@NotNull String key, boolean def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte() != 0
                : def;
    }

    default Nbt<?> getDeep(@NotNull String key) {
        String[] keys = StringUtils.split(key, '.');

        NbtComponentLike currentNbtCompound = this;
        Nbt<?> value = null;

        for (String k : keys) {
            if (currentNbtCompound == null) {
                return null;
            }
            if (currentNbtCompound.containsKey(k)) {
                Nbt<?> obj = currentNbtCompound.get(k);
                value = obj;
                if (obj instanceof NbtComponentLike) {
                    currentNbtCompound = (NbtComponentLike) obj;
                } else {
                    currentNbtCompound = null;
                }
            } else {
                return null;
            }
        }

        return value;
    }

    /**
     * 根据 NBT键 获取对应的 byte, 如果没有找到对应的 byte 则返回 (byte) 0.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 byte 的 NBT键.
     * @return 待查找的 byte.
     */
    default byte getDeepByte(@NotNull String key) {
        return getDeepByte(key, (byte) 0);
    }

    /**
     * 根据 NBT键 获取对应的 byte, 如果没有找到对应的 byte 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 byte 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 byte.
     */
    default byte getDeepByte(@NotNull String key, byte def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 short, 如果没有找到对应的 short 则返回 (short) 0.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 short 的 NBT键.
     * @return 待查找的 short.
     */
    default short getDeepShort(@NotNull String key) {
        return getDeepShort(key, (short) 0);
    }

    /**
     * 根据 NBT键 获取对应的 short, 如果没有找到对应的 short 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 short 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 short.
     */
    default short getDeepShort(@NotNull String key, short def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsShort()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 int, 如果没有找到对应的 int 则返回 0.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 int 的 NBT键.
     * @return 待查找的 int.
     */
    default int getDeepInt(@NotNull String key) {
        return getDeepInt(key, 0);
    }

    /**
     * 根据 NBT键 获取对应的 int, 如果没有找到对应的 int 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 int 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 int.
     */
    default int getDeepInt(@NotNull String key, int def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsInt()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 long, 如果没有找到对应的 long 则返回 0.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 long 的 NBT键.
     * @return 待查找的 long.
     */
    default long getDeepLong(@NotNull String key) {
        return getDeepLong(key, 0);
    }

    /**
     * 根据 NBT键 获取对应的 long, 如果没有找到对应的 long 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 long 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 long.
     */
    default long getDeepLong(@NotNull String key, long def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsLong()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 UUID, 如果没有找到对应的 UUID 则返回 null.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 UUID 的 NBT键.
     * @return 待查找的 UUID.
     */
    default @Nullable UUID getDeepUUID(@NotNull String key) {
        return getDeepUUID(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 UUID, 如果没有找到对应的 UUID 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 UUID 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 UUID, 则返回的默认值.
     * @return 待查找的 UUID.
     */
    default @Nullable UUID getDeepUUID(@NotNull String key, @Nullable UUID def) {
        Nbt<?> value = getDeep(key);
        if (value instanceof NbtIntArray) {
            int[] ints = ((NbtIntArray) value).getAsIntArray();
            if (ints.length == 4) {
                return new UUID((long) ints[0] << 32 | (long) ints[1] & 4294967295L, (long) ints[2] << 32 | (long) ints[3] & 4294967295L);
            }
        } else {
            Nbt<?> most = getDeep(key + "Most");
            Nbt<?> least = getDeep(key + "Least");
            if (most instanceof NbtNumeric<?> && least instanceof NbtNumeric<?>) {
                return new UUID(
                        ((NbtNumeric<?>) most).getAsLong(),
                        ((NbtNumeric<?>) least).getAsLong()
                );
            }
        }
        return def;
    }

    /**
     * 根据 NBT键 获取对应的 float, 如果没有找到对应的 float 则返回 0.0F.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 float 的 NBT键.
     * @return 待查找的 float.
     */
    default float getDeepFloat(@NotNull String key) {
        return getDeepFloat(key, 0.0F);
    }

    /**
     * 根据 NBT键 获取对应的 float, 如果没有找到对应的 float 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 float 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 float.
     */
    default float getDeepFloat(@NotNull String key, float def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsFloat()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 double, 如果没有找到对应的 double 则返回 0.0D.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 double 的 NBT键.
     * @return 待查找的 double.
     */

    default double getDeepDouble(@NotNull String key) {
        return getDeepDouble(key, 0.0D);
    }

    /**
     * 根据 NBT键 获取对应的 double, 如果没有找到对应的 double 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 double 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 数字, 则返回的默认值.
     * @return 待查找的 double.
     */
    default double getDeepDouble(@NotNull String key, double def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsDouble()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 String, 如果没有找到对应的 String 则返回 null.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 String 的 NBT键.
     * @return 待查找的 String.
     */
    default @Nullable String getDeepString(@NotNull String key) {
        return getDeepString(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 String, 如果没有找到对应的 String 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 String 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 String, 则返回的默认值.
     * @return 待查找的 String.
     */
    default @Nullable String getDeepString(@NotNull String key, @Nullable String def) {
        Nbt<?> value = getDeep(key);
        return (value != null)
                ? value.getAsString()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 byte[], 如果没有找到对应的 byte[] 则返回空数组.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 byte[] 的 NBT键.
     * @return 待查找的 byte[].
     */
    default byte[] getDeepByteArray(@NotNull String key) {
        return getDeepByteArray(key, ArrayUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * 根据 NBT键 获取对应的 byte[], 如果没有找到对应的 byte[] 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 byte[] 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 byte[], 则返回的默认值.
     * @return 待查找的 byte[].
     */
    default byte[] getDeepByteArray(@NotNull String key, byte[] def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtByteArray)
                ? ((NbtByteArray) value).getAsByteArray()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 int[], 如果没有找到对应的 int[] 则返回空数组.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 int[] 的 NBT键.
     * @return 待查找的 int[].
     */
    default int[] getDeepIntArray(@NotNull String key) {
        return getDeepIntArray(key, ArrayUtils.EMPTY_INT_ARRAY);
    }

    /**
     * 根据 NBT键 获取对应的 int[], 如果没有找到对应的 int[] 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 int[] 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 int[], 则返回的默认值.
     * @return 待查找的 int[].
     */
    default int[] getDeepIntArray(@NotNull String key, int[] def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtIntArray)
                ? ((NbtIntArray) value).getAsIntArray()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 long[], 如果没有找到对应的 long[] 则返回空数组.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 long[] 的 NBT键.
     * @return 待查找的 long[].
     */
    default long[] getDeepLongArray(@NotNull String key) {
        return getDeepLongArray(key, ArrayUtils.EMPTY_LONG_ARRAY);
    }

    /**
     * 根据 NBT键 获取对应的 long[], 如果没有找到对应的 long[] 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 long[] 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 long[], 则返回的默认值.
     * @return 待查找的 long[].
     */
    default long[] getDeepLongArray(@NotNull String key, long[] def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtLongArray)
                ? ((NbtLongArray) value).getAsLongArray()
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 NbtCompound, 如果没有找到对应的 NbtCompound 则返回 null.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 NbtCompound 的 NBT键.
     * @return 待查找的 NbtCompound.
     */
    default @Nullable NbtCompound getDeepCompound(@NotNull String key) {
        return getDeepCompound(key, null);
    }

    /**
     * 根据 NBT键 获取对应的 NbtCompound, 如果没有找到对应的 NbtCompound 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 NbtCompound 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 NbtCompound, 则返回的默认值.
     * @return 待查找的 NbtCompound.
     */
    default @Nullable NbtCompound getDeepCompound(@NotNull String key, @Nullable NbtCompound def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtCompound)
                ? (NbtCompound) value
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 NbtList, 如果没有找到对应的 NbtList 则返回 null.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 NbtList 的 NBT键.
     * @return 待查找的 NbtList.
     */
    default @Nullable NbtList getDeepList(@NotNull String key) {
        return getDeepList(key, null);
    }


    /**
     * 根据 NBT键 获取对应的 NbtList, 如果没有找到对应的 NbtList 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 NbtList 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 NbtList, 则返回的默认值.
     * @return 待查找的 NbtList.
     */
    default @Nullable NbtList getDeepList(@NotNull String key, @Nullable NbtList def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtList)
                ? (NbtList) value
                : def;
    }

    /**
     * 根据 NBT键 获取对应的 boolean, 如果没有找到对应的 boolean 则返回 false.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 boolean 的 NBT键.
     * @return 待查找的 boolean.
     */
    default boolean getDeepBoolean(@NotNull String key) {
        return getDeepBoolean(key, false);
    }

    /**
     * 根据 NBT键 获取对应的 boolean, 如果没有找到对应的 boolean 则返回默认值.
     * NBT键 以 . 做分隔符.
     *
     * @param key 要获取 boolean 的 NBT键.
     * @param def 如果找不到对应的 NBT 或对应的 NBT 不是 boolean, 则返回的默认值.
     * @return 待查找的 boolean.
     */
    default boolean getDeepBoolean(@NotNull String key, boolean def) {
        Nbt<?> value = getDeep(key);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte() != 0
                : def;
    }

    default void putDeep(@NotNull String key, @NotNull Nbt<?> value, boolean force) {
        String[] keys = StringUtils.split(key, '.');

        NbtComponentLike currentNbtCompound = this;

        for (int i = 0; i < keys.length; i++) {
            String k = keys[i];

            if (i == (keys.length - 1)) {
                currentNbtCompound.set(k, value); // is last node
            } else {
                if (currentNbtCompound.containsKey(k)) {
                    Nbt<?> obj = currentNbtCompound.get(k);
                    if (obj instanceof NbtComponentLike) {
                        currentNbtCompound = (NbtComponentLike) obj;
                    } else {
                        // 如果需要强制设置
                        if (force) {
                            obj = new NbtCompound();
                            currentNbtCompound.set(k, obj);
                            currentNbtCompound = (NbtComponentLike) obj;
                        } else {
                            return;
                        }
                    }
                } else {
                    NbtCompound obj = new NbtCompound();
                    currentNbtCompound.set(k, obj);
                    currentNbtCompound = obj;
                }
            }
        }
    }

    default void putDeep(@NotNull String key, @NotNull Nbt<?> value) {
        putDeep(key, value, false);
    }

    default void putDeepByte(@NotNull String key, byte value, boolean force) {
        putDeep(key, NbtByte.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepByte(@NotNull String key, byte value) {
        putDeepByte(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepShort(@NotNull String key, short value, boolean force) {
        putDeep(key, NbtShort.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepShort(@NotNull String key, short value) {
        putDeepShort(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepInt(@NotNull String key, int value, boolean force) {
        putDeep(key, NbtInt.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepInt(@NotNull String key, int value) {
        putDeepInt(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepLong(@NotNull String key, long value, boolean force) {
        putDeep(key, NbtLong.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepLong(@NotNull String key, long value) {
        putDeepLong(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepFloat(@NotNull String key, float value, boolean force) {
        putDeep(key, NbtFloat.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepFloat(@NotNull String key, float value) {
        putDeepFloat(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepDouble(@NotNull String key, double value, boolean force) {
        putDeep(key, NbtDouble.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepDouble(@NotNull String key, double value) {
        putDeepDouble(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepString(@NotNull String key, @NotNull String value, boolean force) {
        putDeep(key, NbtString.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepString(@NotNull String key, @NotNull String value) {
        putDeepString(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepByteArray(@NotNull String key, byte[] value, boolean force) {
        putDeep(key, new NbtByteArray(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepByteArray(@NotNull String key, byte[] value) {
        putDeepByteArray(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepByteArray(@NotNull String key, @NotNull List<Byte> value, boolean force) {
        putDeep(key, new NbtByteArray(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepByteArray(@NotNull String key, @NotNull List<Byte> value) {
        putDeepByteArray(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepIntArray(@NotNull String key, int[] value, boolean force) {
        putDeep(key, new NbtIntArray(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepIntArray(@NotNull String key, int[] value) {
        putDeepIntArray(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepIntArray(@NotNull String key, @NotNull List<Integer> value, boolean force) {
        putDeep(key, new NbtIntArray(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepIntArray(@NotNull String key, @NotNull List<Integer> value) {
        putDeepIntArray(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepLongArray(@NotNull String key, long[] value, boolean force) {
        putDeep(key, new NbtLongArray(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepLongArray(@NotNull String key, long[] value) {
        putDeepLongArray(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepLongArray(@NotNull String key, @NotNull List<Long> value, boolean force) {
        putDeep(key, new NbtLongArray(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepLongArray(@NotNull String key, @NotNull List<Long> value) {
        putDeepLongArray(key, value, false);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若 force 为 false 且对应的 key 无效, 则不进行设置.
     * 若 force 为 true 且对应的 key 无效, 则进行强制设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * 强制设置的例子: 你想要将 t1.t2.t3 设置为一个 1, t1.t2 的值不是 NbtCompound, 则强制将 t1.t2 的值设置为 new NbtCompound()
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     * @param force key 无效时是否强制设置.
     */
    default void putDeepBoolean(@NotNull String key, boolean value, boolean force) {
        putDeep(key, NbtByte.valueOf(value), force);
    }

    /**
     * 将指定的 NBT键 设置为给定值.
     * 若对应的 key 无效, 则不进行设置.
     * key 无效的例子: 你想要将 t1.t2.t3 设置为一个 1, 这需要 t1.t2 的值是 NbtCompound, 如果 t1.t2 的值不是 NbtCompound, 则 key 无效.
     * NBT键 以 . 做分隔符.
     *
     * @param key   待设置的 NBT键.
     * @param value 待设置的 NBT键 的新值.
     */
    default void putDeepBoolean(@NotNull String key, boolean value) {
        putDeepBoolean(key, value, false);
    }

    NbtComponentLike clone();
}
