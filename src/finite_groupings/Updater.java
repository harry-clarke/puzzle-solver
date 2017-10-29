package finite_groupings;

import utils.updatablepriorityqueue.Priority;
import utils.updatablepriorityqueue.UpdatablePriorityQueue;

import javax.annotation.Nonnull;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 29/10/2017.
 */
public class Updater<E> extends UpdatablePriorityQueue<Cell<E>> {

	public void update() {
		final Priority<Cell<E>> priority = poll();
		if (priority == null)
			return;
		priority.resetCount();
		priority.getValue().updatePossibilities();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Adds a value listener to the cell that removes it from the updater.
	 * Adds a possibility listener to the cell that resets its priority in the queue.
	 */
	@Override
	public boolean add(@Nonnull Cell<E> cell) {
		cell.addCellListener(
				(c, v) -> remove(v),
				(c, p) -> getPriority(c).resetCount()
		);
		return super.add(cell);
	}
}
