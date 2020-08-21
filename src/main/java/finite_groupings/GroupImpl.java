package finite_groupings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 */
public class GroupImpl<E> implements Group<E> {

	protected static final String TOO_MANY_CELLS_EXCEPTION_MSG = "More cells than values to put in those cells.";
	protected static final String LOST_CELL_EXCEPTION_MSG = "Cell update called by a stranger cell.";

	/**
	 * All cells in the grouping.
	 */
	private final ImmutableSet<Cell<E>> allCells;
	/**
	 * All cells that still don't have a value.
	 */
	private final Set<Cell<E>> unpairedCells;
	/**
	 * All values that haven't yet been paired with a cell.
	 */
	private final Set<E> values;

	public GroupImpl(final @Nonnull Set<Cell<E>> cells,
					 final @Nonnull Set<E> values) {
		if (cells.size() > values.size())
			throw new IllegalArgumentException(TOO_MANY_CELLS_EXCEPTION_MSG);
		this.allCells = ImmutableSet.copyOf(cells);
		//noinspection ConstantConditions
		this.unpairedCells = Sets.newHashSet(Sets.filter(allCells, cell -> !cell.hasValue()));
		this.values = Sets.newHashSet(values);

		// Sets the group to listen for changes to any of its cells.
		cells.parallelStream().forEach(c -> c.addCellListener(this::onCellValueSet, this::onCellPossibilityUpdate));
	}

	/**
	 * @return All cells belonging to this grouping.
	 */
	public Set<Cell<E>> getAllCells() {
		return allCells;
	}

	/**
	 * Kicks off any logical assumptions that can be made about the group now that a cell has different possibilities.
	 * @see finite_groupings.Cell.CellPossibilityListener
	 * @param cell A cell in the grouping that's been updated.
	 * @param possibilities The possible values that this cell could now be.
	 *                      Values should always be a subset of the previous possible values.
	 */
	public void onCellPossibilityUpdate(final @Nonnull Cell<E> cell, final @Nonnull Set<E> possibilities) {
		if (!allCells.contains(cell))
			throw new IllegalStateException(LOST_CELL_EXCEPTION_MSG);
		updateCellGroupings(cell);
	}

	/**
	 * @see finite_groupings.Cell.CellValueListener
	 * @param cell A cell in the grouping that's been updated.
	 * @param value The value that the cell has been paired with.
	 */
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
		if (subsets.size() < possibilities.size()) {
			// Todo: Check if this is a child of a subset.
			return; // This isn't the root of a subset.
		}
		// Make sure this is the smallest subset we can make.
		if (subsets.size() > possibilities.size()) {
			// Todo: Add logic to check the subset for an even smaller subset.
			return;
		}
		// Remove these possibilities from cells outside the subgroup.
		unpairedCells.forEach(c -> c.removePossibilities(possibilities));
	}
}
