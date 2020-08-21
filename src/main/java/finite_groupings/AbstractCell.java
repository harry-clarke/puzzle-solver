package finite_groupings;

import com.google.common.collect.*;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public abstract class AbstractCell<E> implements Cell<E> {

	private final ValueUpdater valueUpdater;
	private final Set<Cell.CellPossibilityListener<E>> possibilityListeners;
	private Set<E> possibilities;
	private E value;

	public AbstractCell(final Set<E> possibilities) {
		this.value = null;
		this.valueUpdater = new ValueUpdater();
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
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final @Nonnull E value) {
		this.value = value;
		possibilities = Set.of(value);
		valueUpdater.onCellValueUpdate(value);
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
	 * {@inheritDoc}
	 */
	@Override
	public void removePossibilities(final Set<E> possibilities) {
		this.possibilities.removeAll(possibilities);
		onPossibilityUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePossibility(final E possibility) {
		possibilities.remove(possibility);
		onPossibilityUpdate();
	}

	protected final void onPossibilityUpdate() {
		if (possibilities.size() == 1) {
			final Optional<E> value = possibilities.stream().findFirst();
			setValue(value.get());
		} else {
			informPossibilityListeners();
		}
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
	 * Container class for updating all value listeners.
	 */
	class ValueUpdater {
		/**
		 * Vague listeners want to be notified when the cell takes any value.
		 */
		private final Set<Cell.CellValueListener<E>> vagueListeners;

		/**
		 * Specific listeners only want to be notified when the cell has taken a specific value.
		 */
		private final Multimap<E, Cell.CellValueListener<E>> specificListeners;

		public ValueUpdater() {
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
