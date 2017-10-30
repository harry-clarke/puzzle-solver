package utils.updatablepriorityqueue;

import javax.annotation.Nonnull;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 29/10/2017.
 */
public class Priority<E> implements Comparable<Priority<?>> {

	public static int DEFAULT_COUNT = 0;

	private UpdatablePriorityQueue<E> queue;
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
	public int compareTo(@Nonnull Priority<?g> o) {
		return Integer.compare(count, o.count);
	}
}
