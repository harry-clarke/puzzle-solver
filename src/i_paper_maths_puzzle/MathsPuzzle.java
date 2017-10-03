package i_paper_maths_puzzle;

import i_paper_maths_puzzle.cell.Cell;

import static i_paper_maths_puzzle.Line.LINE_LENGTH;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 03/10/2017.
 */
public class MathsPuzzle {

	private final Cell[][] cells = new Cell[LINE_LENGTH][LINE_LENGTH];
	
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

	private void parseLines(String[] lines) {
		if (lines.length != LINE_LENGTH)
			throw new IllegalArgumentException("Too many lines. Expected " + LINE_LENGTH);
		for (int j = 0; j < LINE_LENGTH; j++) {
			final String line = lines[j];
			if (line.length() != LINE_LENGTH)
				throw new IllegalArgumentException("Line too long. Expected " + LINE_LENGTH);
			for (int i = 0; i < LINE_LENGTH; i++) {
				final Cell c = Cell.parseChar(line.charAt(i));
				cells[i][j] = c;
			}
		}
	}
}
