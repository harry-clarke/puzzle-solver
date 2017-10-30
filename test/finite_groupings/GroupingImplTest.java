package finite_groupings;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		cells = List.of(new AbstractCellTest.BooleanAbstractCell(),
				new AbstractCellTest.BooleanAbstractCell());
		grouping = new GroupingImpl<>(Sets.newHashSet(cells), FULL_SET);
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