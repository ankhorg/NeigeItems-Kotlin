package pers.neige.neigeitems.nms;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.NMSUtils;

import java.util.*;

public class ItemTagList extends ItemTagData implements List<ItemTagData> {
    private final AbstractList<Object> nbt;

    public AbstractList<Object> getNbt() {
        return this.nbt;
    }

    public ItemTagList() {
        super(NMSUtils.newItemTagList());
        this.nbt = (AbstractList<Object>)this.data;
    }

    public ItemTagList(AbstractList<Object> nbt) {
        super(nbt);
        this.nbt = nbt;
    }

    @Override
    public int size() {
        return nbt.size();
    }

    @Override
    public boolean isEmpty() {
        return nbt.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof ItemTagData) {
            return nbt.contains(((ItemTagData)o).data);
        }
        return false;
    }

    @NotNull
    @Override
    public Iterator<ItemTagData> iterator() {
        ArrayList<ItemTagData> tempList = new ArrayList<>();
        for (Object element : nbt) {
            tempList.add(new ItemTagData(element));
        }
        return tempList.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return nbt.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return nbt.toArray(a);
    }

    public boolean add(ItemTagData element) {
        return this.nbt.add(element.data);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof ItemTagData) {
            return nbt.remove(((ItemTagData)o).data);
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        ArrayList<Object> tempList = new ArrayList<>();
        for (Object o : c) {
            if (o instanceof ItemTagData) {
                tempList.add(((ItemTagData)o).data);
            }
        }
        return nbt.containsAll(tempList);
    }

    public void add(int index, ItemTagData element) {
        this.nbt.add(index, element.data);
    }

    @Override
    public ItemTagData remove(int index) {
        return new ItemTagData(nbt.remove(index));
    }

    @Override
    public int indexOf(Object o) {
        if (o instanceof ItemTagData) {
            return nbt.indexOf(((ItemTagData)o).data);
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o instanceof ItemTagData) {
            return nbt.lastIndexOf(((ItemTagData)o).data);
        }
        return -1;
    }

    @NotNull
    @Override
    public ListIterator<ItemTagData> listIterator() {
        ArrayList<ItemTagData> tempList = new ArrayList<>();
        for (Object element : nbt) {
            tempList.add(new ItemTagData(element));
        }
        return tempList.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<ItemTagData> listIterator(int index) {
        ArrayList<ItemTagData> tempList = new ArrayList<>();
        for (Object element : nbt) {
            tempList.add(new ItemTagData(element));
        }
        return tempList.listIterator(index);
    }

    @NotNull
    @Override
    public List<ItemTagData> subList(int fromIndex, int toIndex) {
        ArrayList<ItemTagData> tempList = new ArrayList<>();
        for (Object element : nbt.subList(fromIndex, toIndex)) {
            tempList.add(new ItemTagData(element));
        }
        return tempList;
    }

    public boolean addAll(@NotNull Collection<? extends ItemTagData> c) {
        List<Object> temp = new ArrayList<>();
        for (ItemTagData element : c) {
            temp.add(element.data);
        }
        return this.nbt.addAll(temp);
    }

    public boolean addAll(int index, @NotNull Collection<? extends ItemTagData> c) {
        List<Object> temp = new ArrayList<>();
        for (ItemTagData element : c) {
            temp.add(element.data);
        }
        return this.nbt.addAll(index, temp);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        List<Object> temp = new ArrayList<>();
        for (Object element : c) {
            if (element instanceof ItemTagData) {
                temp.add(((ItemTagData)element).data);
            } else {
                temp.add(element);
            }
        }
        return this.nbt.removeAll(temp);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        List<Object> temp = new ArrayList<>();
        for (Object element : c) {
            if (element instanceof ItemTagData) {
                temp.add(((ItemTagData)element).data);
            } else {
                temp.add(element);
            }
        }
        return this.nbt.retainAll(temp);
    }

    public void clear() {
        this.nbt.clear();
    }

    @Override
    public ItemTagData get(int index) {
        return new ItemTagData(nbt.get(index));
    }

    @Override
    public ItemTagData set(int index, ItemTagData element) {
        nbt.set(index, element.data);
        return element;
    }
}