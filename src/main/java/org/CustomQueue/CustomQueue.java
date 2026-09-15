package org.CustomQueue;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import static java.util.Arrays.fill;
import static java.util.Objects.requireNonNull;

public class CustomQueue<E> implements Queue<E> {

    private int modCount = 0;

    private Object[] queue;

    private int headIndex, tailIndex = 0;

    public CustomQueue() {
        this(16);
    }

    public CustomQueue(int size) {
        if(size < 0)
            throw new IllegalArgumentException();
        int capacity = 16;
        while (capacity < size && capacity > 0)
            capacity <<= 1;
        if (capacity <= 0)
            capacity = 1 << 30;
        this.queue = new Object[capacity];
    }

    public CustomQueue(Collection<? extends E> c) {
        requireNonNull(c);
        int capacity = 16;
        while (capacity < c.size() && capacity > 0)
            capacity <<= 1;
        if (capacity <= 0)
            capacity = 1 << 30;
        this.queue = new Object[capacity];
        for (E e : c) {
            requireNonNull(e);
            queue[tailIndex++] = e;
        }
    }

    public boolean add(E item) {
        requireNonNull(item);
        if (tailIndex >= queue.length)
            expand(size() + 1);
        queue[tailIndex++] = item;
        modCount++;
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        requireNonNull(c);
        if(c.isEmpty())
            return false;
        if(c == this)
            throw new IllegalArgumentException();
        for (E e : c)
            requireNonNull(e);
        int minimumCapacity = size() + c.size();
        if (tailIndex + c.size() > queue.length)
            expand(minimumCapacity);
        for(E e : c)
            queue[tailIndex++] = e;
        modCount++;
        return true;
    }

    public void clear() {
        while (headIndex < tailIndex)
            queue[headIndex++] = null;
        tailIndex = headIndex = 0;
        modCount++;
    }

    public boolean contains(Object o) {
        for(int i = headIndex; i < tailIndex; i++)
            if (queue[i].equals(o))
                return true;
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        requireNonNull(c);
        if (c.isEmpty())
            return true;
        Set<?> values = new HashSet<>(c);
        for (int i = headIndex; i < tailIndex; i++) {
            values.remove(queue[i]);
            if (values.isEmpty())
                return true;
        }
        return false;
    }

    public E element() {
        if(headIndex == tailIndex)
            throw new NoSuchElementException();
        return (E) queue[headIndex];
    }

    public boolean isEmpty() {
        return headIndex == tailIndex;
    }

    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    public boolean offer(E item) {
        return add(item);
    }

    public E peek() {
        if (isEmpty())
            return null;
        return (E) queue[headIndex];
    }

    public E poll() {
        if(size() == 0)
            return null;
        return remove();
    }

    public E remove() {
        if(isEmpty())
            throw new NoSuchElementException();
        E e = (E) queue[headIndex];
        queue[headIndex++] = null;
        modCount++;
        return e;
    }

    public boolean remove(Object o) {
        if (o == null || isEmpty())
            return false;
        for (int i = headIndex; i < tailIndex; i++)
            if (o.equals(queue[i])) {
                removeAt(i);
                return true;
            }
        return false;
    }



    public boolean removeAll(Collection<?> c) {
        requireNonNull(c);
        if (isEmpty() || c.isEmpty())
            return false;
        if (c == this) {
            clear();
            return true;
        }
        Set<?> values = (c instanceof Set<?> s) ? s : new HashSet<>(c);
        int index = headIndex;
        boolean modified = false;
        for(int x = headIndex; x < tailIndex; x++)
            if (!values.contains(queue[x]))
                queue[index++] = queue[x];
            else
                modified = true;
        nullIndexes(index);
        return modified;
    }

    public boolean retainAll(Collection<?> c) {
        requireNonNull(c);
        if(isEmpty() || c == this)
            return false;
        java.util.Set<?> set = (c instanceof java.util.Set) ? (java.util.Set<?>) c : new java.util.HashSet<>(c);
        boolean modified = false;
        int index = headIndex;
        for (int i = headIndex; i < tailIndex; i++)
            if (set.contains(queue[i]))
                queue[index++] = queue[i];
            else
                modified = true;
        nullIndexes(index);
        return modified;
    }

    public int size() {
        return (tailIndex - headIndex);
    }

    public Object[] toArray() {
        return Arrays.copyOfRange(queue, headIndex, tailIndex);
    }

    public <T> T[] toArray(T[] a) {
        requireNonNull(a);
        int size = size();
        if (a.length < size)
            return (T[]) Arrays.copyOfRange(queue, headIndex, tailIndex, a.getClass());
        System.arraycopy(queue, headIndex, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(queue, headIndex, tailIndex, Object[].class));
    }

    private void expand(int minCapacity) {
        int currentSize = tailIndex - headIndex;
        if (currentSize < minCapacity && (queue.length - currentSize) >= (minCapacity - currentSize)) {
            System.arraycopy(queue, headIndex, queue, 0, currentSize);
            fill(queue, currentSize, tailIndex, null);
            this.headIndex = 0;
            this.tailIndex = currentSize;
            return;
        }
        int oldCapacity = queue.length;
        int newCapacity = (oldCapacity == 0) ? 16 : oldCapacity << 1;
        while (newCapacity < minCapacity && newCapacity > 0)
            newCapacity <<= 1;
        if (newCapacity <= 0)
            newCapacity = 1 << 30;
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(queue, headIndex, newArray, 0, currentSize);
        this.queue = newArray;
        this.headIndex = 0;
        this.tailIndex = currentSize;
    }

    private void nullIndexes(int index) {
        if (index < tailIndex) {
            fill(queue, index, tailIndex, null);
            tailIndex = index;
            modCount++;
        }
    }

    private void removeAt(int i) {
        int numMoved = tailIndex - i - 1;
        if (numMoved > 0)
            System.arraycopy(queue, i + 1, queue, i, numMoved);
        queue[--tailIndex] = null;
        modCount++;
    }

    private class QueueIterator implements Iterator<E> {
        private int cursor = headIndex;
        private int lastRet = -1;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor < tailIndex;
        }

        @Override
        public E next() {
            checkForComodification();
            if (cursor >= tailIndex) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return (E) queue[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            removeAt(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
