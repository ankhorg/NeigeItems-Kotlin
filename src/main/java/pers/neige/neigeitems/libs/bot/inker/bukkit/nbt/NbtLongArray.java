package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagLongArray;

import java.util.Arrays;
import java.util.List;

public final class NbtLongArray extends NbtCollection<RefNbtTagLongArray, Long> {
    private static final boolean DIRECT_ACCESS_SUPPORT = CbVersion.v1_13_R1.isSupport();

    NbtLongArray(RefNbtTagLongArray delegate) {
        super(delegate);
    }

    public NbtLongArray(long[] value) {
        super(new RefNbtTagLongArray(value));
    }

    public NbtLongArray(List<Long> value) {
        super(new RefNbtTagLongArray(value));
    }

    @Override
    public Long get(int index) {
        return getAsLongArray()[index];
    }

    public long getLong(int index) {
        return getAsLongArray()[index];
    }

    @Override
    public int size() {
        return getAsLongArray().length;
    }

    @Override
    public Long set(int index, Long element) {
        throw new UnsupportedOperationException("NbtLongArray is immutable");
    }

    @Override
    public void add(int index, Long element) {
        throw new UnsupportedOperationException("NbtLongArray is immutable");
    }

    @Override
    public Long remove(int index) {
        throw new UnsupportedOperationException("NbtLongArray is immutable");
    }

    public long[] getAsLongArray() {
        if (DIRECT_ACCESS_SUPPORT) {
            return delegate.getLongs();
        } else {
            return delegate.longs;
        }
    }

    @Override
    public String getAsString() {
        return Arrays.toString(getAsLongArray());
    }

    @Override
    public NbtLongArray clone() {
        return new NbtLongArray(cloneNms());
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (delegate == o.delegate) return 0;
        if (o instanceof NbtLongArray) {
            long[] v1 = getAsLongArray();
            long[] v2 = ((NbtLongArray) o).getAsLongArray();
            int len1 = v1.length;
            int len2 = v2.length;
            if (len1 != len2) return Integer.compare(len1, len2);

            int k = 0;
            while (k < len1) {
                long c1 = v1[k];
                long c2 = v2[k];
                if (c1 != c2) {
                    return Long.compare(c1, c2);
                }
                k++;
            }
            return 0;
        }
        return super.compareTo(o);
    }
}
