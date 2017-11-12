package finite_groupings;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 22/10/2017.
 */
class AbstractCellTest {

	public static final ImmutableSet<Boolean> FULL_SET = ImmutableSet.of(true, false);

	protected TestListener listener;
	protected AbstractCell<Boolean> abstractCell;

	private static <E> void valueListenerAssertionError(final Cell<E> c, final E v) {
		throw new AssertionError();
	}

	@BeforeEach
	void setUp() {
		abstractCell = new MockAbstractCell();
		listener = new TestListener();
	}

	@Test
	void testAddCellPossibilityListener() {
		abstractCell.addCellListener(
				(c, v) -> {
					throw new AssertionError();
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
			throw new AssertionError("Shouldn't be called.");
		}, true);
		abstractCell.setValue(false);
	}

	@Test
	void testAddCellValueListenerVague() {
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
	void testReducePossibilitiesToValue() {
		abstractCell.addCellListener((c, v) -> {
			assertEquals(abstractCell, c);
			listener.call();
		});
		abstractCell.reducePossibilities(Set.of(true));

		assertEquals(Set.of(true), abstractCell.getPossibilities());

		Optional<Boolean> value = abstractCell.getValue();
		assertTrue(value.isPresent());
		assertEquals(true, value.get());

		assertTrue(listener.called);
	}

	@Test
	void testRemovePossibilityToValue() {
		abstractCell.addCellListener((c, v) -> {
			assertEquals(abstractCell, c);
			listener.call();
		});
		abstractCell.removePossibility(false);

		assertEquals(Set.of(true), abstractCell.getPossibilities());

		Optional<Boolean> value = abstractCell.getValue();
		assertTrue(value.isPresent());
		assertEquals(true, value.get());

		assertTrue(listener.called);
	}

	@Test
	void testReducePossibilities() {
		assertRemovePossibility(c -> c.reducePossibilities(Set.of(1, 2)));
	}

	@Test
	void testRemovePossibility() {
		assertRemovePossibility(c -> c.removePossibility(3));
	}

	private void assertRemovePossibility(final Consumer<Cell<Integer>> consumer) {
		final AbstractCell<Integer> cell = new AbstractCell<>(Set.of(1, 2, 3)) {
			@Override
			public void updatePossibilities() {
				informPossibilityListeners();
			}
		};
		cell.addCellListener(
				AbstractCellTest::valueListenerAssertionError,
				(c, p) -> {
					assertEquals(Set.of(1, 2), p);
					assertEquals(cell, c);
					listener.call();
				}
		);
		cell.removePossibility(3);
		assertTrue(listener.called);
	}

	public static class MockAbstractCell extends AbstractCell<Boolean> {
		public MockAbstractCell() {
			super(FULL_SET);
		}

		@Override
		public void updatePossibilities() {
			informPossibilityListeners();
		}
	}

}