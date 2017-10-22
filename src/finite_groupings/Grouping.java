package finite_groupings;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public interface Grouping<E> {

	/**
	 * Increases the likelihood that a cell will be asked to update its possibilities
	 * via the {@link Cell#updatePossibilities()} method.
	 * @param cell The cell to increase the priority of.
	 */
	void increaseCellPriority(Cell<E> cell);
}
