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

/**
 * A resizable, array-backed implementation of the {@link Queue} interface.
 *
 * <p>Elements are stored in a backing {@code Object[]} array between a
 * {@code headIndex} (inclusive) and a {@code tailIndex} (exclusive). The
 * head index advances as elements are removed from the front of the queue,
 * and the tail index advances as elements are added to the back. When the
 * array runs out of room at the tail, it is either compacted (if enough
 * free space exists at the head) or grown to a larger power-of-two capacity.
 *
 * <p>This implementation does <b>not</b> permit {@code null} elements;
 * most mutating methods will throw a {@link NullPointerException} if given
 * a {@code null} argument or a collection containing {@code null}.
 *
 * <p>This class is <b>not</b> thread-safe. If multiple threads access a
 * {@code CustomQueue} instance concurrently, and at least one of the
 * threads modifies it structurally, it must be synchronized externally.
 *
 * <p>The {@link Iterator} returned by {@link #iterator()} is
 * <i>fail-fast</i>: if the queue is structurally modified at any time
 * after the iterator is created, in any way except through the iterator's
 * own {@link Iterator#remove()} method, the iterator will throw a
 * {@link ConcurrentModificationException}.
 *
 * @author Benjamin Kane
 * LinkedIn - <a href="https://www.linkedin.com/in/benjamin-kane-81149482/"/>
 * GitHub account bk10aao - <a href="https://github.com/bk10aao"/>
 * Repository - <a href="https://github.com/bk10aao/CustomQueue"/>
 *
 * @param <E> the type of elements held in this queue
 */
public class CustomQueue<E> implements Queue<E> {

    /**
     * The number of times this queue has been structurally modified.
     * Used by {@link QueueIterator} to implement fail-fast behavior.
     */
    private int modCount = 0;

    /**
     * The backing array in which the elements of the queue are stored.
     * Its length is always a power of two (except for the capacity-overflow
     * edge case of {@code 1 << 30}).
     */
    private Object[] queue;

    /**
     * Index of the first (head) element of the queue, and index one past
     * the last (tail) element of the queue. Elements occupy the half-open
     * range {@code [headIndex, tailIndex)} of {@link #queue}.
     */
    private int headIndex, tailIndex = 0;

    /**
     * Constructs an empty queue with a default initial capacity of 16.
     */
    public CustomQueue() {
        this(16);
    }

