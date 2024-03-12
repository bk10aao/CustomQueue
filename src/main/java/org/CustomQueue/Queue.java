package org.CustomQueue;

/**
 * @param <T>
 */
public interface Queue<T> {

    /**
     * Add item to end of queue
     * @param item to be added to queue
     * @return boolean true if added else false
     * @throws NullPointerException on null item
     */
    boolean enqueue(T item);

    /**
     * Offer item to end of queue
     * @param item to be added to queue
     * @return boolean true if added else false
     * @throws NullPointerException on null item
     */
    boolean offer(T item);

    /**
     * Remove item from front of queue
     * @return T item returned
     * @throws NullPointerException on null item
     */
    T remove();

    /**
     * Remove item from front of queue
     * @return T item returned
     * @throws NullPointerException on null item
     */
    T dequeue();

    /**
     * Get size of queue
     * @return Integer of size queue
     */
    int getSize();

    /**
     * Get size of queue including null elements
     * @return Integer of size queue including null elements
     */
    int getQueueSize();
}