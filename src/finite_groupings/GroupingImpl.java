package finite_groupings;

import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public class GroupingImpl<E> implements Grouping<E>, Cell.CellPossibilityListener<E> {

	protected static final String TOO_MANY_CELLS_EXCEPTION_MSG = "More cells than values to put in those cells.";
	protected static final String LOST_CELL_EXCEPTION_MSG = "Cell update called by a stranger cell.";

	private final Map<Cell<E>, CellPriority> cellPriorities;
	private final PriorityQueue<CellPriority> cellQueue;
	private final Set<E> values;

	public GroupingImpl(@Nonnull final Set<Cell<E>> cells, @Nonnull final Set<E> values) {
		if (cells.size() > values.size())
			throw new IllegalArgumentException(TOO_MANY_CELLS_EXCEPTION_MSG);

		this.cellPriorities = Maps.asMap(cells, CellPriority::new);
		this.cellQueue = new PriorityQueue<>(cellPriorities.values());
		this.values = values;

		cells.parallelStream().forEach(c -> c.addCellListener(this::setCell, this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCellPossibilityUpdate(final @Nonnull Cell<E> cell, final @Nonnull Set<E> possibilities) {
		if (!cellPriorities.containsKey(cell))
			throw new IllegalStateException(LOST_CELL_EXCEPTION_MSG);
	}

	@Override
	public void increaseCellPriority(final @Nonnull Cell<E> cell) {
		final CellPriority cellPriority = cellPriorities.get(cell);
		cellQueue.remove(cellPriority);
		cellPriority.count += 1;
		cellQueue.add(cellPriority);
	}

	public void update() {
//		Insert grouping techniques here.
		lastManStandingCheck();

		final CellPriority cell;
		if (cellQueue.isEmpty())
			return;
		cell = cellQueue.poll();
		cell.count = 0;
		cellQueue.add(cell);
		cell.cell.updatePossibilities();
	}

	public Map<Cell<E>, CellPriority> getCellPriorities() {
		return cellPriorities;
	}

	public PriorityQueue<CellPriority> getCellQueue() {
		return cellQueue;
	}

	public Set<E> getValues() {
		return values;
	}

	protected void setCell(final @Nonnull Cell<E> cell, @Nonnull final E value) {
		values.remove(value);
		cellQueue.remove(cellPriorities.get(cell));
		cell.setValue(value);
		update();
	}

	/**
	 * If there's only one value left, and only cell left, pair them together.
	 */
	protected void lastManStandingCheck() {
		if (values.size() != 1)
			return;
		final E value = values.stream().findFirst().orElseThrow(IllegalStateException::new);
		if (cellQueue.size() != 1)
			throw new IllegalStateException();
		final CellPriority cellPriority = cellQueue.poll();
		setCell(cellPriority.cell, value);
	}

	protected class CellPriority implements Comparable<CellPriority> {

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
