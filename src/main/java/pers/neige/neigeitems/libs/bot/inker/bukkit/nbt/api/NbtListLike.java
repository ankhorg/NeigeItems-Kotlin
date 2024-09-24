package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api;

import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.*;

import java.util.List;

public interface NbtListLike extends NbtCollectionLike<Nbt<?>> {
    NbtListLike clone();

    /**
     * 将 value 转换为对应类型的 NBT实例 后添加至列表的末尾.
     *
     * @param value 待添加元素.
     * @return 添加至列表末尾的 NBT实例.
     */
    default NbtByte addByte(byte value) {
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
    default NbtShort addShort(short value) {
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
    default NbtInt addInt(int value) {
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
    default NbtLong addLong(long value) {
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
    default NbtFloat addFloat(float value) {
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
    default NbtDouble addDouble(double value) {
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
    default NbtString addString(String value) {
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
    default NbtByteArray addByteArray(byte[] value) {
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
    default NbtByteArray addByteArray(List<Byte> value) {
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
    default NbtIntArray addIntArray(int[] value) {
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
    default NbtIntArray addIntArray(List<Integer> value) {
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
    default NbtLongArray addLongArray(long[] value) {
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
    default NbtLongArray addLongArray(List<Long> value) {
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
    default NbtByte addBoolean(boolean value) {
        NbtByte nbt = NbtByte.valueOf(value);
        add(nbt);
        return nbt;
    }

    /**
     * 向列表的末尾添加一个 new NbtCompound().
     *
     * @return 添加至列表末尾的 NBT实例.
     */
    default NbtCompound addEmptyCompound() {
        NbtCompound nbt = new NbtCompound();
        add(nbt);
        return nbt;
    }

    /**
     * 向列表的末尾添加一个 new NbtList().
     *
     * @return 添加至列表末尾的 NBT实例.
     */
    default NbtList addEmptyList() {
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
    default NbtByte addByte(int index, byte value) {
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
    default NbtShort addShort(int index, short value) {
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
    default NbtInt addInt(int index, int value) {
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
    default NbtLong addLong(int index, long value) {
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
    default NbtFloat addFloat(int index, float value) {
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
    default NbtDouble addDouble(int index, double value) {
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
    default NbtString addString(int index, String value) {
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
    default NbtByteArray addByteArray(int index, byte[] value) {
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
    default NbtByteArray addByteArray(int index, List<Byte> value) {
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
    default NbtIntArray addIntArray(int index, int[] value) {
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
    default NbtIntArray addIntArray(int index, List<Integer> value) {
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
    default NbtLongArray addLongArray(int index, long[] value) {
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
    default NbtLongArray addLongArray(int index, List<Long> value) {
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
    default NbtByte addBoolean(int index, boolean value) {
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
    default NbtCompound addEmptyCompound(int index) {
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
    default NbtList addEmptyList(int index) {
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
    default NbtByte setByte(int index, byte value) {
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
    default NbtShort setShort(int index, short value) {
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
    default NbtInt setInt(int index, int value) {
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
    default NbtLong setLong(int index, long value) {
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
    default NbtFloat setFloat(int index, float value) {
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
    default NbtDouble setDouble(int index, double value) {
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
    default NbtString setString(int index, String value) {
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
    default NbtByteArray setByteArray(int index, byte[] value) {
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
    default NbtByteArray setByteArray(int index, List<Byte> value) {
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
    default NbtIntArray setIntArray(int index, int[] value) {
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
    default NbtIntArray setIntArray(int index, List<Integer> value) {
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
    default NbtLongArray setLongArray(int index, long[] value) {
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
    default NbtLongArray setLongArray(int index, List<Long> value) {
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
    default NbtByte setBoolean(int index, boolean value) {
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
    default NbtCompound setEmptyCompound(int index) {
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
    default NbtList setEmptyList(int index) {
        NbtList nbt = new NbtList();
        set(index, nbt);
        return nbt;
    }
}
