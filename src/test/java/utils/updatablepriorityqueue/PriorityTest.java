package utils.updatablepriorityqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;
import static utils.updatablepriorityqueue.Priority.DEFAULT_COUNT;

/**
 * @author Harry Clarke (harry-clarke@outlook.com).
 * @since 30/10/2017.
 */
class PriorityTest {

	MockUpdatablePriorityQueue priorityQueue;
	Priority<Object> priority;
	Object object;

	@BeforeEach
	void setUp() {
		priorityQueue = new MockUpdatablePriorityQueue();
		object = new Object();
		priority = new Priority<>(priorityQueue, object);
	}

	@Test
	void testDefaults() {
		assertEquals(DEFAULT_COUNT, priority.getCount());
		assertEquals(object, priority.getValue());
		assertFalse(priorityQueue.called);
	}

	@Test
	void testDecrementCount() {
		priority.decrementCount();
		assertTrue(priorityQueue.called);
		assertEquals(DEFAULT_COUNT - 1, priority.getCount());
	}

	@Test
	void testIncrementCount() {
		priority.incrementCount();
		assertTrue(priorityQueue.called);
		assertEquals(DEFAULT_COUNT + 1, priority.getCount());
	}

	@Test
	void testSetCount() {
		priority.setCount(DEFAULT_COUNT + 1);
		assertTrue(priorityQueue.called);
		assertEquals(DEFAULT_COUNT + 1, priority.getCount());
	}

	@Test
	void testResetCount() {
		setResetCount(DEFAULT_COUNT + 1);

		priority.resetCount();
		assertTrue(priorityQueue.called);
		assertEquals(DEFAULT_COUNT, priority.getCount());
	}

	@Test
	void testCompareTo() {
		final Priority<Object> priority2 = new Priority<>(priorityQueue, new Object());
		priority.incrementCount();
		assertEquals(1, priority2.compareTo(priority));
		assertEquals(-1, priority.compareTo(priority2));
	}

	private void setResetCount(final int count) {
		priority.setCount(count);
		priorityQueue.called = false;
	}

	private class MockUpdatablePriorityQueue extends UpdatablePriorityQueue<Object> {

		boolean called = false;

		@Override
		protected void updatePriority(@Nonnull Priority<Object> priority) {
			called = true;
			super.updatePriority(priority);
		}
	}
}