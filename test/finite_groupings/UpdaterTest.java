package finite_groupings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.updatablepriorityqueue.Priority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 05/11/2017.
 */
class UpdaterTest {

	Updater<Boolean> updater;

	@BeforeEach
	void setUp() {
		updater = new Updater<>();
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

		final Priority<Cell<Boolean>> priority = updater.getPriority(cell);
		priority.incrementCount();

		assertTrue(updater.contains(cell));
		assertFalse(listener.called);

		updater.update();
		assertEquals(0, priority.getCount());
		assertTrue(listener.called);
	}

	@Test
	void testValueUpdate() {
		final Cell<Boolean> cell = new AbstractCellTest.MockAbstractCell();

		updater.add(cell);

		assertTrue(updater.contains(cell));
		cell.setValue(true);
		assertFalse(updater.contains(cell));
	}

	@Test
	void testPossibilityUpdate() {
		final Updater<Integer> updater = new Updater<>();
		final Cell<Integer> cell = new AbstractCell<>(Set.of(1, 2, 3)) {
			@Override
			public void updatePossibilities() {
			}
		};

		updater.add(cell);

		Priority<Cell<Integer>> priority = updater.getPriority(cell);
		priority.incrementCount();

		cell.reducePossibilities(Set.of(1, 2));

		assertEquals(0, priority.getCount());
	}
}