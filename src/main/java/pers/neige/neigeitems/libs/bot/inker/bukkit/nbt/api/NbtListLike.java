package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.*;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader.ArrayUtils;

import java.util.List;

public interface NbtListLike extends NbtCollectionLike<Nbt<?>> {
    NbtListLike clone();

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtByte addByte(byte value) {
        NbtByte nbt = NbtByte.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtShort addShort(short value) {
        NbtShort nbt = NbtShort.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtInt addInt(int value) {
        NbtInt nbt = NbtInt.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtLong addLong(long value) {
        NbtLong nbt = NbtLong.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtFloat addFloat(float value) {
        NbtFloat nbt = NbtFloat.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtDouble addDouble(double value) {
        NbtDouble nbt = NbtDouble.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtString addString(@NonNull String value) {
        NbtString nbt = NbtString.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtByteArray addByteArray(byte @NonNull [] value) {
        NbtByteArray nbt = new NbtByteArray(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtByteArray addByteArray(@NonNull List<Byte> value) {
        NbtByteArray nbt = new NbtByteArray(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtIntArray addIntArray(int @NonNull [] value) {
        NbtIntArray nbt = new NbtIntArray(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtIntArray addIntArray(@NonNull List<Integer> value) {
        NbtIntArray nbt = new NbtIntArray(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtLongArray addLongArray(long @NonNull [] value) {
        NbtLongArray nbt = new NbtLongArray(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtLongArray addLongArray(@NonNull List<Long> value) {
        NbtLongArray nbt = new NbtLongArray(value);
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtByte addBoolean(boolean value) {
        NbtByte nbt = NbtByte.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 向列表的末尾添加一个 new NbtCompound().
     *
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtCompound addEmptyCompound() {
        NbtCompound nbt = new NbtCompound();
        add(nbt);
        return nbt;
    }

    /**
     * 向列表的末尾添加一个 new NbtList().
     *
     * @return 添加至列表末尾的 NBT实例.
     */
    default @NonNull NbtList addEmptyList() {
        NbtList nbt = new NbtList();
        add(nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByte addByte(int index, byte value) {
        NbtByte nbt = NbtByte.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtShort addShort(int index, short value) {
        NbtShort nbt = NbtShort.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtInt addInt(int index, int value) {
        NbtInt nbt = NbtInt.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtLong addLong(int index, long value) {
        NbtLong nbt = NbtLong.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtFloat addFloat(int index, float value) {
        NbtFloat nbt = NbtFloat.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtDouble addDouble(int index, double value) {
        NbtDouble nbt = NbtDouble.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtString addString(int index, @NonNull String value) {
        NbtString nbt = NbtString.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByteArray addByteArray(int index, byte @NonNull [] value) {
        NbtByteArray nbt = new NbtByteArray(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByteArray addByteArray(int index, @NonNull List<Byte> value) {
        NbtByteArray nbt = new NbtByteArray(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtIntArray addIntArray(int index, int @NonNull [] value) {
        NbtIntArray nbt = new NbtIntArray(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtIntArray addIntArray(int index, @NonNull List<Integer> value) {
        NbtIntArray nbt = new NbtIntArray(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtLongArray addLongArray(int index, long @NonNull [] value) {
        NbtLongArray nbt = new NbtLongArray(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtLongArray addLongArray(int index, @NonNull List<Long> value) {
        NbtLongArray nbt = new NbtLongArray(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后插入列表的指定位置.
     *
     * @param index 要插入指定元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByte addBoolean(int index, boolean value) {
        NbtByte nbt = NbtByte.valueOf(value);
        add(index, nbt);
        return nbt;
    }

    /**
     * 在列表的指定位置插入一个 new NbtCompound().
     *
     * @param index 要插入指定元素的索引.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtCompound addEmptyCompound(int index) {
        NbtCompound nbt = new NbtCompound();
        add(index, nbt);
        return nbt;
    }

    /**
     * 在列表的指定位置插入一个 new NbtList().
     *
     * @param index 要插入指定元素的索引.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtList addEmptyList(int index) {
        NbtList nbt = new NbtList();
        add(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByte setByte(int index, byte value) {
        NbtByte nbt = NbtByte.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtShort setShort(int index, short value) {
        NbtShort nbt = NbtShort.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtInt setInt(int index, int value) {
        NbtInt nbt = NbtInt.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtLong setLong(int index, long value) {
        NbtLong nbt = NbtLong.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtFloat setFloat(int index, float value) {
        NbtFloat nbt = NbtFloat.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtDouble setDouble(int index, double value) {
        NbtDouble nbt = NbtDouble.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtString setString(int index, @NonNull String value) {
        NbtString nbt = NbtString.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByteArray setByteArray(int index, byte @NonNull [] value) {
        NbtByteArray nbt = new NbtByteArray(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByteArray setByteArray(int index, @NonNull List<Byte> value) {
        NbtByteArray nbt = new NbtByteArray(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtIntArray setIntArray(int index, int @NonNull [] value) {
        NbtIntArray nbt = new NbtIntArray(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtIntArray setIntArray(int index, @NonNull List<Integer> value) {
        NbtIntArray nbt = new NbtIntArray(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtLongArray setLongArray(int index, long @NonNull [] value) {
        NbtLongArray nbt = new NbtLongArray(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtLongArray setLongArray(int index, @NonNull List<Long> value) {
        NbtLongArray nbt = new NbtLongArray(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将 value 转换为对应类型的 NBT实例 后替换指定位置的元素.
     *
     * @param index 待替换元素的索引.
     * @param value 待添加元素.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtByte setBoolean(int index, boolean value) {
        NbtByte nbt = NbtByte.valueOf(value);
        set(index, nbt);
        return nbt;
    }

    /**
     * 将指定位置的元素替换为 new NbtCompound().
     *
     * @param index 待替换元素的索引.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtCompound setEmptyCompound(int index) {
        NbtCompound nbt = new NbtCompound();
        set(index, nbt);
        return nbt;
    }

    /**
     * 将指定位置的元素替换为 new NbtList().
     *
     * @param index 待替换元素的索引.
     * @return 插入的 NBT实例.
     */
    default @NonNull NbtList setEmptyList(int index) {
        NbtList nbt = new NbtList();
        set(index, nbt);
        return nbt;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 0.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default byte getByte(int index) {
        return getByte(index, (byte) 0);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Byte getByteOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte()
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default byte getByte(int index, byte def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 0.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default short getShort(int index) {
        return getShort(index, (short) 0);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Short getShortOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsShort()
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default short getShort(int index, short def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsShort()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 0.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default int getInt(int index) {
        return getInt(index, 0);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Integer getIntOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsInt()
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default int getInt(int index, int def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsInt()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 0.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default long getLong(int index) {
        return getLong(index, 0);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Long getLongOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsLong()
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default long getLong(int index, long def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsLong()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 0.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default float getFloat(int index) {
        return getFloat(index, 0);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Float getFloatOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsFloat()
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default float getFloat(int index, float def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsFloat()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 0.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default double getDouble(int index) {
        return getDouble(index, 0);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Double getDoubleOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsDouble()
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default double getDouble(int index, double def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsDouble()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable String getString(int index) {
        return getString(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable String getStringOrNull(int index) {
        return getString(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    @Contract("_, !null -> !null")
    default @Nullable String getString(int index, @Nullable String def) {
        Nbt<?> value = get(index);
        return (value != null)
                ? value.getAsString()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回空数组.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default byte @NonNull [] getByteArray(int index) {
        return getByteArray(index, ArrayUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default byte @Nullable [] getByteArrayOrNull(int index) {
        return getByteArray(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    @Contract("_, !null -> !null")
    default byte @Nullable [] getByteArray(int index, byte @Nullable [] def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtByteArray)
                ? ((NbtByteArray) value).getAsByteArray()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回空数组.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default int @NonNull [] getIntArray(int index) {
        return getIntArray(index, ArrayUtils.EMPTY_INT_ARRAY);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default int @Nullable [] getIntArrayOrNull(int index) {
        return getIntArray(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    @Contract("_, !null -> !null")
    default int @Nullable [] getIntArray(int index, int @Nullable [] def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtIntArray)
                ? ((NbtIntArray) value).getAsIntArray()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回空数组.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default long @NonNull [] getLongArray(int index) {
        return getLongArray(index, ArrayUtils.EMPTY_LONG_ARRAY);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default long @Nullable [] getLongArrayOrNull(int index) {
        return getLongArray(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    @Contract("_, !null -> !null")
    default long @Nullable [] getLongArray(int index, long @Nullable [] def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtLongArray)
                ? ((NbtLongArray) value).getAsLongArray()
                : def;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable NbtCompound getCompound(int index) {
        return getCompound(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable NbtCompound getCompoundOrNull(int index) {
        return getCompound(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    @Contract("_, !null -> !null")
    default @Nullable NbtCompound getCompound(int index, @Nullable NbtCompound def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtCompound)
                ? (NbtCompound) value
                : def;
    }

    /**
     * 获取对应索引的 NbtCompound, 如果没有找到对应的 NbtCompound 则设置并返回一个空 NbtCompound.
     *
     * @param index 元素的索引.
     * @return 待查找的 NbtCompound.
     */
    default @NonNull NbtCompound getOrCreateCompound(int index) {
        Nbt<?> value = get(index);
        if (!(value instanceof NbtCompound)) {
            value = new NbtCompound();
            set(index, value);
        }
        return (NbtCompound) value;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable NbtList getList(int index) {
        return getList(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable NbtList getListOrNull(int index) {
        return getList(index, null);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    @Contract("_, !null -> !null")
    default @Nullable NbtList getList(int index, @Nullable NbtList def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtList)
                ? (NbtList) value
                : def;
    }

    /**
     * 获取对应索引的 NbtList, 如果没有找到对应的 NbtList 则设置并返回一个空 NbtList.
     *
     * @param index 元素的索引.
     * @return 待查找的 NbtList.
     */
    default @NonNull NbtList getOrCreateList(int index) {
        Nbt<?> value = get(index);
        if (!(value instanceof NbtList)) {
            value = new NbtList();
            set(index, value);
        }
        return (NbtList) value;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 false.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default boolean getBoolean(int index) {
        return getBoolean(index, false);
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回 null.
     *
     * @param index 元素的索引.
     * @return 对应索引的元素.
     */
    default @Nullable Boolean getBooleanOrNull(int index) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte() != 0
                : null;
    }

    /**
     * 获取对应索引的元素并尝试转换为对应类型, 越界或类型不符则返回默认值.
     *
     * @param index 元素的索引.
     * @param def   默认值.
     * @return 对应索引的元素.
     */
    default boolean getBoolean(int index, boolean def) {
        Nbt<?> value = get(index);
        return (value instanceof NbtNumeric<?>)
                ? ((NbtNumeric<?>) value).getAsByte() != 0
                : def;
    }
}
