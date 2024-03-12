package org.CustomQueue;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomQueueTest {

    @Test
    public void givenDefaultQueue_returns_sizeOf_32() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertEquals(32, customQueue.getQueueSize());
    }

    @Test
    public void givenParametizedConstructor_ofSize_10_returns_sizeOf_10() {
        CustomQueue<Object> customQueue = new CustomQueue<>(10);
        assertEquals(10, customQueue.getQueueSize());
    }

    @Test
    public void givenDefaultQueue_returns_NullPointerException_whenAdding_null() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertThrows(NullPointerException.class, ()-> customQueue.enqueue(null));
    }

    @Test
    public void givenQueue_ofSize_10_returns_NullPointerException_whenAdding_null() {
        CustomQueue<Object> customQueue = new CustomQueue<>(10);
        assertThrows(NullPointerException.class, ()-> customQueue.enqueue(null));
    }

    @Test
    public void givenDefaultQueue_returns_true_whenAdding_valueOf_10() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertTrue(customQueue.enqueue(10));
        assertEquals(1, customQueue.getSize());
    }

    @Test
    public void givenDefaultQueue_returns_true_whenAdding_10() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        Object item = 10;
        assertTrue(customQueue.enqueue(item));
    }

    @Test
    public void givenQueueOfSize_2_whenAdding_10_values_returnsQueueSizeOf_16() {
        CustomQueue<Object> customQueue = new CustomQueue<>(2);
        Object item = 10;
        for(int i = 0; i < 10; i++)
            customQueue.enqueue(item);
        assertEquals(16, customQueue.getQueueSize());
    }

    @Test
    public void givenDefaultQueue_whenAdding_32_values_returnsQueueSizeOf_64() {
        CustomQueue<Object> customQueue = new CustomQueue<>(2);
        Object item = 10;
        for(int i = 0; i < 33; i++)
            customQueue.enqueue(item);
        assertEquals(64, customQueue.getQueueSize());
    }

    @Test
    public void givenDefaultQueue_returns_NullPointerException_whenOfferingNullItem() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertThrows(NullPointerException.class, ()-> customQueue.offer(null));
    }

    @Test
    public void givenDefaultQueue_returns_true_whenOffering_whenQueueHasSpace() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertTrue(customQueue.offer(10));
    }

    @Test
    public void givenDefaultQueue_returns_false_whenOffering_whenQueueIsFull() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        Object item = 10;
        for(int i = 0; i < 32; i++)
            customQueue.enqueue(item);
        assertFalse(customQueue.offer(item));
    }

    @Test
    public void givenQueueOfSize_0_returns_false_whenOffering_10() {
        CustomQueue<Object> customQueue = new CustomQueue<>(0);
        assertFalse(customQueue.offer(10));
    }

    @Test
    public void givenQueueOfSize_10_returns_false_whenOffering_whenQueueIsFull() {
        CustomQueue<Object> customQueue = new CustomQueue<>(10);
        Object item = 10;
        for(int i = 0; i < 10; i++)
            customQueue.enqueue(item);
        assertFalse(customQueue.offer(10));
    }

    @Test
    public void givenDefaultQueue_returns_true_whenOffering_whenQueueIsNotFull() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertTrue(customQueue.offer(10));
    }

    @Test
    public void givenQueueOfSize_10_returns_true_whenOffering_whenQueueIsNotFull() {
        CustomQueue<Object> customQueue = new CustomQueue<>(10);
        assertTrue(customQueue.offer(10));
    }

    @Test
    public void givenDefaultQueue_is_empty_onRemove_returns_NoSuchElementException() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertThrows(NoSuchElementException.class, customQueue::remove);
    }

    @Test
    public void givenDefaultQueue_which_contains_oneElementOf_10_returns_1_withSizeOf_0_on_remove() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        Object item = 10;
        customQueue.enqueue(item);
        assertEquals(item, customQueue.remove());
        assertEquals(0, customQueue.getSize());
    }

    @Test
    public void givenQueueOfSize_10_which_contains_oneElementOf_10_returns_10_withSizeOf_0_on_remove() {
        CustomQueue<Object> customQueue = new CustomQueue<>(10);
        Object item = 10;
        customQueue.enqueue(item);
        assertEquals(item, customQueue.remove());
        assertEquals(0, customQueue.getSize());
    }

    @Test
    public void givenQueueOfSize_8_which_contains_threeElementsOf_10_returns_10_withQueueSize_4_on_removing_6_times() {
        CustomQueue<Object> customQueue = new CustomQueue<>(8);
        Object item = 10;
        for(int i = 0; i < 8; i++)
            customQueue.enqueue(item);
        for(int i = 0; i < 5; i++) {
            customQueue.remove();
        }
        assertEquals(item, customQueue.remove());
        assertEquals(2, customQueue.getSize());
        assertEquals(4, customQueue.getQueueSize());
    }

    ///////HERE
    @Test
    public void givenDefaultQueue_is_empty_returns_null_on_poll() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertNull(customQueue.dequeue());
    }

    @Test
    public void givenDefaultQueue_which_contains_oneElementOf_10_returns_1_withSizeOf_0_on_poll() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        Object item = 10;
        customQueue.enqueue(item);
        assertEquals(item, customQueue.dequeue());
        assertEquals(0, customQueue.getSize());
    }

    @Test
    public void givenQueueOfSize_10_which_contains_oneElementOf_10_returns_10_withSizeOf_0_on_poll() {
        CustomQueue<Object> customQueue = new CustomQueue<>(10);
        Object item = 10;
        customQueue.enqueue(item);
        assertEquals(item, customQueue.dequeue());
        assertEquals(0, customQueue.getSize());
    }

    @Test
    public void givenQueueOfSize_8_which_contains_threeElementsOf_10_returns_10_withQueueSize_4_on_poll_6_times() {
        CustomQueue<Object> customQueue = new CustomQueue<>(8);
        Object item = 10;
        for(int i = 0; i < 8; i++)
            customQueue.enqueue(item);
        for(int i = 0; i < 5; i++) {
            customQueue.dequeue();
        }
        assertEquals(item, customQueue.remove());
        assertEquals(2, customQueue.getSize());
        assertEquals(4, customQueue.getQueueSize());
    }
}