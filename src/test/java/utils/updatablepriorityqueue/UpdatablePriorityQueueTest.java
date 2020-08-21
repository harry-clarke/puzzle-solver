package utils.updatablepriorityqueue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (harry-clarke@outlook.com).
 * @since 30/10/2017.
 */
@SuppressWarnings({"RedundantOperationOnEmptyContainer", "SuspiciousMethodCalls", "ConstantConditions"})
class UpdatablePriorityQueueTest {

    @BeforeAll
    static void testInit() {
        final UpdatablePriorityQueue<?> priorityQueue = new UpdatablePriorityQueue<>();

        assertNotNull(priorityQueue.getPriorities());
        assertNotNull(priorityQueue.getQueue());

        assertNull(priorityQueue.peek());
        assertThrows(NoSuchElementException.class, priorityQueue::element);

        assertNull(priorityQueue.poll());
        assertThrows(NoSuchElementException.class, priorityQueue::remove);


        assertEquals(priorityQueue.getQueue().comparator(), priorityQueue.comparator());
        assertEquals(0, priorityQueue.size());
        assertTrue(priorityQueue.isEmpty());

        assertFalse(priorityQueue.iterator().hasNext());

        assertEquals(0, priorityQueue.toArray().length);
    }

    UpdatablePriorityQueue<Boolean> queue;

    @BeforeEach
    void setUp() {
        queue = new UpdatablePriorityQueue<>();
    }

    @Test
    void testAdd() {
        assertTrue(queue.add(true));
        final Priority<Boolean> priority = queue.getPriority(true);

        assertTrue(queue.contains(true));
        assertTrue(queue.contains(priority));
        assertEquals(1, queue.size());

        assertEquals(0, priority.getCount());
        assertEquals(true, priority.getValue());
    }

    @Test
    void testAddAll() {
        final Set<Boolean> set = Set.of(true, false);
        assertTrue(queue.addAll(set));
        final Set<Priority<Boolean>> prioritySet = set.stream().map(queue::getPriority).collect(Collectors.toSet());

        assertTrue(queue.containsAll(set));
        assertTrue(queue.containsAll(prioritySet));
        assertEquals(2, queue.size());
    }

    @Test
    void testRemove() {
        queue.add(true);
        assertEquals(queue.getPriority(true), queue.remove());
        assertThrows(NoSuchElementException.class, queue::remove);
    }

    @Test
    void testRemoveElement() {
        queue.add(true);
        assertTrue(queue.remove(true));

        assertEquals(0, queue.size());
    }

    @Test
    void testRemoveAll() {
        final Set<Boolean> set = Set.of(true, false);
        queue.addAll(set);

        assertTrue(queue.removeAll(set));
        assertEquals(0, queue.size());
    }

    @Test
    void testClear() {
        final Set<Boolean> set = Set.of(true, false);
        queue.addAll(set);

        queue.clear();
        assertEquals(0, queue.size());
    }

    @Test
    void testPeek() {
        queue.add(true);
        final Priority<Boolean> priority = queue.getPriority(true);

        assertEquals(priority, queue.peek());
        assertEquals(1, queue.size());
    }

    @Test
    void testPoll() {
        queue.add(true);
        final Priority<Boolean> priority = queue.getPriority(true);

        assertEquals(priority, queue.poll());
        assertNull(queue.poll());
        assertEquals(0, queue.size());
    }

    @Test
    void testToArray() {
        queue.add(true);
        final Boolean[] expected = new Boolean[]{true};

        assertTrue(Arrays.equals(expected, queue.toArray(new Boolean[0])));
        assertTrue(Arrays.equals(expected, queue.toArray(new Boolean[1])));

        final Boolean[] nullCheck = new Boolean[2];
        Arrays.fill(nullCheck, false);
        queue.toArray(nullCheck);
        assertTrue(Arrays.equals(new Boolean[]{true, null}, queue.toArray(nullCheck)));

        assertTrue(Arrays.equals(expected, queue.toArray()));
    }

    @Test
    void testRetainAll() {
        queue.addAll(Set.of(true, false));
        queue.retainAll(Set.of(true));
        assertEquals(1, queue.size());

        assertTrue(queue.contains(true));
        assertFalse(queue.contains(false));
    }

    @RepeatedTest(10)
    void testPriorityChange() {
        queue.add(true);
        queue.add(false);
        final Priority<Boolean> priorityTrue = queue.getPriority(true);
        final Priority<Boolean> priorityFalse = queue.getPriority(false);

        priorityTrue.incrementCount();

        assertEquals(priorityTrue, queue.poll());
        assertEquals(priorityFalse, queue.poll());
    }
}