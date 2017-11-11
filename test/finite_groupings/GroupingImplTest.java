package finite_groupings;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static finite_groupings.AbstractCellTest.FULL_SET;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 23/10/2017.
 */
class GroupingImplTest {

	List<Cell<Boolean>> cells;
	GroupingImpl<Boolean> grouping;

	@BeforeEach
	void setUp() {
		cells = List.of(new AbstractCellTest.MockAbstractCell(),
				new AbstractCellTest.MockAbstractCell());
		grouping = new GroupingImpl<>(Sets.newHashSet(cells), FULL_SET);
	}

	@BeforeAll
	static void testInit() {
		final GroupingImplTest test = new GroupingImplTest();
		test.setUp();
		assertEquals(FULL_SET, test.grouping.getValues());
		assertEquals(Sets.newHashSet(test.cells), test.grouping.getAllCells());
		assertEquals(Sets.newHashSet(test.cells), test.grouping.getUnpairedCells());
	}

	@Test
	void testTooManyCellsException() {
		final Set<Cell<Boolean>> cells = Set.of(
				new AbstractCellTest.MockAbstractCell(),
				new AbstractCellTest.MockAbstractCell(),
				new AbstractCellTest.MockAbstractCell()
		);
		assertThrows(IllegalArgumentException.class, () -> new GroupingImpl<>(cells, FULL_SET));
	}

	@Test
	void testLostCellException() {
		final Cell<Boolean> lostCell = new AbstractCellTest.MockAbstractCell();
		assertThrows(IllegalStateException.class,
				() -> grouping.onCellPossibilityUpdate(lostCell, Set.of()));
	}

	@Test
	void testOnCellPossibilityUpdate() {
		grouping.onCellPossibilityUpdate(cells.get(0), Set.of());
	}

	@Test
	void testOnCellValueUpdate() {
		cells.get(0).setValue(true);

		assertEquals(Set.of(), grouping.getValues());
		assertEquals(Set.of(), grouping.getUnpairedCells());
	}

	/*
	 * Tests:
	 * lastManStandingCheck
	 */

	@Test
	void testLastManStandingCheck() {
		Cell<Boolean> trueCell = cells.get(0);
		Cell<Boolean> falseCell = cells.get(1);

		assertFalse(trueCell.hasValue());
		assertFalse(falseCell.hasValue());

		trueCell.setValue(true);

		final Optional<Boolean> trueVal = trueCell.getValue();
		assertTrue(trueVal.isPresent());
		assertTrue(trueVal.get());

		final Optional<Boolean> falseVal = falseCell.getValue();
		assertTrue(falseVal.isPresent());
		assertFalse(falseVal.get());
	}
}