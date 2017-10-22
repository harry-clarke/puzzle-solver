package finite_groupings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 22/10/2017.
 */
class AbstractCellTest {

	public static final Set<Boolean> FULL_SET = Set.of(true, false);

	AbstractCell<Boolean> abstractCell;

	@BeforeEach
	void setUp() {
		abstractCell = new BooleanAbstractCell();
	}

	@Test
	void testAddCellPossibilityListener() {
		final TestListener listener = new TestListener();
		abstractCell.addCellPossibilityListener((c, p) -> {
			assertEquals(abstractCell, c);
			assertEquals(FULL_SET, p);
			listener.call();
		});
		abstractCell.updatePossibilities();
		assertTrue(listener.called);
	}

	private static class BooleanAbstractCell extends AbstractCell<Boolean> {
		public BooleanAbstractCell() {
			super(FULL_SET);
		}

		@Override
		public void updatePossibilities() {
			informPossibilityListeners();
		}
	}

	/**
	 * Todo: Replace with JMockit.
	 */
	private static class TestListener {
		public boolean called = false;

		public void call() {
			called = true;
		}
	}
}