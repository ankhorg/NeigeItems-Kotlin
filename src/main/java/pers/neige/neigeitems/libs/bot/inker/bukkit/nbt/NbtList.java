package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtListLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtBase;
import pers.neige.neigeitems.ref.nbt.RefNbtTagEnd;
import pers.neige.neigeitems.ref.nbt.RefNbtTagList;

public final class NbtList extends NbtCollection<RefNbtTagList, Nbt<?>> implements NbtListLike {
    private final static boolean THROW_INDEX_SUPPORT = CbVersion.v1_13_R1.isSupport();
    private final static boolean RETURN_SET_SUPPORT = CbVersion.v1_13_R1.isSupport();
    private final static boolean BOOLEAN_ADD_SUPPORT = CbVersion.v1_13_R1.isSupport();
    private final static boolean INDEX_ADD_SUPPORT = CbVersion.v1_14_R1.isSupport();

    NbtList(RefNbtTagList delegate) {
        super(delegate);
    }

    public NbtList() {
        super(new RefNbtTagList());
    }

    private static Nbt<?> nms2nbt(RefNbtBase nms) {
        return fromNms(nms);
    }

    private static RefNbtBase nbt2nms(Nbt<?> nbt) {
        return nbt == null ? null : nbt.delegate;
    }

    @Override
    public Nbt<?> get(int index) {
        RefNbtBase result = delegate.get(index);
        if (THROW_INDEX_SUPPORT && result instanceof RefNbtTagEnd && (index < 0 || index >= delegate.size())) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + delegate.size());
        }
        return nms2nbt(result);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Nbt<?> set(int index, Nbt<?> element) {
        if (RETURN_SET_SUPPORT) {
            return nms2nbt(delegate.set1(index, nbt2nms(element)));
        } else if (index >= 0 && index < delegate.size()) {
            Nbt<?> previous = nms2nbt(delegate.get(index));
            delegate.set0(index, nbt2nms(element));
            return previous;
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + delegate.size());
        }
    }

    @Override
    public void add(int index, Nbt<?> element) {
        if (INDEX_ADD_SUPPORT) {
            delegate.add2(index, nbt2nms(element));
        } else {
            int addedIndex = delegate.size();
            if (index > addedIndex) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + delegate.size());
            }
            RefNbtBase nms = nbt2nms(element);
            if (BOOLEAN_ADD_SUPPORT) {
                delegate.add1(nms);
            } else {
                delegate.add0(nms);
            }
            if (index == addedIndex) {
                return;
            }
            if (RETURN_SET_SUPPORT) {
                for (int i = index; i < addedIndex; i++) {
                    nms = delegate.set1(i, nms);
                }
                delegate.set1(addedIndex, nms);
            } else {
                for (int i = index; i < addedIndex; i++) {
                    RefNbtBase oldValue = delegate.get(i);
                    delegate.set0(i, nms);
                    nms = oldValue;
                }
                delegate.set0(addedIndex, nms);
            }
        }
    }

    @Override
    public Nbt<?> remove(int index) {
        return nms2nbt(delegate.remove(index));
    }

    @Override
    public NbtList clone() {
        return new NbtList(cloneNms());
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (delegate == o.delegate) return 0;
        if (o instanceof NbtList) {
            NbtList anotherList = (NbtList) o;
            int len1 = size();
            int len2 = anotherList.size();
            if (len1 != len2) return Integer.compare(len1, len2);

            int k = 0;
            while (k < len1) {
                Nbt<?> c1 = get(k);
                Nbt<?> c2 = anotherList.get(k);
                if (c1 != c2) {
                    int result = c1.compareTo(c2);
                    if (result != 0) {
                        return result;
                    }
                }
                k++;
            }
            return 0;
        }
        return super.compareTo(o);
    }
}
