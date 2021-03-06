package finite_groupings;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

/**
 * The basic representation of a cell.
 * The cell can either have a value, or a set of potential values.
 * The cell also provides listener services to notify other objects when the cell finds new information about its
 * potential value.
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
	 * Removes the given values as possibilities for this cell.
	 * Shouldn't fail even if one or more of the possibilities don't exist.
	 * @param possibilities The possibilities to remove.
	 */
	void removePossibilities(Set<E> possibilities);

	/**
	 * Removes the given value as a possibility for this cell.
	 * Shouldn't fail even if the possibility doesn't exist.
	 * @param possibility The possibility to remove.
	 */
	void removePossibility(E possibility);

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

	/**
	 * Listens for changes in the set of values that a cell could possibly be.
	 * Is not used when a cell is set to a specific value (i.e. when the set changes to singleton).
	 * @param <E> The value type.
	 */
	@FunctionalInterface
	interface CellPossibilityListener<E> {
		/**
		 * @param cell The cell that has been updated.
		 */
		void onCellPossibilityUpdate(@Nonnull Cell<E> cell, @Nonnull Set<E> possibilities);
	}

	/**
	 * Listens to a cell when it is assigned a value.
	 * @param <E> The value type.
	 */
	@FunctionalInterface
	interface CellValueListener<E> {
		/**
		 * @param cell The cell that has been updated.
		 * @param value The value that has been set.
		 */
		void onCellValueUpdate(@Nonnull Cell<E> cell, @Nonnull E value);
	}
}
