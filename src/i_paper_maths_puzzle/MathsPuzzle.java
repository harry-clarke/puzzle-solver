package i_paper_maths_puzzle;

import i_paper_maths_puzzle.cell.Cell;

import static i_paper_maths_puzzle.Line.CELL_COUNT;
import static i_paper_maths_puzzle.Line.LINE_LENGTH;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 03/10/2017.
 */
public class MathsPuzzle {

	private final Cell[][] cells = new Cell[LINE_LENGTH][LINE_LENGTH];

	private final int[] rowAnswers = new int[CELL_COUNT];
	private final int[] colAnswers = new int[CELL_COUNT];

	public MathsPuzzle(final String[] lines) {
		parseLines(lines);
		assertValid();
	}

	private void assertValid() {
		final int[] blanks = new int[] {1,3};
		for (final int i : blanks) {
			for (final int j : blanks) {
				if (cells[i][j].getType() != Cell.CellType.NULL)
					throw new IllegalArgumentException("Cell (" + i + "," + j + ") should be null");
			}
		}
	}

	public Cell getCell(final int x, final int y) {
		return cells[x][y];
	}

	public int getRowAnswer(final int y) {
		return rowAnswers[y];
	}

	public int getColAnswer(final int x) {
		return colAnswers[x];
	}

	private void parseLines(String[] lines) {
		for (int j = 0; j < LINE_LENGTH; j++) {
			final String line = lines[j];
			for (int i = 0; i < LINE_LENGTH; i++) {
				final Cell c = Cell.parseChar(line.charAt(i));
				cells[i][j] = c;
			}
		}

		for (int j = 0; j < LINE_LENGTH; j+=2) {
			rowAnswers[j/2] = Integer.parseInt(lines[j].substring(LINE_LENGTH+1));
		}

		final String[] colAnswersStr = lines[LINE_LENGTH+1].split(",");
		for (int i = 0; i < CELL_COUNT; i++) {
			colAnswers[i] = Integer.parseInt(colAnswersStr[i]);
		}
	}
}
