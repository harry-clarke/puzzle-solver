package finite_groupings;

import utils.updatablepriorityqueue.Priority;
import utils.updatablepriorityqueue.UpdatablePriorityQueue;

import javax.annotation.Nonnull;

/**
 * A Cell-specific wrapper of the {@link UpdatablePriorityQueue}.
 * Responsible for using the priority queue to update a high-priority cell when asked.
 */
public class CellUpdater<E> {

	private final UpdatablePriorityQueue<Cell<E>> queue;

	public CellUpdater() {
		this.queue = new UpdatablePriorityQueue<>();
	}

	public UpdatablePriorityQueue<Cell<E>> getQueue() {
		return queue;
	}

	/**
	 * Takes the cell with the largest priority and requests that it updates its set of potential values.
	 */
	public void update() {
		final Priority<Cell<E>> priority = queue.poll();
		if (priority == null)
			return;
		//
		synchronized (priority) {
			priority.getValue().updatePossibilities();
			priority.resetCount();
		}
	}


	/**
	 * Adds a value listener to the cell that removes it from the updater
	 * (once a cell knows its value, it doesn't need to keep looking).
	 * Adds a possibility listener to the cell that resets its priority in the queue
	 * (if the cell has just changed its potential values, there may be no point (need to verify)
	 * checking them again immediately after).
	 *
	 */
	public boolean add(final @Nonnull Cell<E> cell) {
		if(queue.add(cell)) {
			cell.addCellListener(
					(c, v) -> queue.remove(c),
					(c, p) -> queue.getPriority(c).resetCount()
			);
			return true;
		}
		return false;
	}
}
