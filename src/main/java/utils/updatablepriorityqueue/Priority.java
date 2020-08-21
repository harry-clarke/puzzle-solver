package utils.updatablepriorityqueue;

import javax.annotation.Nonnull;

/**
 * Acts as a wrapper for a single value in a priority queue.
 * The counter determines where the value is placed in the queue,
 * and any changes to the counter will update the parent priority queue accordingly.
 * @param <E> The value type to be wrapped.
 */
public class Priority<E> implements Comparable<Priority<?>> {

	public static int DEFAULT_COUNT = 0;

	/**
	 * The parent queue that this priority belongs to.
	 */
	private final UpdatablePriorityQueue<E> queue;
	private int count;

	private final E value;

	protected Priority(final @Nonnull UpdatablePriorityQueue<E> queue,
					   final @Nonnull E value) {
		this.queue = queue;
		this.count = DEFAULT_COUNT;
		this.value = value;
	}

	public int getCount() {
		return count;
	}

	public E getValue() {
		return value;
	}

	public void resetCount() {
		setCount(DEFAULT_COUNT);
	}

	public void incrementCount() {
		setCount(count + 1);
	}

	public void decrementCount() {
		setCount(count - 1);
	}

	public void setCount(final int count) {
		this.count = count;
		queue.updatePriority(this);
	}

	@Override
	public int compareTo(@Nonnull Priority<?> o) {
		return Integer.compare(o.count, count);
	}
}
