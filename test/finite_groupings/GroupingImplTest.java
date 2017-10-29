package finite_groupings;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static finite_groupings.AbstractCellTest.FULL_SET;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 23/10/2017.
 */
class GroupingImplTest {

	Set<Cell<Boolean>> cells;
	GroupingImpl<Boolean> grouping;

	@BeforeEach
	void setUp() {
		cells = Set.of(new AbstractCellTest.BooleanAbstractCell(),
				new AbstractCellTest.BooleanAbstractCell());
		grouping = new GroupingImpl<>(cells, FULL_SET);
	}
}