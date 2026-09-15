package customqueue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomQueueTest {

    @Test
    public void givenDefaultConstructor_returnsSizeOf_0_andIsEmpty() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        assertEquals(0, customQueue.size());
        assertTrue(customQueue.isEmpty());
    }

    @Test
    public void givenConstructor_withSize_negative_1_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CustomQueue<Integer>(-1));
    }

    @Test
    public void givenConstructor_takingCollection_onNullCollection_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CustomQueue<Integer>(null));
    }

    @Test
    public void givenConstructor_takingCollectionWith10Values_returnsQueueWithSize_10() {
        List<Integer> list = IntStream.range(0, 10).boxed().toList();
        CustomQueue<Integer> customQueue = new CustomQueue<>(list);
        assertEquals(10, customQueue.size());
        assertFalse(customQueue.isEmpty());
    }

    @Test
    public void givenConstructor_takingCollectionWith17Values_triggersExpand() {
        List<Integer> list = IntStream.range(0, 17).boxed().toList();
        CustomQueue<Integer> customQueue = new CustomQueue<>(list);
        assertEquals(17, customQueue.size());
        assertFalse(customQueue.isEmpty());
    }

    @Test
    public void givenDefaultQueue_returns_NullPointerException_whenAdding_null() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        assertThrows(NullPointerException.class, ()-> customQueue.add(null));
    }

    @Test
    public void givenQueue_ofSize_32_triggersSizeExpansion() {
        CustomQueue<Integer> customQueue = new CustomQueue<>(32);
        assertTrue(customQueue.isEmpty());
    }

    @Test
    public void givenQueue_ofSize_10_returns_NullPointerException_whenAdding_null() {
        CustomQueue<Integer> customQueue = new CustomQueue<>(10);
        assertThrows(NullPointerException.class, ()-> customQueue.add(null));
    }

    @Test
    public void givenDefaultQueue_returns_true_whenAdding_valueOf_10() {
        CustomQueue<Object> customQueue = new CustomQueue<>();
        assertTrue(customQueue.add(10));
        assertEquals(1, customQueue.size());
    }

    @Test
    public void givenEmptyQueue_onIsEmpty_returnsTrue() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        assertTrue(customQueue.isEmpty());
    }

    @Test
    public void givenNonEmptyQueue_onIsEmpty_returnsFalse() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        customQueue.add(1);
        assertFalse(customQueue.isEmpty());
    }

    @Test
    public void whenAdding17Items_triggersExpand() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        IntStream.range(0, 17).forEach(customQueue::add);
        assertEquals(17, customQueue.size());
    }

    @Test
    public void whenAddAll_collectionWith_17Items_triggersExpand() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        List<Integer> items = new ArrayList<>();
        IntStream.range(0, 17).forEach(items::add);
        assertTrue(customQueue.addAll(items));
        assertEquals(17, customQueue.size());
    }

    @Test
    void addAll_collectionWithNull_throwsNullPointerException() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        List<Integer> items = Arrays.asList(1, 2, null, 4);
        assertThrows(NullPointerException.class, () -> customQueue.addAll(items));
    }

    @Test
    void addAll_sameCollection_throwsIllegalArgumentException() {
        CustomQueue<Integer> customQueue = new CustomQueue<>();
        List<Integer> items = Arrays.asList(1, 2, 4);
        customQueue.addAll(items);
        assertThrows(IllegalArgumentException.class, () -> customQueue.addAll(customQueue));
    }

    @Test
    void remove_emptyQueue_throwsNoSuchElementException() {
        CustomQueue<Integer> queue = new CustomQueue<>(4);
        assertThrows(NoSuchElementException.class, queue::remove);
    }

    @Test
    void remove_withElements_returnsHeadAndDecreasesSize() {
        CustomQueue<Integer> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList(10, 20, 30));

        assertEquals(10, queue.remove());
        assertEquals(2, queue.size());
        assertEquals(20, queue.peek());
    }

    @Test
    void removeOn_emptyQueue_returnsFalse() {
        CustomQueue<Integer> queue = new CustomQueue<>(4);
        assertFalse(queue.remove(Integer.valueOf(10)));
    }

    @Test
    void removeObject_missingItem_returnsFalse() {
        CustomQueue<Integer> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList(10, 20, 30));

        assertFalse(queue.remove(Integer.valueOf(99)));
        assertEquals(3, queue.size());
    }

    @Test
    void removeObject_nullItem_returnsFalse() {
        CustomQueue<Integer> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList(10, 20, 30));

        assertFalse(queue.remove(null));
        assertEquals(3, queue.size());
    }
    @Test
    void removeObject_emptyQueue_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertFalse(queue.remove("A"));
        assertEquals(0, queue.size());
    }

    @Test
    void removeObject_nullTarget_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.remove(null));
        assertEquals(3, queue.size());
    }

    @Test
    void removeObject_elementNotFound_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.remove("Z"));
        assertEquals(3, queue.size());
    }

    @Test
    void removeObject_headElement_removesAndPreservesOrder() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.remove("A"));
        assertEquals(2, queue.size());
        assertEquals("B", queue.peek());
        assertEquals("B", queue.poll());
        assertEquals("C", queue.poll());
    }

    @Test
    void removeObject_middleElement_removesAndShiftsElements() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D"));

        assertTrue(queue.remove("B"));
        assertEquals(3, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("C", queue.poll());
        assertEquals("D", queue.poll());
    }

    @Test
    void removeObject_tailElement_removesAndDecrementsSize() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.remove("C"));
        assertEquals(2, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("B", queue.poll());
        assertTrue(queue.isEmpty());
    }

    @Test
    void removeObject_duplicateElements_removesFirstOccurrenceOnly() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "A", "C"));

        assertTrue(queue.remove("A"));
        assertEquals(3, queue.size());
        assertEquals("B", queue.poll());
        assertEquals("A", queue.poll());
        assertEquals("C", queue.poll());
    }

    @Test
    void removeObject_afterPollOperations_removesCorrectElement() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("X", "Y", "A", "B", "C"));
        assertEquals("X", queue.poll());
        assertEquals("Y", queue.poll());

        assertTrue(queue.remove("B"));
        assertEquals(2, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("C", queue.poll());
    }

    @Test
    void contains_emptyQueue_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertFalse(queue.contains("A"));
    }

    @Test
    void contains_nullTarget_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.contains(null));
    }

    @Test
    void contains_elementPresent_returnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.contains("B"));
    }

    @Test
    void contains_elementNotPresent_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.contains("Z"));
    }

    @Test
    void contains_boundaryElements_returnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("Head", "Middle", "Tail"));

        assertTrue(queue.contains("Head"));
        assertTrue(queue.contains("Tail"));
    }

    @Test
    void contains_polledElement_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("Polled", "Active1", "Active2"));

        assertEquals("Polled", queue.poll());

        assertFalse(queue.contains("Polled"));
        assertTrue(queue.contains("Active1"));
        assertTrue(queue.contains("Active2"));
    }

    @Test
    void containsAll_variousCollections_returnsExpectedResult() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D"));

        assertTrue(queue.containsAll(Arrays.asList("A", "C")));
        assertFalse(queue.containsAll(Arrays.asList("A", "Z")));
    }

    @Test
    void containsAll_emptyCollectionEmptyQueue_returnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertTrue(queue.containsAll(Collections.emptyList()));
    }

    @Test
    void containsAll_emptyCollectionNonEmptyQueue_returnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.containsAll(Collections.emptyList()));
    }

    @Test
    void containsAll_nullCollection_throwsNullPointerException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertThrows(NullPointerException.class, () -> queue.containsAll(null));
    }

    @Test
    void containsAll_nonEmptyCollectionEmptyQueue_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertFalse(queue.containsAll(List.of("A")));
    }

    @Test
    void containsAll_allElementsPresent_returnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D"));

        assertTrue(queue.containsAll(Arrays.asList("A", "C")));
        assertTrue(queue.containsAll(Arrays.asList("D", "B", "A")));
    }

    @Test
    void containsAll_partiallyMatchingCollection_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.containsAll(Arrays.asList("A", "Z")));
        assertFalse(queue.containsAll(Arrays.asList("X", "Y", "Z")));
    }

    @Test
    void containsAll_targetCollectionWithNull_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.containsAll(Arrays.asList("A", null)));
    }

    @Test
    void containsAll_afterPollOperations_returnsFalseForPolledElements() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D"));

        assertEquals("A", queue.poll());

        assertFalse(queue.containsAll(Arrays.asList("A", "B")));
        assertTrue(queue.containsAll(Arrays.asList("B", "C", "D")));
    }

    @Test
    void clear_emptyQueue_remainsEmpty() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
    }

    @Test
    void clear_populatedQueue_resetsState() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D"));

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
        assertNull(queue.poll());
    }

    @Test
    void clear_reuseQueue_addsNewElementsCorrectly() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        queue.clear();

        assertTrue(queue.add("X"));
        assertTrue(queue.add("Y"));
        assertEquals(2, queue.size());
        assertEquals("X", queue.poll());
        assertEquals("Y", queue.poll());
        assertTrue(queue.isEmpty());
    }

    @Test
    void clear_afterPolls_resetsIndicesAndEmptiesQueue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D"));
        queue.poll();
        queue.poll();

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertFalse(queue.contains("C"));
        assertFalse(queue.contains("D"));
    }

    @Test
    void removeAll_nullCollection_throwsNullPointerException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertThrows(NullPointerException.class, () -> queue.removeAll(null));
    }

    @Test
    void removeAll_emptyCollection_returnsFalseAndDoesNotModifyQueue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.removeAll(Collections.emptyList()));
        assertEquals(3, queue.size());
    }

    @Test
    void removeAll_matchingElements_removesTargetElementsAndPreservesOrder() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D", "B", "E"));

        assertTrue(queue.removeAll(Arrays.asList("B", "D")));
        assertEquals(3, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("C", queue.poll());
        assertEquals("E", queue.poll());
    }

    @Test
    void removeAll_noMatchingElements_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.removeAll(Arrays.asList("X", "Y")));
        assertEquals(3, queue.size());
    }

    @Test
    void removeAll_allMatchingElements_emptiesQueue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.removeAll(Arrays.asList("A", "B", "C")));
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
    }

    @Test
    void removeAll_afterPollOperations_operatesCorrectlyWithNonZeroHeadIndex() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("X", "Y", "A", "B", "C", "D"));

        queue.poll();
        queue.poll();

        assertTrue(queue.removeAll(Arrays.asList("A", "C")));
        assertEquals(2, queue.size());
        assertEquals("B", queue.poll());
        assertEquals("D", queue.poll());
    }

    @Test
    void retainAll_nullCollection_throwsNullPointerException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertThrows(NullPointerException.class, () -> queue.retainAll(null));
    }

    @Test
    void retainAll_emptyQueue_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertFalse(queue.retainAll(List.of("A", "B")));
        assertEquals(0, queue.size());
    }

    @Test
    void retainAll_emptyCollection_clearsQueueAndReturnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.retainAll(Collections.emptyList()));
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
    }

    @Test
    void retainAll_matchingSubset_retainsMatchingElementsAndPreservesOrder() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C", "D", "E"));

        assertTrue(queue.retainAll(Arrays.asList("B", "D", "Z")));
        assertEquals(2, queue.size());
        assertEquals("B", queue.poll());
        assertEquals("D", queue.poll());
        assertTrue(queue.isEmpty());
    }

    @Test
    void retainAll_noMatchingElements_clearsQueueAndReturnsTrue() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertTrue(queue.retainAll(Arrays.asList("X", "Y", "Z")));
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void retainAll_allElementsMatch_returnsFalseAndUnchanged() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.retainAll(Arrays.asList("A", "B", "C", "D")));
        assertEquals(3, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("B", queue.poll());
        assertEquals("C", queue.poll());
    }

    @Test
    void retainAll_withDuplicates_retainsAllOccurrences() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "A", "C", "B"));

        assertTrue(queue.retainAll(List.of("A")));
        assertEquals(2, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("A", queue.poll());
    }

    @Test
    void retainAll_afterPollOperations_operatesCorrectlyWithNonZeroHeadIndex() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("X", "Y", "A", "B", "C", "D"));

        queue.poll();
        queue.poll();

        assertTrue(queue.retainAll(Arrays.asList("B", "D")));
        assertEquals(2, queue.size());
        assertEquals("B", queue.poll());
        assertEquals("D", queue.poll());
    }

    @Test
    void retainAll_selfRetention_returnsFalse() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertFalse(queue.retainAll(queue));
        assertEquals(3, queue.size());
    }

    @Test
    void offer_validElement_returnsTrueAndIncrementsSize() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertTrue(queue.offer("A"));
        assertEquals(1, queue.size());
        assertEquals("A", queue.peek());
    }

    @Test
    void offer_nullElement_throwsNullPointerException() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertThrows(NullPointerException.class, () -> queue.offer(null));
        assertEquals(0, queue.size());
    }

    @Test
    void offer_multipleElements_maintainsFIFOOrder() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertTrue(queue.offer("A"));
        assertTrue(queue.offer("B"));
        assertTrue(queue.offer("C"));

        assertEquals(3, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("B", queue.poll());
        assertEquals("C", queue.poll());
    }

    @Test
    void offer_exceedsInitialCapacity_expandsArrayAndSucceeds() {
        CustomQueue<String> queue = new CustomQueue<>(2);

        assertTrue(queue.offer("A"));
        assertTrue(queue.offer("B"));
        assertTrue(queue.offer("C"));

        assertEquals(3, queue.size());
        assertEquals("A", queue.peek());
    }

    @Test
    void offer_afterPollOperations_appendsCorrectlyAtTail() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.offer("X");
        queue.offer("Y");

        assertEquals("X", queue.poll());

        assertTrue(queue.offer("Z"));
        assertEquals(2, queue.size());
        assertEquals("Y", queue.poll());
        assertEquals("Z", queue.poll());
    }

    @Test
    void iterator_emptyQueue_hasNextReturnsFalseAndNextThrows() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        Iterator<String> it = queue.iterator();

        assertNotNull(it);
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator_traversesElementsInFIFOOrder() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");
        queue.add("C");

        Iterator<String> it = queue.iterator();

        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertTrue(it.hasNext());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator_afterPollOperations_respectsHeadIndexOffset() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("X");
        queue.add("Y");
        queue.add("A");
        queue.add("B");

        queue.poll();
        queue.poll();

        Iterator<String> it = queue.iterator();

        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_remove_removesLastReturnedElement() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");
        queue.add("C");

        Iterator<String> it = queue.iterator();
        assertEquals("A", it.next());
        assertEquals("B", it.next());

        it.remove();

        assertEquals(2, queue.size());
        assertEquals("A", queue.poll());
        assertEquals("C", queue.poll());
    }

    @Test
    void iterator_remove_calledBeforeNextThrowsIllegalStateException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");

        Iterator<String> it = queue.iterator();

        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void iterator_remove_calledTwiceConsecutivelyThrowsIllegalStateException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");

        Iterator<String> it = queue.iterator();
        it.next();
        it.remove();

        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void iterator_failFast_structuralModificationThrowsConcurrentModificationException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");

        Iterator<String> it = queue.iterator();
        queue.add("C");

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void toArray_emptyQueue_returnsEmptyArray() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        Object[] result = queue.toArray();

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void toArray_populatedQueue_returnsArrayWithElementsInOrder() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");
        queue.add("C");

        Object[] result = queue.toArray();

        assertArrayEquals(new Object[]{"A", "B", "C"}, result);
    }

    @Test
    void toArray_returnsNewArrayInstance() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");

        Object[] result = queue.toArray();
        result[0] = "MUTATED";

        assertEquals("A", queue.peek());
    }

    @Test
    void toArray_afterPollOperations_respectsHeadIndexOffset() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("X");
        queue.add("Y");
        queue.add("A");
        queue.add("B");

        queue.poll();
        queue.poll();

        Object[] result = queue.toArray();

        assertEquals(2, result.length);
        assertArrayEquals(new Object[]{"A", "B"}, result);
    }

    @Test
    void toArray_nullArray_throwsNullPointerException() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");

        assertThrows(NullPointerException.class, () -> queue.toArray((String[]) null));
    }

    @Test
    void toArray_smallerArray_allocatesNewArrayOfSameTypeAndCorrectSize() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        String[] input = new String[0];
        String[] result = queue.toArray(input);

        assertNotEquals(input, result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"A", "B", "C"}, result);
    }

    @Test
    void toArray_exactSizeArray_populatesAndReturnsSameArrayInstance() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        String[] input = new String[3];
        String[] result = queue.toArray(input);

        assertEquals(input, result);
        assertArrayEquals(new String[]{"A", "B", "C"}, result);
    }

    @Test
    void toArray_largerArray_populatesAndSetsNullSentinel() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B"));

        String[] input = new String[]{"X", "Y", "Z", "W", "V"};
        String[] result = queue.toArray(input);

        assertEquals(input, result);
        assertEquals("A", result[0]);
        assertEquals("B", result[1]);
        assertNull(result[2]);
        assertEquals("W", result[3]);
    }

    @Test
    void toArray_incompatibleArrayType_throwsArrayStoreException() {
        CustomQueue<Object> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");

        assertThrows(ArrayStoreException.class, () -> queue.toArray(new Integer[2]));
    }

    @Test
    void toString_emptyQueue_returnsEmptyBrackets() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertEquals("[]", queue.toString());
    }

    @Test
    void toString_singleElement_returnsFormattedString() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");

        assertEquals("[A]", queue.toString());
    }

    @Test
    void toString_multipleElements_returnsCommaSeparatedFormattedString() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("A", "B", "C"));

        assertEquals("[A, B, C]", queue.toString());
    }

    @Test
    void toString_afterPollOperations_respectsHeadIndexOffset() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.addAll(Arrays.asList("X", "Y", "A", "B"));

        queue.poll();
        queue.poll();

        assertEquals("[A, B]", queue.toString());
    }

    @Test
    void element_emptyQueue_throwsNoSuchElementException() {
        CustomQueue<String> queue = new CustomQueue<>(4);

        assertThrows(NoSuchElementException.class, queue::element);
    }

    @Test
    void element_singleElement_returnsHeadWithoutRemoving() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");

        assertEquals("A", queue.element());
        assertEquals(1, queue.size());
    }

    @Test
    void element_multipleElements_returnsFirstElementWithoutRemoving() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("A");
        queue.add("B");
        queue.add("C");

        assertEquals("A", queue.element());
        assertEquals(3, queue.size());
        assertEquals("A", queue.element());
    }

    @Test
    void element_afterPollOperations_respectsHeadIndexOffset() {
        CustomQueue<String> queue = new CustomQueue<>(4);
        queue.add("X");
        queue.add("Y");
        queue.add("Z");

        assertEquals("X", queue.poll());

        assertEquals("Y", queue.element());
        assertEquals(2, queue.size());
    }
}