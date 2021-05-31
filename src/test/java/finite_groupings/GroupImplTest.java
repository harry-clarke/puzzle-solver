package finite_groupings;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static finite_groupings.AbstractCellTest.FULL_SET;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (harry-clarke@outlook.com).
 * @since 23/10/2017.
 */
class GroupImplTest {

	List<MockAbstractCell<Boolean>> cells;
	GroupImpl<Boolean> grouping;

	@BeforeEach
	void setUp() {
		cells = new MockAbstractCellFactory<>(FULL_SET).createAll();
		grouping = new GroupImpl<>(Sets.newHashSet(cells), FULL_SET);
	}

	@BeforeAll
	static void testInit() {
		final GroupImplTest test = new GroupImplTest();
		test.setUp();
		assertEquals(FULL_SET, test.grouping.getValues());
		assertEquals(Sets.newHashSet(test.cells), test.grouping.getAllCells());
		assertEquals(Sets.newHashSet(test.cells), test.grouping.getUnpairedCells());
	}

	@Test
	void testTooManyCellsException() {
		final MockAbstractCellFactory<Boolean> factory = new MockAbstractCellFactory<>(FULL_SET);
		final Set<Cell<Boolean>> cells = Stream.generate(factory).limit(3).collect(Collectors.toSet());
		assertThrows(IllegalArgumentException.class, () -> new GroupImpl<>(cells, FULL_SET));
	}

	@Test
	void testLostCellException() {
		final Cell<Boolean> lostCell = new MockAbstractCell<>(FULL_SET);
		assertThrows(IllegalStateException.class,
				() -> grouping.onCellPossibilityUpdate(lostCell, Set.of()));
	}

	@Test
	void testOnCellValueUpdate() {
		cells.get(0).setValue(true);

		assertEquals(Set.of(), grouping.getValues());
		assertEquals(Set.of(), grouping.getUnpairedCells());
	}

	/*
	 * Tests:
	 * Last man standing check.
	 * No subset check.
	 */

	/**
	 * Checks that if there is only one cell remaining and only one value to go with it
	 * that they are paired together to complete the grouping:
	 * <br/>
	 * <table>
	 *     <tr>
	 *         <th>Cell</th>
	 *         <th>Possibilities</th>
	 *     </tr>
	 *     <tr>
	 *         <td>A</td>
	 *         <td>1</td>
	 *     </tr>
	 *     <tr>
	 *         <td>B</td>
	 *         <td>?</td>
	 *     </tr>
	 *     <tr>
	 *         <td>Left</td>
	 *         <td>2</td>
	 *     </tr>
	 *     <br/>
	 * </table>
	 * Becomes:
	 * <br/>
	 * <tr>
	 *     <th>Cell</th>
	 *     <th>Possibilities</th>
	 * </tr>
	 * <tr>
	 *     <td>A</td>
	 *     <td>1</td>
	 * </tr>
	 * <tr>
	 *     <td>B</td>
	 *     <td>2</td>
	 * </tr>
	 * <tr>
	 *     <td>Left</td>
	 *     <td></td>
	 * </tr>
	 * </table>
	 * <br/>
	 */
	@Test
	void checkLastManStanding() {
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

	/**
	 * Checks the scenario where a cell's possibilities are reduced yet this doesn't create a subset of any kind.
	 * <br/>
	 * <table>
	 *     <tr>
	 *         <th>Cell</th>
	 *         <th>Possibilities</th>
	 *     </tr>
	 *     <tr>
	 *         <td>A</td>
	 *         <td>1,2,3</td>
	 *     </tr>
	 *     <tr>
	 *         <td>B</td>
	 *         <td>1,2,3</td>
	 *     </tr>
	 *     <tr>
	 *         <td>C</td>
	 *         <td>1,2,3</td>
	 *     </tr>
	 * </table>
	 * <br/>
	 * Set to:
	 * <br/>
	 * <table>
	 *     <tr>
	 *         <th>Cell</th>
	 *         <th>Possibilities</th>
	 *     </tr>
	 *     <tr>
	 *         <td>A</td>
	 *         <td>1,2</td>
	 *     </tr>
	 *     <tr>
	 *         <td>B</td>
	 *         <td>1,2,3</td>
	 *     </tr>
	 *     <tr>
	 *         <td>C</td>
	 *         <td>1,2,3</td>
	 *     </tr>
	 * </table>
	 * <br/>
	 * Is left unchanged.
	 */
	@Test
	void checkNoSubset() {
		final GroupingImplFactory<Integer> factory = GroupingImplFactory.create(Set.of(1, 2, 3));
		final Group<Integer> group = factory.getGrouping();

		Spliterator<MockAbstractCell<Integer>> spliterator = factory.getCells().spliterator();
		spliterator.tryAdvance(c -> {
			// Removes possibility 3 from the first cell.
			c.removePossibility(3);
			// Check that the cell has been appropriately updated.
			assertEquals(Set.of(1, 2), c.getPossibilities());
		});

		// Check that the other 2 cells' possibilities are untouched.
		spliterator.forEachRemaining(c -> assertEquals(Set.of(1, 2, 3), c.getPossibilities()));
	}

	@Test
	void checkSmallerSubset() {
		final List<MockAbstractCell<Integer>> cells = MockAbstractCellFactory.createAll(Set.of(1, 2, 3));
		final MockAbstractCell<Integer> cell = new MockAbstractCell<>(Set.of(1, 2, 3, 4));
		cells.add(cell);
		final Group<Integer> grouping = new GroupImpl<>(Sets.newHashSet(cells), Set.of(1, 2, 3, 4));

		cell.removePossibility(1);
		assertTrue(cell.getValue().isPresent());
		assertEquals(4, (int) cell.getValue().get());

	}
}