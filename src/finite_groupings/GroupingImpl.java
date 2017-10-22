package finite_groupings;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public class GroupingImpl<E> implements Grouping<E>, Cell.CellPossibilityListener<E> {

	private static final String TOO_MANY_CELLS_EXCEPTION_MSG = "More cells than values to put in those cells.";
	private static final String LOST_CELL_EXCEPTION_MSG = "Cell update called by a stranger cell.";

	private final Map<Cell<E>, CellPriority> cellPriorities;
	private final PriorityQueue<CellPriority> cellQueue;
	private final Set<E> values;

	public GroupingImpl(final Set<Cell<E>> cellQueue, final Set<E> values) {
		if (cellQueue.size() > values.size())
			throw new IllegalArgumentException(TOO_MANY_CELLS_EXCEPTION_MSG);

		this.cellPriorities = Maps.asMap(cellQueue, CellPriority::new);
		this.cellQueue = new PriorityQueue<>(cellPriorities.values());
		this.values = values;

		cellQueue.parallelStream().forEach(c -> c.addCellPossibilityListener(this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCellPossibilityUpdate(final @Nonnull Cell<E> cell, final @Nonnull Set<E> possibilities) {
		if (!cellPriorities.containsKey(cell))
			throw new IllegalStateException(LOST_CELL_EXCEPTION_MSG);
		synchronized (values) {
			if (possibilities.size() == 1) {
				for (final E possibility : possibilities) {
					setCell(cell, possibility);
				}
			}
		}
	}

	@Override
	public void increaseCellPriority(final Cell<E> cell) {
		final CellPriority cellPriority = cellPriorities.get(cell);
		cellQueue.remove(cellPriority);
		cellPriority.count += 1;
		cellQueue.add(cellPriority);
	}

	public void update() {
		final CellPriority cell;
		synchronized (cellQueue) {
			if (cellQueue.isEmpty())
				return;
			cell = cellQueue.poll();
			cell.count = 0;
			cellQueue.add(cell);
		}
		cell.cell.updatePossibilities();
	}

	private void setCell(final Cell<E> cell, final E value) {
		values.remove(value);
		cellQueue.remove(cellPriorities.get(cell));
		cell.setValue(value);
		update();
	}

	/**
	 * If there's only one value left, and only cell left, pair them together.
	 */
	private void lastManStandingCheck() {
		if (values.size() != 1)
			return;
		final E value = values.stream().findFirst().orElseThrow(IllegalStateException::new);
		if (cellQueue.size() != 1)
			throw new IllegalStateException();
		final CellPriority cellPriority = cellQueue.poll();
		setCell(cellPriority.cell, value);
	}

	private class CellPriority implements Comparable<CellPriority> {

		private int count;

		private final Cell<E> cell;

		CellPriority(Cell<E> cell) {
			this.count = 1;
			this.cell = cell;
		}

		@Override
		public int compareTo(@Nonnull CellPriority o) {
			return Integer.compare(count, o.count);
		}
	}
}
