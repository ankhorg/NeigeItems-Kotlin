package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.ref.nbt.RefNbtTagIntArray;

import java.util.Arrays;
import java.util.List;

public final class NbtIntArray extends NbtCollection<RefNbtTagIntArray, Integer> {
    NbtIntArray(RefNbtTagIntArray delegate) {
        super(delegate);
    }

    public NbtIntArray(int[] value) {
        super(new RefNbtTagIntArray(value));
    }

    public NbtIntArray(List<Integer> value) {
        super(new RefNbtTagIntArray(value));
    }

    @Override
    public Integer get(int index) {
        return delegate.getInts()[index];
    }

    public int getInt(int index) {
        return delegate.getInts()[index];
    }

    @Override
    public int size() {
        return delegate.getInts().length;
    }

    @Override
    public Integer set(int index, Integer element) {
        throw new UnsupportedOperationException("NbtIntArray is immutable");
    }

    @Override
    public void add(int index, Integer element) {
        throw new UnsupportedOperationException("NbtIntArray is immutable");
    }

    @Override
    public Integer remove(int index) {
        throw new UnsupportedOperationException("NbtIntArray is immutable");
    }

    public int[] getAsIntArray() {
        return delegate.getInts();
    }

    @Override
    public String getAsString() {
        return Arrays.toString(getAsIntArray());
    }

    @Override
    public NbtIntArray clone() {
        return new NbtIntArray(cloneNms());
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (delegate == o.delegate) return 0;
        if (o instanceof NbtIntArray) {
            int[] v1 = getAsIntArray();
            int[] v2 = ((NbtIntArray) o).getAsIntArray();
            int len1 = v1.length;
            int len2 = v2.length;
            if (len1 != len2) return Integer.compare(len1, len2);

            int k = 0;
            while (k < len1) {
                int c1 = v1[k];
                int c2 = v2[k];
                if (c1 != c2) {
                    return Integer.compare(c1, c2);
                }
                k++;
            }
            return 0;
        }
        return super.compareTo(o);
    }
}
