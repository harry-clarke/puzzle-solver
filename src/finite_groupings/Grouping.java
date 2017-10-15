package finite_groupings;

import java.util.Map;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public interface Grouping<E> {
	void onCellAssumptionUpdate(Cell<E> cell, Map<E, Runnable> assumptions);

	void increaseCellPriority(Cell<E> cell);
}
