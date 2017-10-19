package finite_groupings;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public abstract class AbstractCell<E> implements Cell<E> {

	private final Set<Grouping<E>> groupings;

	private E value;

	public AbstractCell() {
		groupings = Sets.newConcurrentHashSet();
		value = null;
	}

	/**
	 * Registers the cell to a specific grouping.
	 * A cell could belong to multiple groupings,
	 * and should notify all groupings of any changes to its state.
	 *
	 * @param grouping The grouping to register.
	 */
	@Override
	public void addToGrouping(final Grouping<E> grouping) {
		groupings.add(grouping);
	}

	/**
	 * Looks at the state of the grouping and determines what possible values this cell could have.
	 * When an assumption is correct, the cell is set and then the runnable is called.
	 * The Runnable is used to add any extra information about other cells that this assumption opens up.
	 * For example, which cells should update their assumptions.
	 */
	@Override
	public final void updateAssumptions() {
		final Map<E,Runnable> assumptions = createAssumptions();
		groupings.forEach(g -> g.onCellAssumptionUpdate(this, assumptions));
	}

	protected abstract Map<E,Runnable> createAssumptions();

	/**
	 * Sets the value of a cell.
	 *
	 * @param value The value to set the cell to.
	 */
	@Override
	public void setValue(final E value) {
		this.value = value;
	}

	public Optional<E> getValue() {
		return Optional.ofNullable(value);
	}

	public Set<Grouping<E>> getGroupings() {
		return groupings;
	}
}
