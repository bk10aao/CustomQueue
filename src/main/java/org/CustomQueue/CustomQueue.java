package org.CustomQueue;

import java.util.NoSuchElementException;

public class CustomQueue<T> {
    private T[] queue;

    private int size = 32;
    private int headIndex = 0;
    private int tailIndex = 0;

    CustomQueue() {
        queue = (T[]) new Object[size];
    }

    CustomQueue(int size) {
        this.size = size;
        queue = (T[]) new Object[size];
    }

    public boolean add(T item) {
        if(item == null) throw new NullPointerException();
        if(tailIndex == size) expand();
        queue[tailIndex++] = item;
        return true;
    }

    public boolean offer(T item) {
        if(item == null) throw new NullPointerException();
        if(tailIndex == size) return false;
        queue[tailIndex++] = item;
        return true;
    }

    public T remove() {
        if(queue[headIndex] == null) throw new NoSuchElementException();
        T item = queue[headIndex++];
        if((tailIndex - headIndex) == queue.length / 4) reduce();
        return item;
    }

    public T poll() {
        if(queue[headIndex] == null) return null;
        T item = queue[headIndex++];
        if((tailIndex - headIndex) == queue.length / 4) reduce();
        return item;
    }

    public int getSize() {
        return tailIndex - headIndex;
    }

    public int getQueueSize() {
        return queue.length;
    }

    private void expand() {
        size = tailIndex * 2;
        T[] temp = (T[]) new Object[size];
        System.arraycopy(queue, headIndex, temp, 0, size / 2);
        headIndex = 0;
        queue = temp;
    }

    private void reduce() {
        if((tailIndex - headIndex) == queue.length / 4) {
            T[] temp = (T[]) new Object[(tailIndex - headIndex) * 2];
            for(int i = headIndex, insertIndex = 0; headIndex <= tailIndex; headIndex++, insertIndex++) {
                temp[insertIndex] = queue[i];
            }
            tailIndex = headIndex - tailIndex + 1;
            headIndex = 0;
            queue = temp;
        }
    }
}