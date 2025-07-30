package org.CustomQueue;

/**
 * @param <E>
 */
public interface Queue<E> {

    /**
     * Add item to end of queue
     * @param item to be added to queue
     * @return boolean true if added else false
     * @throws NullPointerException on null item
     */
    boolean enqueue(E item);

    /**
     * Offer item to end of queue
     * @param item to be added to queue
     * @return boolean true if added else false
     * @throws NullPointerException on null item
     */
    boolean offer(E item);

    /**
     * Remove item from front of queue
     * @return E item returned
     * @throws NullPointerException on null item
     */
    E remove();

    /**
     * Remove item from front of queue
     * @return E item returned
     * @throws NullPointerException on null item
     */
    E dequeue();

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