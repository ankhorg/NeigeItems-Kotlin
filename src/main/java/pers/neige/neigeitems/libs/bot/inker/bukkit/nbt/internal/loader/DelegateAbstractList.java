package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader;

import java.util.AbstractList;
import java.util.List;

public final class DelegateAbstractList<E> extends AbstractList<E> {
    private final List<E> delegate;

    public DelegateAbstractList(List<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public E set(int index, E element) {
        return delegate.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        delegate.add(index, element);
    }

    @Override
    public E remove(int index) {
        return delegate.remove(index);
    }
}