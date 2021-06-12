package finite_groupings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;
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
	@Override
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

	@Override
	public Set<E> getValues() {
		return values;
	}

	protected Set<Cell<E>> getUnpairedCells() {
		return Collections.unmodifiableSet(unpairedCells);
	}


	private static class Context<E> {
		Set<Cell<E>> includedCells = new HashSet<>();
		Set<E> includedValues = new HashSet<>();

		private Context<E> copy() {
			final Context<E> copy = new Context<>();
			copy.includedCells = new HashSet<>(this.includedCells);
			copy.includedValues = new HashSet<>(this.includedValues);
			return copy;
		}
	}

	protected void updateCellGroupings(final @Nonnull Cell<E> cell) {
	    findSubGroups(new Context<>(), cell);
	}

	protected Group<E> contextToSubGroup(final Context<E> context) {
		final Set<Cell<E>> outcasts = Set.copyOf(Sets.difference(getUnpairedCells(), context.includedCells));
		for (final Cell<E> cell : outcasts) {
			cell.removePossibilities(context.includedValues);
		}
	    return new GroupImpl<>(context.includedCells, context.includedValues);
    }

	protected List<Group<E>> findSubGroups(final Context<E> context, final Cell<E> cell) {
		context.includedCells.add(cell);
		context.includedValues.addAll(cell.getPossibilities());
		if (isCompleteSubGroup(context)) {
			if (getUnpairedCells().size() == context.includedCells.size())
				return List.of();
			final Group<E> subGroup = contextToSubGroup(context);
			// Todo continue to look for other sub groups
			return List.of(subGroup);
		}
		final Context<E> contextCopy = context.copy();
        final E variable = cell.getPossibilities().stream().findAny().orElseThrow();
        return findSubGroups(contextCopy, variable);
	}

	protected List<Group<E>> findSubGroups(final Context<E> context, final E value) {
		final List<Cell<E>> cells = getUnpairedCells()
                .stream()
				.filter(c -> !context.includedCells.contains(c))
                .filter(c -> c.getPossibilities().contains(value))
                .sorted(Comparator.comparingInt(c -> c.getPossibilities().size()))
                .toList();
		for (final Cell<E> cell : cells) {
		    final Context<E> contextCopy = context.copy();
		    final List<Group<E>> subGroups = findSubGroups(contextCopy, cell);
		    if (!subGroups.isEmpty()) {
		        return subGroups;
            }
        }
		return List.of();
	}

	protected static boolean isCompleteSubGroup(final Context<?> context) {
		return context.includedCells.size() == context.includedValues.size();
	}
}
