package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.ref.nbt.RefNbtTagByteArray;

import java.util.Arrays;
import java.util.List;

public final class NbtByteArray extends NbtCollection<RefNbtTagByteArray, Byte> {
    NbtByteArray(RefNbtTagByteArray delegate) {
        super(delegate);
    }

    public NbtByteArray(byte[] value) {
        super(new RefNbtTagByteArray(value));
    }

    public NbtByteArray(List<Byte> value) {
        super(new RefNbtTagByteArray(value));
    }

    @Override
    public Byte get(int index) {
        return delegate.getBytes()[index];
    }

    public byte getByte(int index) {
        return delegate.getBytes()[index];
    }

    @Override
    public int size() {
        return delegate.getBytes().length;
    }

    @Override
    public Byte set(int index, Byte element) {
        throw new UnsupportedOperationException("NbtByteArray is immutable");
    }

    @Override
    public void add(int index, Byte element) {
        throw new UnsupportedOperationException("NbtByteArray is immutable");
    }

    @Override
    public Byte remove(int index) {
        throw new UnsupportedOperationException("NbtByteArray is immutable");
    }

    public byte[] getAsByteArray() {
        return delegate.getBytes();
    }

    @Override
    public String getAsString() {
        return Arrays.toString(getAsByteArray());
    }

    @Override
    public NbtByteArray clone() {
        return new NbtByteArray(cloneNms());
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (delegate == o.delegate) return 0;
        if (o instanceof NbtByteArray) {
            byte[] v1 = getAsByteArray();
            byte[] v2 = ((NbtByteArray) o).getAsByteArray();
            int len1 = v1.length;
            int len2 = v2.length;
            if (len1 != len2) return Integer.compare(len1, len2);

            int k = 0;
            while (k < len1) {
                byte c1 = v1[k];
                byte c2 = v2[k];
                if (c1 != c2) {
                    return Byte.compare(c1, c2);
                }
                k++;
            }
            return 0;
        }
        return super.compareTo(o);
    }
}
