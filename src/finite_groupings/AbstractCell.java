package finite_groupings;

import com.google.common.collect.*;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.google.common.base.Functions.forSupplier;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
public abstract class AbstractCell<E> implements Cell<E> {

	private final ValueUpdater valueUpdater;
	private final Set<Cell.CellPossibilityListener<E>> possibilityListeners;
	private Set<E> possibilities;
	private E value;

	public AbstractCell(final Set<E> possibilities) {
		this.value = null;
		this.valueUpdater = new ValueUpdater(possibilities);
		this.possibilityListeners = Sets.newConcurrentHashSet();
		this.possibilities = Sets.newConcurrentHashSet(possibilities);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellListener(final @Nonnull CellValueListener<E> valueListener,
								final @Nonnull CellPossibilityListener<E> possibilityListener) {
		valueUpdater.addCellValueListener(valueListener);
		possibilityListeners.add(possibilityListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellListener(final @Nonnull CellValueListener<E> listener) {
		valueUpdater.addCellValueListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellListener(final @Nonnull CellValueListener<E> listener, final @Nonnull E value) {
		valueUpdater.addCellValueListener(listener, value);
	}

	/**
	 * Sets the value of a cell.
	 * @param value The value to set the cell to.
	 */
	@Override
	public void setValue(final @Nonnull E value) {
		this.value = value;
		valueUpdater.onCellValueUpdate(value);
		possibilities = Set.of(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void updatePossibilities();

	/**
	 * {@inheritDoc}
	 * Todo: Prevent two different set implementations from being used.
	 */
	@Override
	public void reducePossibilities(final Set<E> possibilities) {
		this.possibilities = Sets.intersection(this.possibilities, possibilities);
		onPossibilityUpdate();
	}

	/**
	 * Removes the given value as a possibility for this cell.
	 *
	 * @param possibility The possibility to remove.
	 */
	@Override
	public void removePossibility(final E possibility) {
		possibilities.remove(possibility);
		onPossibilityUpdate();
	}

	protected final void onPossibilityUpdate() {
		final Optional<E> value = possibilities.stream().findFirst();
		if (possibilities.size() == 1 && value.isPresent())
			setValue(value.get());
		else
			informPossibilityListeners();
	}

	/**
	 * Calls all possibility listeners and informs them that this cell has been updated.
	 */
	protected final void informPossibilityListeners() {
		possibilityListeners.forEach(l -> l.onCellPossibilityUpdate(this, possibilities));
	}

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public Set<E> getPossibilities() {
		return Sets.newConcurrentHashSet(possibilities);
	}

	public @Nonnull Optional<E> getValue() {
		return Optional.ofNullable(value);
	}

	/**
	 * @return True if a value has been set, otherwise false.
	 */
	@Override
	public boolean hasValue() {
		return getValue().isPresent();
	}

	/**
	 * Wrapper class for updating all value listeners.
	 */
	class ValueUpdater {
		private final Set<Cell.CellValueListener<E>> vagueListeners;
		private final Multimap<E, Cell.CellValueListener<E>> specificListeners;

		public ValueUpdater(final Set<E> possibilities) {
			vagueListeners = Sets.newConcurrentHashSet();
			specificListeners = HashMultimap.create();
		}

		public void addCellValueListener(final @Nonnull CellValueListener<E> listener) {
			this.vagueListeners.add(listener);
		}

		public void addCellValueListener(final @Nonnull CellValueListener<E> listener, @Nonnull E value) {
			this.specificListeners.put(value, listener);
		}

		public void onCellValueUpdate(final E value) {
			vagueListeners.forEach(l -> l.onCellValueUpdate(AbstractCell.this, value));
			specificListeners.get(value).forEach(l -> l.onCellValueUpdate(AbstractCell.this, value));
		}
	}
}
