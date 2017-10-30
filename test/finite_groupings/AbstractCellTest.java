package finite_groupings;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 22/10/2017.
 */
class AbstractCellTest {

	public static final ImmutableSet<Boolean> FULL_SET = ImmutableSet.of(true, false);

	AbstractCell<Boolean> abstractCell;

	@BeforeEach
	void setUp() {
		abstractCell = new BooleanAbstractCell();
	}

	@Test
	void testAddCellPossibilityListener() {
		final TestListener listener = new TestListener();
		abstractCell.addCellListener(
				(c, v) -> {
					throw new AssertionFailedError();
					},
				(c, p) -> {
					assertEquals(abstractCell, c);
					assertEquals(FULL_SET, p);
					listener.call();
				});
		abstractCell.updatePossibilities();
		assertTrue(listener.called);
	}

	@Test
	void testAddCellValueListenerSpecificCalled() {
		final TestListener listener = new TestListener();
		abstractCell.addCellListener((c, v) -> {
			assertEquals(abstractCell, c);
			assertEquals(true, v);
			listener.call();
		}, true);
		abstractCell.setValue(true);
		assertTrue(listener.called);
	}

	@Test
	void testAddCellValueListenerSpecificIgnored() {
		abstractCell.addCellListener((c, v) -> {
			throw new AssertionFailedError("Shouldn't be called.");
		}, true);
		abstractCell.setValue(false);
	}

	@Test
	void testAddCellValueListenerVague() {
		final TestListener listener = new TestListener();
		abstractCell.addCellListener((c, v) -> {
			assertEquals(abstractCell, c);
			assertEquals(true, v);
			listener.call();
		});
		abstractCell.setValue(true);
		assertTrue(listener.called);
	}

	@Test
	void testSetValue() {
		final TestListener listener = new TestListener();
		abstractCell.addCellListener((c, v) -> {
			assertEquals(abstractCell, c);
			assertEquals(true, v);
			listener.call();
		});

		assertFalse(abstractCell.getValue().isPresent());
		assertEquals(FULL_SET, abstractCell.getPossibilities());

		abstractCell.setValue(true);
		assertTrue(abstractCell.getValue().isPresent());
		assertEquals(true, abstractCell.getValue().get());

		assertEquals(1, abstractCell.getPossibilities().size());
		assertTrue(abstractCell.getPossibilities().contains(true));
	}

	@Test
	void testReducePossibilities() {
		final TestListener listener = new TestListener();

		abstractCell.addCellListener((c, p) -> {
			assertEquals(abstractCell, c);
			listener.call();
		});
		assertTrue(abstractCell.getPossibilities().equals(FULL_SET));
		abstractCell.reducePossibilities(Set.of(true));
		assertEquals(Set.of(true), abstractCell.getPossibilities());

		assertTrue(listener.called);
	}



	public static class BooleanAbstractCell extends AbstractCell<Boolean> {
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
	public static class TestListener {
		public boolean called = false;

		public void call() {
			called = true;
		}
	}
}