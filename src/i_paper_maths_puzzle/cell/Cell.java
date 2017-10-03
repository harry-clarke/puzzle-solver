package i_paper_maths_puzzle.cell;


/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 03/10/2017.
 */
public interface Cell {

	enum CellType { NULL, OPERATOR, NUMBER }

	CellType getType();

	static Cell parseChar(final char c) {
		switch (c) {
			case ' ': return new NullCell();

			case '*': return Operator.MULTIPLY;
			case '/': return Operator.DIVIDE;
			case '+': return Operator.ADD;
			case '-': return Operator.SUBTRACT;

			case '1': return Number.ONE;
			case '2': return Number.TWO;
			case '3': return Number.THREE;
			case '4': return Number.FOUR;
			case '5': return Number.FIVE;
			case '6': return Number.SIX;
			case '7': return Number.SEVEN;
			case '8': return Number.EIGHT;
			case '9': return Number.NINE;
			default: throw new IllegalArgumentException("Invalid Char.");
		}
	}
}
