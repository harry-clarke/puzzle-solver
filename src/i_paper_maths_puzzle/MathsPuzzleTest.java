package i_paper_maths_puzzle;

import i_paper_maths_puzzle.pojos.*;
import i_paper_maths_puzzle.pojos.Number;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 04/10/2017.
 */
public class MathsPuzzleTest {

	public static final String[] EXAMPLE_1 = new String[] {
			"1-6+5=0",
			"* * *",
			"7-3*4=16",
			"* - *",
			"8+9-2=15",
			"= = =",
			"56,9,40"
	};

	public static final String[] EXAMPLE_2 = new String[] {
			" - +5=0",
			"* * *",
			"7-3*4=16",
			"* - *",
			"8+9-2=15",
			"= = =",
			"56,9,40"
	};

	public static final Cell[][] EXAMPLE_1_CELLS = {
			{Number.ONE, Operator.SUBTRACT, Number.SIX, Operator.ADD, Number.FIVE},
			{Operator.MULTIPLY, NullCell.INSTANCE, Operator.MULTIPLY, NullCell.INSTANCE, Operator.MULTIPLY, NullCell.INSTANCE, Operator.MULTIPLY},
			{Number.SEVEN, Operator.SUBTRACT, Number.THREE, Operator.MULTIPLY, Number.FOUR},
			{Operator.MULTIPLY, NullCell.INSTANCE, Operator.SUBTRACT, NullCell.INSTANCE, Operator.MULTIPLY},
			{Number.EIGHT, Operator.ADD, Number.NINE, Operator.SUBTRACT, Number.TWO}
	};

	public static final int[] EXAMPLE_1_ROW_ANSWERS = {0, 16, 15};

	public static final int[] EXAMPLE_1_COL_ANSWERS = {56, 9, 40};

	@Test
	public void testParser() {
		final MathsPuzzle puzzle = new MathsPuzzle(MathsPuzzleTest.EXAMPLE_1);
		assertPuzzleEquals(EXAMPLE_1_CELLS, EXAMPLE_1_ROW_ANSWERS, EXAMPLE_1_COL_ANSWERS, puzzle);
	}

	public static void assertPuzzleEquals(final Cell[][] grid, final int[] rowAnswers, final int[] colAnswers, final MathsPuzzle puzzle) {
		assertGridEquals(grid, puzzle);

		assertRowEquals(rowAnswers, puzzle);

		assertColEquals(colAnswers, puzzle);
	}

	public static void assertGridEquals(final Cell[][] grid, final MathsPuzzle puzzle) {
		for (int x = 0; x < Line.LINE_LENGTH; x++) {
			for (int y = 0; y < Line.LINE_LENGTH; y++) {
				assertEquals(grid[y][x], puzzle.getCell(x, y), x + "," + y);
			}
		}
	}

	private static void assertRowEquals(int[] rowAnswers, MathsPuzzle puzzle) {
		for (int y = 0; y < Line.CELL_COUNT; y++) {
			assertEquals(rowAnswers[y], puzzle.getRowAnswer(y));
		}
	}

	private static void assertColEquals(int[] colAnswers, MathsPuzzle puzzle) {
		for (int x = 0; x < Line.CELL_COUNT; x++) {
			assertEquals(colAnswers[x], puzzle.getColAnswer(x));
		}
	}
}