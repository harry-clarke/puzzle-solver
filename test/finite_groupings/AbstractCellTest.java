package finite_groupings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 15/10/2017.
 */
class AbstractCellTest {

	private AbstractCell<Boolean> abstractCell;

	@BeforeEach
	public void beforeEach() {
		abstractCell = new BooleanAbstractCell<>();
	}

	@Test
	public void testAddGrouping() {
		assertTrue(abstractCell.getGroupings().isEmpty());
		final TestGrouping<Boolean> testGrouping = new TestGrouping<>();
		abstractCell.addToGrouping(testGrouping);
		assertTrue(abstractCell.getGroupings().contains(testGrouping));
		assertEquals(1, abstractCell.getGroupings().size());
	}

	private static class TestGrouping<E> implements Grouping<E> {

		@Override
		public void onCellAssumptionUpdate(Cell<E> cell, Map<E, Runnable> assumptions) {

		}

		@Override
		public void increaseCellPriority(Cell<E> cell) {

		}
	}

	private static class BooleanAbstractCell<E> extends AbstractCell<E> {
		@Override
		protected Map<E, Runnable> createAssumptions() {
			return Collections.emptyMap();
		}
	}
}