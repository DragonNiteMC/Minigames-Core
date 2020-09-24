package com.ericlam.mc.minigames.core.function;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 循環迭代器
 *
 * @param <E> 自定義類
 */
public final class CircularIterator<E> implements Iterator<E> {

    private final List<E> list;
    private int index = 0;

    public CircularIterator(Collection<E> list) {
        this.list = new LinkedList<>(list);
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public E next() {
        index = ++index > list.size() - 1 ? 0 : index;
        return list.get(index);
    }

    public boolean hasPrevious() {
        return !list.isEmpty();
    }

    public E previous() {
        index = --index < 0 ? list.size() - 1 : index;
        return list.get(index);
    }

    public int nextIndex() {
        return index + 1;
    }


    public int previousIndex() {
        return index - 1;
    }
}
