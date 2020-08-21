package i_paper_maths_puzzle.pojos;

import i_paper_maths_puzzle.pojos.Operator;

/**
 * @author Harry Clarke (harry-clarke@outlook.com).
 * @since 03/10/2017.
 */
public class Line {

	public static final int CELL_COUNT = 3;
	public static final int OPERATOR_COUNT = 2;
	public static final int LINE_LENGTH = CELL_COUNT + OPERATOR_COUNT;

	private final Integer[] cells = new Integer[CELL_COUNT];
	private final Operator[] operators = new Operator[OPERATOR_COUNT];

	public Line(final Integer cell0,
				final Operator op1,
				final Integer cell1,
				final Operator op2,
				final Integer cell2) {
		cells[0] = cell0;
		cells[1] = cell1;
		cells[2] = cell2;
		operators[0] = op1;
		operators[1] = op2;
	}

	public Integer getCell(final int index) {
		return cells[index];
	}

	public Operator getOperator(final int index) {
		return operators[index];
	}
}
