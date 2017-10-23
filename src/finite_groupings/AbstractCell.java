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
	public void addCellPossibilityListener(final @Nonnull CellPossibilityListener<E> listener) {
		possibilityListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellValueListener(final @Nonnull CellValueListener<E> listener) {
		valueUpdater.addCellValueListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellValueListener(final @Nonnull CellValueListener<E> listener, final @Nonnull E value) {
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
		informPossibilityListeners();
	}

	/**
	 * {@inheritDoc}
	 * Todo: Prevent two different set implementations from being used.
	 */
	@Override
	public void reducePossibilities(final Set<E> possibilities) {
		this.possibilities = Sets.intersection(this.possibilities, possibilities);
		informPossibilityListeners();
	}

	/**
	 * Calls all possibility listeners and informs them that this cell has been updated.
	 */
	protected final void informPossibilityListeners() {
		synchronized (possibilityListeners) {
			synchronized (possibilities) {
				possibilityListeners.parallelStream()
						.forEach(l -> l.onCellPossibilityUpdate(this, possibilities));
			}
		}
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
			vagueListeners.parallelStream().forEach(l -> l.onCellValueUpdate(AbstractCell.this, value));
			specificListeners.get(value).parallelStream().forEach(l -> l.onCellValueUpdate(AbstractCell.this, value));
		}
	}
}
