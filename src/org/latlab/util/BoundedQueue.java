/**
 * 
 */
package org.latlab.util;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A queue sorted by the given {@code comparator} bounded by a given size.
 * 
 * @author leonard
 * 
 */
public class BoundedQueue<T> extends AbstractQueue<T> {
    private final int maximumSize;
    private final List<T> list;
    private final Comparator<T> comparator;

    public BoundedQueue(int maximumSize, Comparator<T> comparator) {
        this.maximumSize = maximumSize;
        this.comparator = comparator;

        list = new ArrayList<T>(maximumSize);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }

    public boolean offer(T o) {
        // remove the last element if the list is full
        if (size() >= maximumSize) {
            list.remove(size() - 1);
        }

        // insert the new element by the sorted order
        int index = Collections.binarySearch(list, o, comparator);
        int position = index >= 0 ? index : -index - 1;
        list.add(position, o);

        return true;
    }

    public T peek() {
        return size() > 0 ? list.get(0) : null;

    }

    public T poll() {
        if (size() == 0)
            return null;

        return list.remove(0);
    }

    public T get(int index) {
        return list.get(index);
    }
}
