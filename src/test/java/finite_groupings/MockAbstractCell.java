package finite_groupings;

import javax.annotation.Nonnull;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Harry Clarke (harry-clarke@outlook.com).
 * @since 12/11/2017.
 */
public class MockAbstractCell<E> extends AbstractCell<E> {

	public MockAbstractCell(final @Nonnull Set<E> possibilities) {
		super(possibilities);
	}

	@Override
	public void updatePossibilities() {
		informPossibilityListeners();
	}

	private CellValueListener<E> extendListener(final CellValueListener<E> listener) {
		return (c, v) -> {
			assertEquals(this, c);
			listener.onCellValueUpdate(c, v);
		};
	}

	private CellPossibilityListener<E> extendListener(final CellPossibilityListener<E> listener) {
		return (c, p) -> {
			assertEquals(this, c);
			listener.onCellPossibilityUpdate(c, p);
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellListener(final @Nonnull CellValueListener<E> valueListener,
								final @Nonnull CellPossibilityListener<E> possibilityListener) {
		super.addCellListener(
				extendListener(valueListener),
				extendListener(possibilityListener)
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellListener(final @Nonnull CellValueListener<E> listener) {
		super.addCellListener(extendListener(listener));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCellListener(final @Nonnull CellValueListener<E> listener,
								final @Nonnull E value) {
		super.addCellListener(extendListener(listener), value);
	}

}
