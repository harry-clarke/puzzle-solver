package finite_groupings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public class GroupingImpl<E> implements Grouping<E> {

	protected static final String TOO_MANY_CELLS_EXCEPTION_MSG = "More cells than values to put in those cells.";
	protected static final String LOST_CELL_EXCEPTION_MSG = "Cell update called by a stranger cell.";

	private final ImmutableSet<Cell<E>> allCells;
	private final Set<Cell<E>> unpairedCells;
	private final Set<E> values;

	public GroupingImpl(final @Nonnull Set<Cell<E>> cells,
						final @Nonnull Set<E> values) {
		if (cells.size() > values.size())
			throw new IllegalArgumentException(TOO_MANY_CELLS_EXCEPTION_MSG);
		this.allCells = ImmutableSet.copyOf(cells);
		this.unpairedCells = Sets.newHashSet(Sets.filter(allCells, cell -> !cell.hasValue()));
		this.values = Sets.newHashSet(values);

		cells.parallelStream().forEach(c -> c.addCellListener(this::onCellValueSet, this::onCellPossibilityUpdate));
	}

	/**
	 * @return All cells belonging to this grouping.
	 */
	public Set<Cell<E>> getAllCells() {
		return allCells;
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCellPossibilityUpdate(final @Nonnull Cell<E> cell, final @Nonnull Set<E> possibilities) {
		if (!allCells.contains(cell))
			throw new IllegalStateException(LOST_CELL_EXCEPTION_MSG);
		updateCellGroupings(cell);
	}

	protected void onCellValueSet(final @Nonnull Cell<E> cell, final @Nonnull E value) {
		values.remove(value);
		unpairedCells.remove(cell);
		unpairedCells.forEach(c -> c.removePossibility(value));
	}

	public Set<E> getValues() {
		return values;
	}

	protected Set<Cell<E>> getUnpairedCells() {
		return Collections.unmodifiableSet(unpairedCells);
	}

	protected void updateCellGroupings(final @Nonnull Cell<E> cell) {
		final Set<E> possibilities = cell.getPossibilities();
		final Set<Cell<E>> subsets = unpairedCells.stream()
				.filter(c -> possibilities.containsAll(c.getPossibilities()))
				.collect(Collectors.toSet());
		// See if a subset can be formed.
		if (subsets.size() < possibilities.size())
			return;
		// Make sure this is the smallest subset we can make.
		if (subsets.size() > possibilities.size()) {
			// Todo: Add logic to check the subset for an even smaller subset.
			return;
		}
		// Remove these possibilities from cells outside the subgroup.
		unpairedCells.forEach(c -> c.removePossibilities(possibilities));
	}
}
