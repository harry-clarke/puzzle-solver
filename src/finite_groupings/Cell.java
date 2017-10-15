package finite_groupings;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public interface Cell<E> {

	/**
	 * Registers the cell to a specific grouping.
	 * A cell could belong to multiple groupings,
	 * and should notify all groupings of any changes to its state.
	 * @param grouping The grouping to register.
	 */
	void addToGrouping(Grouping<E> grouping);

	/**
	 * Looks at the state of the grouping and determines what possible values this cell could have.
	 * When an assumption is correct, the cell is set and then the runnable is called.
	 * The Runnable is used to add any extra information about other cells that this assumption opens up.
	 * For example, which cells should update their assumptions.
	 */
	void updateAssumptions();

	/**
	 * Sets the value of a cell.
	 * @param value The value to set the cell to.
	 */
	void setValue(E value);
}
