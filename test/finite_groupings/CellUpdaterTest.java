package finite_groupings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.updatablepriorityqueue.Priority;

import java.util.Set;

import static finite_groupings.AbstractCellTest.FULL_SET;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 05/11/2017.
 */
class CellUpdaterTest {

	CellUpdater<Boolean> updater;

	@BeforeEach
	void setUp() {
		updater = new CellUpdater<>();
	}

	@Test
	void testEmptyUpdate() {
		updater.update();
	}

	@Test
	void testUpdate() {
		final TestListener listener = new TestListener();
		Cell<Boolean> cell = new AbstractCell<>(Set.of(true, false)) {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void updatePossibilities() {
				listener.call();
			}
		};

		updater.add(cell);

		// Todo: Add queue method for changing priority count. Abstract away priorities from external classes.
		final Priority<Cell<Boolean>> priority = updater.getQueue().getPriority(cell);
		priority.incrementCount();

		assertTrue(updater.getQueue().contains(cell));
		assertFalse(listener.called);

		updater.update();
		assertEquals(0, priority.getCount());
		assertTrue(listener.called);
	}

	@Test
	void testValueUpdate() {
		final MockAbstractCell<Boolean> cell = new MockAbstractCell<>(FULL_SET);

		updater.add(cell);

		assertTrue(updater.getQueue().contains(cell));
		cell.setValue(true);
		assertFalse(updater.getQueue().contains(cell));
	}

	@Test
	void testPossibilityUpdate() {
		final CellUpdater<Integer> updater = new CellUpdater<>();
		final Cell<Integer> cell = new AbstractCell<>(Set.of(1, 2, 3)) {
			@Override
			public void updatePossibilities() {
			}
		};

		updater.add(cell);

		Priority<Cell<Integer>> priority = updater.getQueue().getPriority(cell);
		priority.incrementCount();

		cell.reducePossibilities(Set.of(1, 2));

		assertEquals(0, priority.getCount());
	}
}