    /**
     * Constructs an empty queue with an initial capacity sufficient to
     * hold at least {@code size} elements. The actual backing array
     * capacity is rounded up to the next power of two, with a minimum
     * of 16.
     *
     * @param size the desired minimum initial capacity
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public CustomQueue(final int size) {
        if (size < 0)
            throw new IllegalArgumentException();
        int capacity = 16;
        while (capacity < size && capacity > 0)
            capacity <<= 1;
        if (capacity <= 0)
            capacity = 1 << 30;
        this.queue = new Object[capacity];
    }

    /**
     * Constructs a queue containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this queue
     * @throws NullPointerException if the specified collection is {@code null},
     *         or if any element of the collection is {@code null}
     */
    public CustomQueue(final Collection<? extends E> c) {
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

    /**
     * Inserts the specified element at the tail of this queue, expanding
     * the backing array if necessary.
     *
     * @param item the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws NullPointerException if the specified element is {@code null}
     */
    public boolean add(final E item) {
        requireNonNull(item);
        if (tailIndex >= queue.length)
            expand(size() + 1);
        queue[tailIndex++] = item;
        modCount++;
        return true;
    }

    /**
     * Appends all the elements in the specified collection to the tail of
     * this queue, in the order that they are returned by the specified
     * collection's iterator. The backing array is expanded if necessary.
     *
     * @param c the collection containing elements to be added to this queue
     * @return {@code true} if this queue changed as a result of the call
     * @throws NullPointerException if the specified collection is {@code null},
     *         or if any element of the collection is {@code null}
     * @throws IllegalArgumentException if the specified collection is this queue
     */
    public boolean addAll(final Collection<? extends E> c) {
        requireNonNull(c);
        if (c.isEmpty())
            return false;
        if (c == this)
            throw new IllegalArgumentException();
        for (E e : c)
            requireNonNull(e);
        int minimumCapacity = size() + c.size();
        if (tailIndex + c.size() > queue.length)
            expand(minimumCapacity);
        for (E e : c)
            queue[tailIndex++] = e;
        modCount++;
        return true;
    }

    /**
     * Removes all the elements from this queue. The queue will be empty
     * after this call returns. All references held in the backing array
     * are cleared to allow garbage collection.
     */
    public void clear() {
        while (headIndex < tailIndex)
            queue[headIndex++] = null;
        tailIndex = headIndex = 0;
        modCount++;
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue
     * contains at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o element whose presence in this queue is to be tested
     * @return {@code true} if this queue contains the specified element
     * @throws NullPointerException if this queue contains a {@code null}
     *         slot before finding a match (not expected under normal use)
     */
    public boolean contains(final Object o) {
        for (int i = headIndex; i < tailIndex; i++)
            if (queue[i].equals(o))
                return true;
        return false;
    }

    /**
     * Returns {@code true} if this queue contains all the elements of the
     * specified collection.
     *
     * @param c the collection to be checked for containment in this queue
     * @return {@code true} if this queue contains all the elements of the
     *         specified collection
     * @throws NullPointerException if the specified collection is {@code null}
     */
    public boolean containsAll(final Collection<?> c) {
        requireNonNull(c);
        if (c.isEmpty())
            return true;
        Set<?> values = (c instanceof Set) ? (Set<?>) c : new HashSet<>(c);
        for (int i = headIndex; i < tailIndex; i++) {
            values.remove(queue[i]);
            if (values.isEmpty())
                return true;
        }
        return false;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * <p>Unlike {@link #peek()}, this method throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E element() {
        if (headIndex == tailIndex)
            throw new NoSuchElementException();
        return (E) queue[headIndex];
    }

    /**
     * Returns {@code true} if this queue contains no elements.
     *
     * @return {@code true} if this queue contains no elements
     */
    public boolean isEmpty() {
        return headIndex == tailIndex;
    }

    /**
     * Returns an iterator over the elements in this queue, in proper
     * sequence (from head to tail).
     *
     * <p>The returned iterator is fail-fast: it will throw a
     * {@link ConcurrentModificationException} if the queue is
     * structurally modified after the iterator is created, except through
     * the iterator's own {@link Iterator#remove()} method.
     *
     * @return an iterator over the elements in this queue in proper sequence
     */
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    /**
     * Inserts the specified element at the tail of this queue.
     *
     * <p>This implementation behaves identically to {@link #add(Object)};
     * since this queue has no fixed capacity limit (it grows as needed),
     * this method never returns {@code false}.
     *
     * @param item the element to add
     * @return {@code true} (as specified by {@link Queue#offer})
     * @throws NullPointerException if the specified element is {@code null}
     */
    public boolean offer(final E item) {
        return add(item);
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns
     * {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E peek() {
        if (isEmpty())
            return null;
        return (E) queue[headIndex];
    }

    /**
     * Retrieves and removes the head of this queue, or returns
     * {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E poll() {
        if (size() == 0)
            return null;
        return remove();
    }

    /**
     * Retrieves and removes the head of this queue.
     *
     * <p>Unlike {@link #poll()}, this method throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E remove() {
        if (isEmpty())
            throw new NoSuchElementException();
        E e = (E) queue[headIndex];
        queue[headIndex++] = null;
        modCount++;
        return e;
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present. More formally, removes the first element {@code e}
     * such that {@code o.equals(e)}, if such an element exists, shifting
     * all subsequent elements one position toward the head.
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue contained the specified element
     */
    public boolean remove(final Object o) {
        if (o == null || isEmpty())
            return false;
        for (int i = headIndex; i < tailIndex; i++)
            if (o.equals(queue[i])) {
                removeAt(i);
                return true;
            }
        return false;
    }

    /**
     * Removes from this queue all of its elements that are contained in
     * the specified collection.
     *
     * @param c collection containing elements to be removed from this queue
     * @return {@code true} if this queue changed as a result of the call
     * @throws NullPointerException if the specified collection is {@code null}
     */
    public boolean removeAll(final Collection<?> c) {
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
        for (int x = headIndex; x < tailIndex; x++)
            if (!values.contains(queue[x]))
                queue[index++] = queue[x];
            else
                modified = true;
        nullIndexes(index);
        return modified;
    }

    /**
     * Retains only the elements in this queue that are contained in the
     * specified collection. In other words, removes from this queue all
     * of its elements that are not contained in the specified collection.
     *
     * @param c collection containing elements to be retained in this queue
     * @return {@code true} if this queue changed as a result of the call
     * @throws NullPointerException if the specified collection is {@code null}
     */
    public boolean retainAll(final Collection<?> c) {
        requireNonNull(c);
        if (isEmpty() || c == this)
            return false;
        Set<?> set = (c instanceof Set) ? (Set<?>) c : new HashSet<>(c);
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

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return tailIndex - headIndex;
    }

    /**
     * Returns an array containing all the elements in this queue in
     * proper sequence (from head to tail). The returned array is a
     * newly allocated copy; the caller is free to modify it without
     * affecting this queue.
     *
     * @return an array containing all the elements in this queue
     */
    public Object[] toArray() {
        return Arrays.copyOfRange(queue, headIndex, tailIndex);
    }

    /**
     * Returns an array containing all the elements in this queue in
     * proper sequence (from head to tail); the runtime type of the
     * returned array is that of the specified array.
     *
     * <p>If the queue fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this queue. If the queue fits in
     * the specified array with room to spare (i.e., the array has more
     * elements than the queue), the element immediately following the
     * end of the queue is set to {@code null}.
     *
     * @param a the array into which the elements of this queue are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose
     * @param <T> the runtime type of the array to contain the queue
     * @return an array containing all the elements in this queue
     * @throws NullPointerException if the specified array is {@code null}
     */
    public <T> T[] toArray(final T[] a) {
        requireNonNull(a);
        int size = size();
        if (a.length < size)
            return (T[]) Arrays.copyOfRange(queue, headIndex, tailIndex, a.getClass());
        System.arraycopy(queue, headIndex, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    /**
     * Returns a string representation of this queue. The string
     * representation consists of a list of the queue's elements in the
     * order they are returned by its iterator, enclosed in square
     * brackets ({@code "[]"}). Adjacent elements are separated by the
     * characters {@code ", "} (comma and space).
     *
     * @return a string representation of this queue
     */
    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(queue, headIndex, tailIndex, Object[].class));
    }

    /**
     * Ensures that the backing array has room for at least
     * {@code minCapacity} elements, either by compacting the existing
     * array (shifting elements down to index 0 to reclaim space freed by
     * prior removals) or, if compaction is insufficient, by allocating a
     * new array of the next power-of-two capacity greater than or equal
     * to {@code minCapacity} and copying the existing elements into it.
     *
     * <p>After this method returns, {@code headIndex} is always 0.
     *
     * @param minCapacity the minimum number of elements the backing array
     *                     must be able to hold
     */
    private void expand(final int minCapacity) {
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

    /**
     * Nulls out backing-array slots from {@code index} (inclusive) up to
     * the current {@code tailIndex} (exclusive), and moves
     * {@code tailIndex} to {@code index}. Used after a bulk removal
     * operation (such as {@link #removeAll(Collection)} or
     * {@link #retainAll(Collection)}) has compacted the surviving
     * elements to the front of the live range, to clear the now-stale
     * trailing references and shrink the logical size accordingly.
     *
     * @param index the new tail index; slots from this index to the old
     *              tail index are cleared
     */
    private void nullIndexes(final int index) {
        if (index < tailIndex) {
            fill(queue, index, tailIndex, null);
            tailIndex = index;
            modCount++;
        }
    }

    /**
     * Removes the element at backing-array position {@code i}, shifting
     * all subsequent elements (up to {@code tailIndex}) one position
     * toward the head to close the gap, and decrementing {@code tailIndex}.
     *
     * @param i the backing-array index of the element to remove
     */
    private void removeAt(final int i) {
        int numMoved = tailIndex - i - 1;
        if (numMoved > 0)
            System.arraycopy(queue, i + 1, queue, i, numMoved);
        queue[--tailIndex] = null;
        modCount++;
    }

    /**
     * A fail-fast iterator over the elements of the enclosing
     * {@link CustomQueue}, traversing from head to tail.
     */
    private class QueueIterator implements Iterator<E> {

        /**
         * Index of the next element to be returned by {@link #next()}.
         */
        private int cursor = headIndex;

        /**
         * Index of the element most recently returned by {@link #next()},
         * or {@code -1} if no element has been returned yet or the last
         * returned element has already been removed.
         */
        private int lastRet = -1;

        /**
         * The value that {@link CustomQueue#modCount} is expected to have
         * throughout iteration. If this expectation is violated, the
         * iterator has detected concurrent modification.
         */
        private int expectedModCount = modCount;

        /**
         * Returns {@code true} if the iteration has more elements.
         *
         * @return {@code true} if there is another element to return
         */
        @Override
        public boolean hasNext() {
            return cursor < tailIndex;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own {@link #remove()}
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            checkForComodification();
            if (cursor >= tailIndex) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return (E) queue[cursor++];
        }

        /**
         * Removes from the underlying queue the last element returned by
         * this iterator.
         *
         * @throws IllegalStateException if the {@link #next()} method has
         *         not yet been called, or the {@code remove} method has
         *         already been called after the last call to {@code next()}
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own {@code remove()}
         */
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

        /**
         * Checks that the enclosing queue has not been structurally
         * modified since this iterator was created (other than through
         * this iterator's own {@link #remove()} method).
         *
         * @throws ConcurrentModificationException if a structural
         *         modification is detected
         */
        private void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}
