package finite_groupings;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public interface Cell<E> {

	/**
	 * @param valueListener A listener to be called when the cell's value changes.
	 * @param possibilityListener A listener to be called when the cell's possibilities change.
	 *                            Not called when the cell can only be one value.
	 */
	void addCellListener(@Nonnull CellValueListener<E> valueListener,
						 @Nonnull CellPossibilityListener<E> possibilityListener);

	/**
	 * @param listener A listener to be called when the cell's value changes.
	 */
	void addCellListener(@Nonnull CellValueListener<E> listener);

	/**
	 * @param value The value that triggers a call to this listener.
	 * @param listener A listener to be called when the cell's value changes to the given value.
	 */
	void addCellListener(@Nonnull CellValueListener<E> listener, @Nonnull E value);

	/**
	 * Reduces the possibilities of values that this cell can be by intersecting
	 * its existing set of possibilities with the new set.
	 * @param possibilities A set of possibilities that this cell can be.
	 *                      If an existing possibility isn't in this set, it's removed,
	 *                      and vice verse.
	 */
	void reducePossibilities(Set<E> possibilities);

	/**
	 * Looks at the state of the cell and determines what possible values this cell could have.
	 * When an assumption is correct, the cell is set and then the runnable is called.
	 * The Runnable is used to add any extra information about other cells that this assumption opens up.
	 * For example, which cells should update their assumptions.
	 */
	void updatePossibilities();

	/**
	 * @return A set of all possible values for this cell.
	 * Returns a singleton set if the cell value is set.
	 */
	@Nonnull
	Set<E> getPossibilities();

	/**
	 * Sets the value of a cell.
	 * @param value The value to set the cell to.
	 */
	void setValue(@Nonnull E value);

	/**
	 * @return The value of the cell if set, otherwise Nothing.
	 */
	@Nonnull
	Optional<E> getValue();

	/**
	 * @return True if a value has been set, otherwise false.
	 */
	boolean hasValue();

	@FunctionalInterface
	interface CellPossibilityListener<E> {
		/**
		 * @param cell The cell that has been updated.
		 */
		void onCellPossibilityUpdate(@Nonnull Cell<E> cell, @Nonnull Set<E> possibilities);
	}

	@FunctionalInterface
	interface CellValueListener<E> {
		/**
		 * @param cell The cell that has been updated.
		 * @param value The value that has been set.
		 */
		void onCellValueUpdate(@Nonnull Cell<E> cell, @Nonnull E value);
	}
}
